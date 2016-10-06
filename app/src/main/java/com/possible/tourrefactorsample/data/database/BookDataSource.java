package com.possible.tourrefactorsample.data.database;

import android.util.Log;

import com.possible.tourrefactorsample.data.models.Book;
import com.possible.tourrefactorsample.data.models.BookDao;
import com.possible.tourrefactorsample.data.models.DaoSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class BookDataSource {

    private static final String TAG = BookDataSource.class.getSimpleName();

    private DaoSession daoSession;
    private BookDao bookDao;

    public BookDataSource(DaoSession session) {
        daoSession = session;
        bookDao = daoSession.getBookDao();
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();

        books.addAll(bookDao.loadAll());

        return books;
    }

    public void saveBooks(final List<Book> books) {
        try {
            daoSession.callInTx(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    removeBooks();
                    insertBooks(books);
                    return null;
                }
            });
        } catch(Exception e) {
            Log.e(TAG, "Unable to update table", e);
        }
    }

    private void removeBooks() {
        daoSession.getBookDao().deleteAll();
    }

    private void insertBooks(List<Book> books) {
        bookDao.insertInTx(books);
    }
}
