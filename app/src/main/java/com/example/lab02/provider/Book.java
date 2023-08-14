package com.example.lab02.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "bookId")
    private int id;
    @ColumnInfo(name = "bookTitle")
    private String title;
    @ColumnInfo(name = "bookIsbn")
    private String isbn;
    @ColumnInfo(name = "bookAuthor")
    private String author;
    @ColumnInfo(name = "bookDesc")
    private String description;
    @ColumnInfo(name = "bookPrice")
    private String price;

    public Book(String title, String isbn, String author, String description, String price) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
