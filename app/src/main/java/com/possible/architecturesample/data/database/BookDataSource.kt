package com.possible.architecturesample.data.database

import android.util.Log
import com.possible.architecturesample.data.models.Book
import com.possible.architecturesample.data.models.BookDao
import com.possible.architecturesample.data.models.DaoSession
import java.util.ArrayList

class BookDataSource(private val daoSession: DaoSession) {
    private val bookDao: BookDao

    init {
        bookDao = daoSession.bookDao
    }

    val books: List<Book>
        get() {
            val books = ArrayList<Book>()

            books.addAll(bookDao.loadAll())

            return books
        }

    fun saveBooks(books: List<Book>) {
        try {
            daoSession.callInTx {
                removeBooks()
                insertBooks(books)
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unable to update table", e)
        }

    }

    private fun removeBooks() {
        daoSession.bookDao.deleteAll()
    }

    private fun insertBooks(books: List<Book>) {
        bookDao.insertInTx(books)
    }

    companion object {
        private val TAG = BookDataSource::class.java.simpleName
    }
}
