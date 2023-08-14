package com.example.lab02.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao mBookDao;
    private LiveData<List<Book>> mAllBooks;

    BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getAllBook();
    }
    LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }
    void insert(Book book) {
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(book));
    }

    void deleteAll(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteAllBooks();
        });
    }

    void deleteLast(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteLastBook();
        });
    }

    void deleteUnknown(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteUnknownAuthor();
        });
    }
}
