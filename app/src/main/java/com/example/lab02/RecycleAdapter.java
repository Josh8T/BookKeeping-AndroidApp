package com.example.lab02;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab02.provider.Book;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
//    ArrayList<Book> data;
    List<Book> data = new ArrayList<>();
    public RecycleAdapter()
    {
    }

    public void setData(List<Book> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.ViewHolder holder, int position) {
        holder.id.setText("ID: " + String.valueOf(data.get(position).getId()));
        holder.title.setText("Title: " + data.get(position).getTitle());
        holder.author.setText("Author: " + data.get(position).getAuthor());
        holder.isbn.setText("ISBN: " + data.get(position).getIsbn());
        holder.description.setText("Desc: " + data.get(position).getDescription());
        holder.price.setText("Price: " + String.valueOf(data.get(position).getPrice()));
        holder.index.setText("No: " + String.valueOf(position + 1));

        //a class declared in a method (so called local or anonymous class can only access the method's local variables if they are declared final (1.8 or are effectively final)
        //this has to do with Java closures
        // see https://docs.oracle.com/javase/tutorial/java/javaOO/localclasses.html and https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
        final int fPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() { //set back to itemView for students
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Item at position " + fPosition + " was clicked!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView id;
        public TextView title;
        public TextView isbn;
        public TextView author;
        public TextView description;
        public TextView price;
        public TextView index;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            id = itemView.findViewById(R.id.card_id);
            title = itemView.findViewById(R.id.card_title);
            isbn = itemView.findViewById(R.id.card_isbn);
            author = itemView.findViewById(R.id.card_author);
            description = itemView.findViewById(R.id.card_description);
            price = itemView.findViewById(R.id.card_price);
            index = itemView.findViewById(R.id.card_index);
        }
    }


}
