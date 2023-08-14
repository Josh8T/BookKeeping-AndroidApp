package com.example.lab02.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {

    @Query("select * from books")
    LiveData<List<Book>> getAllBook();

    @Query("select * from books where bookTitle=:name")
    List<Book> getBook(String name);

    @Insert
    void addBook(Book book);

    @Query("delete from books where bookTitle= :name")
    void deleteBook(String name);

    @Query("delete FROM books")
    void deleteAllBooks();

    @Query("delete FROM books WHERE bookId=(SELECT MAX(bookId) FROM books)")
    void deleteLastBook();

    @Query("delete FROM books WHERE bookAuthor='unknown'")
    void deleteUnknownAuthor();


}
