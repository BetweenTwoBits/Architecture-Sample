package com.possible.architecturesample.data.controllers

import android.app.Application
import com.possible.architecturesample.data.ControllerResult
import com.possible.architecturesample.data.Subscriptor
import com.possible.architecturesample.data.database.BookDataSource
import com.possible.architecturesample.data.models.Book
import com.possible.architecturesample.data.network.ControllerCallback
import com.possible.architecturesample.data.network.NetworkDataSource
import com.possible.architecturesample.data.network.requests.BookRequest
import com.possible.architecturesample.data.network.responses.BookResponse
import rx.Observable
import java.util.ArrayList

class BookController(application: Application, networkDataSource: NetworkDataSource,
                     private val bookDataSource: BookDataSource) : BaseController(application, networkDataSource) {

    fun loadBooks(subscriptor: Subscriptor, forceRefresh: Boolean, bookRequest: BookRequest,
                  callback: ControllerCallback<List<Book>>) {

        val url = bookRequest.url

        val observable = networkDataSource.getObservableBooks(url).
                flatMap { bookResponses -> Observable.just(getParsedData(bookResponses)) }.
                doOnNext { books -> bookDataSource.saveBooks(books) }.
                flatMap { Observable.just(ControllerResult(bookDataSource.books)) }.
                onErrorResumeNext { e -> Observable.just(ControllerResult(e, bookDataSource.books)) }

        val methodTag = "loadBooks"
        executeInBackground(subscriptor, methodTag, forceRefresh, observable, callback)
    }

    private fun getParsedData(bookResponses: List<BookResponse>): List<Book> {
        val books = ArrayList<Book>(bookResponses.size)
        if (bookResponses.isNotEmpty()) {
            bookResponses.mapTo(books) { Book(null, it.title, it.imageUrl, it.author) }
        }

        return books
    }

    companion object {
        private val TAG = BookController::class.java.simpleName
    }
}
