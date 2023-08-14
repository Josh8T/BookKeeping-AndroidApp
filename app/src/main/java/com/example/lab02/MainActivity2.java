package com.example.lab02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.lab02.provider.Book;
import com.example.lab02.provider.BookViewModel;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
//
//    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//    RecycleAdapter adapter;
//
//    ArrayList<Book> data;
//    private BookViewModel mBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        recyclerView =  findViewById(R.id.rv2);
//
//        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
//        recyclerView.setLayoutManager(layoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager
//
//
//        adapter = new RecycleAdapter();
//        recyclerView.setAdapter(adapter);
//
//        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
//        mBookViewModel.getAllBooks().observe(this, newData -> {
//            adapter.setData(newData);
//            adapter.notifyDataSetChanged();
//        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frame2,new RecyclerViewFragment()).commit();
    }
}