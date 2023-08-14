package com.example.lab02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;


import com.example.lab02.provider.Book;
import com.example.lab02.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    EditText bookID;
    EditText bookTitle;
    EditText bookISBN;
    EditText bookAuthor;
    EditText bookDescription;
    EditText bookPrice;
    ArrayList<Book> myList = new ArrayList<>();
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private BookViewModel mBookViewModel;
    RecycleAdapter adapter;
    DatabaseReference myRef;
    View touchFrame;
    int initialX;
    int initialY;
    int priceAdder;
    GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase();
    public static final String digits = "0123456789";
    public static final String alphaNumeric = upper + lower + digits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookKeeping","onCreate");
        setContentView(R.layout.drawer);

        bookID = findViewById(R.id.inputBookID);
        bookTitle = findViewById(R.id.inputTitle);
        bookISBN = findViewById(R.id.inputISBN);
        bookAuthor =  findViewById(R.id.inputAuthor);
        bookDescription = findViewById(R.id.inputDescription);
        bookPrice = findViewById(R.id.inputPrice);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MyDrawerListener());

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFields();
            }
        });

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        /* Create and instantiate the local broadcast receiver
           This class listens to messages come from class SMSReceiver
         */
        MyBroadcastReceiver myBroadCastReceiver = new MyBroadcastReceiver();

        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));


        adapter = new RecycleAdapter();

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        mBookViewModel.getAllBooks().observe(this, newData -> {
            adapter.setData(newData);
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frame1,new RecyclerViewFragment()).commit();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookstoreapp-fc599-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("Book/myBook");

        touchFrame = findViewById(R.id.touchFrameLayout);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        touchFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }

//    View.OnClickListener addListener =

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            Log.i("week11", "onSingleTapConfirmed");
            String newRandomISBN = generateNewRandomString(5);
            bookISBN.setText(newRandomISBN);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            Log.i("week11", "onLongPress");
            loadFields();
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            Log.i("week11", "onScroll");
            if (distanceY > 0 || distanceY < 0) {
                bookTitle.setText("untitled");
            }
            if (!bookPrice.getText().toString().equals("")) {
                double price = Double.parseDouble(bookPrice.getText().toString());
                double newPrice = price + (distanceX * -1);
                bookPrice.setText(String.format("%.2f", newPrice));
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX > 2000 || velocityY > 2000){
                Log.i("week11", "onFling");
                moveTaskToBack(true);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            Log.i("week11", "onDoubleTap");
            clearFields();
            return super.onDoubleTap(e);
        }
    }


    public static String generateNewRandomString(int length) {
        char[] buf;
        Random random=new Random();
        if (length < 1) throw new IllegalArgumentException();
        buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = alphaNumeric.charAt(random.nextInt(alphaNumeric.length()));
        return new String(buf);
    }

    View.OnClickListener undoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            removeLast();
        }
    };

    public void removeLast(){
        if (!(myList.size() == 0)){
            myList.remove(myList.size()-1);
        }else {
            Toast.makeText(MainActivity.this, "No books available to be remove", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeAll(){
        if (!(myList.size() == 0)){
            myList.clear();
        }else {
            Toast.makeText(MainActivity.this, "No books available to be remove", Toast.LENGTH_SHORT).show();
        }
    }

    class MyDrawerListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();
            if (id == R.id.drawer_add) {
                addFields();
            } else if (id == R.id.drawer_remove_last) {
                deleteLast();
            } else if (id == R.id.drawer_remove_all) {
                deleteAll();
            } else if (id == R.id.drawer_doublePrice){
                doubleThePrice();
            } else if (id == R.id.drawer_setISBN) {
                setISBN();
            } else if (id == R.id.drawer_list_books) {
                showList_usingGson();
            } else if (id == R.id.drawer_delete_unknown) {
                deleteUnknown();
            } else if (id == R.id.drawer_closeApp) {
                finish();
            }
            // close the drawer
            drawerLayout.closeDrawers();
            // tell the OS
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_clear) {
            clearFields();
        } else if (id == R.id.option_load) {
            loadFields();
        } else if (id == R.id.option_totalBooks) {
            Toast.makeText(this, "Total Books: " + String.valueOf(myList.size()), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        /*
         * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
         * */
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
             * Retrieve the message from the intent
             * */
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            /*
             * String Tokenizer is used to parse the incoming message
             * The protocol is to have the account holder name and account number separate by a semicolon
             * */

            StringTokenizer sT = new StringTokenizer(msg, "|");
            String bookIDToken = sT.nextToken();
            String bookTitleToken = sT.nextToken();
            String bookISBNToken = sT.nextToken();
            String bookAuthorToken = sT.nextToken();
            String bookDescToken = sT.nextToken();
            String bookPriceToken = sT.nextToken();
            boolean bookBooleanToken = Boolean.parseBoolean(sT.nextToken());
            /*
             * Now, its time to update the UI
             * */

            bookID.setText(bookIDToken);
            bookTitle.setText(bookTitleToken);
            bookISBN.setText(bookISBNToken);
            bookAuthor.setText(bookAuthorToken);
            bookDescription.setText(bookDescToken);
            double newPrice;
            if (bookBooleanToken){
                newPrice = Double.parseDouble(bookPriceToken) + 100;
            }else {
                newPrice = Double.parseDouble(bookPriceToken) + 5;
            }
            bookPrice.setText(String.valueOf(newPrice));

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BookKeeping","onStart");
        // getting shared preferences data
        SharedPreferences myData = getSharedPreferences("f1",0);
        bookID.setText(myData.getString("savedBookID",""));
        bookTitle.setText(myData.getString("savedBookTitle",""));
        bookISBN.setText(myData.getString("savedBookISBN",""));
        bookAuthor.setText(myData.getString("savedBookAuthor",""));
        bookDescription.setText(myData.getString("savedBookDescription",""));
        bookPrice.setText(myData.getString("savedBookPrice",""));
    }

    // to display toast message that the book has been added in
    public void addFields(){
        String id = bookID.getText().toString();
        String title = bookTitle.getText().toString();
        String isbn = bookISBN.getText().toString();
        String author = bookAuthor.getText().toString();
        String description = bookDescription.getText().toString();
        String price = bookPrice.getText().toString();

        if (!id.equals("") && !title.equals("") && !isbn.equals("") && !author.equals("") && !description.equals("") && !price.equals("")){

            // toast message that shows the book info has been inputted
            View view = findViewById(R.id.floatingActionButton);
            Snackbar.make(view,"Book Added", Snackbar.LENGTH_LONG).setAction("Undo", undoListener).show();

            Book newBook = new Book(title, isbn, author, description, price);
//            myList.add(newBook);
            mBookViewModel.insert(newBook);
            myRef.push().setValue(newBook);
            adapter.notifyDataSetChanged();

            // shared preferences to save data to the system
            SharedPreferences myData = getSharedPreferences("f1",0);
            SharedPreferences.Editor myEditor = myData.edit();
            myEditor.putString("savedBookID", id);
            myEditor.putString("savedBookTitle" , title);
            myEditor.putString("savedBookISBN", isbn);
            myEditor.putString("savedBookAuthor", author);
            myEditor.putString("savedBookDescription", description);
            myEditor.putString("savedBookPrice", price);
            myEditor.commit();
        } else{
            Toast message = Toast.makeText(this, "Incomplete fields, please fill in all the fields!", Toast.LENGTH_SHORT);
            message.show();
        }
    }

    // To clear all the fields in the application and displays a toast message that
    public void clearFields(){
        bookID.getText().clear();
        bookTitle.getText().clear();
        bookISBN.getText().clear();
        bookAuthor.getText().clear();
        bookDescription.getText().clear();
        bookPrice.getText().clear();
        // toast message to show that the fields are cleared
        Toast message = Toast.makeText(this, "Fields have been cleared", Toast.LENGTH_SHORT);
        message.show();

    }

    // loads the fields of the previously added book fields
    public void loadFields(){
        SharedPreferences myData = getSharedPreferences("f1",0);
        bookID.setText(myData.getString("savedBookID",""));
        bookTitle.setText(myData.getString("savedBookTitle",""));
        bookISBN.setText(myData.getString("savedBookISBN",""));
        bookAuthor.setText(myData.getString("savedBookAuthor",""));
        bookDescription.setText(myData.getString("savedBookDescription",""));
        bookPrice.setText(myData.getString("savedBookPrice",""));
    }

    // To double to value of the price input
    public void doubleThePrice(){
        if (!bookPrice.getText().toString().equals("")) {
            double price = Double.parseDouble(bookPrice.getText().toString());
            double new_price = 2 * price;
            String new_price_str = Double.toString(new_price);
            bookPrice.setText(new_price_str);
        } else {
            Toast warning = Toast.makeText(this, "The price field has not been inputted!", Toast.LENGTH_SHORT);
            warning.show();
        }
    }

    public void addPriceValue(int additionalPrice){
        if (!bookPrice.getText().toString().equals("")) {
            double price = Double.parseDouble(bookPrice.getText().toString());
            double new_price = price + additionalPrice;
            String new_price_str = Double.toString(new_price);
            bookPrice.setText(new_price_str);
        } else {
            Toast warning = Toast.makeText(this, "The price field has not been inputted!", Toast.LENGTH_SHORT);
            warning.show();
        }
    }

    public void setCapitalAuthor(){
        if (!bookAuthor.getText().toString().equals("")) {
            String author = bookAuthor.getText().toString();
            String res = author.substring(0, 1).toUpperCase() + author.substring(1);
            bookAuthor.setText(res);
        } else {
            Toast warning = Toast.makeText(this, "The author field has not been inputted!", Toast.LENGTH_SHORT);
            warning.show();
        }
    }

    public void setISBN(){
        SharedPreferences myData = getSharedPreferences("f1",0);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.putString("savedBookISBN","00112233");
        myEditor.commit();
    }

    // To save title and ISBN data when changing orientation
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("BookKeeping","onSaveInstanceState");

        outState.putString("Title",bookTitle.getText().toString());
        outState.putString("ISBN",bookISBN.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        Log.i("BookKeeping","onRestoreInstanceState");

        bookTitle.setText(savedInstanceState.getString("Title"));
        bookISBN.setText(savedInstanceState.getString("ISBN"));
        bookID.getText().clear();
        bookAuthor.getText().clear();
        bookDescription.getText().clear();
        bookPrice.getText().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("BookKeeping","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("BookKeeping","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BookKeeping","onStop");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BookKeeping","onResume");
    }

    public void deleteAll(){
        mBookViewModel.deleteAll();
        myRef.removeValue();
        adapter.notifyDataSetChanged();
    }

    public void deleteLast(){
        mBookViewModel.deleteLast();
        myRef.removeValue();
        adapter.notifyDataSetChanged();
    }

    public void deleteUnknown(){
        mBookViewModel.deleteUnknown();
        adapter.notifyDataSetChanged();
    }

    public void showList_usingGson()
    {
        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
    }
}

