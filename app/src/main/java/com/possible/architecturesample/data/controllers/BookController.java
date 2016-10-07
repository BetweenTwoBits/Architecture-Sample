package com.possible.architecturesample.data.controllers;

import android.app.Application;

import com.possible.architecturesample.data.ControllerResult;
import com.possible.architecturesample.data.Subscriptor;
import com.possible.architecturesample.data.database.BookDataSource;
import com.possible.architecturesample.data.models.Book;
import com.possible.architecturesample.data.network.ControllerCallback;
import com.possible.architecturesample.data.network.NetworkDataSource;
import com.possible.architecturesample.data.network.requests.BookRequest;
import com.possible.architecturesample.data.network.responses.BookResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class BookController extends BaseController {
    private static final String TAG = BookController.class.getSimpleName();

    private BookDataSource bookDataSource;

    public BookController(Application application, NetworkDataSource networkDataSource,
                          BookDataSource bookDataSource) {
        super(application, networkDataSource);
        this.bookDataSource = bookDataSource;
    }

    public void loadBooks(Subscriptor subscriptor, boolean forceRefresh, final BookRequest bookRequest,
                          ControllerCallback<ControllerResult<List<Book>>> callback) {

        String url = bookRequest.getUrl();

        Observable<ControllerResult<List<Book>>> observable = networkDataSource.getObservableBooks(url)
                .flatMap(new Func1<List<BookResponse>, Observable<? extends List<Book>>>() {
                    @Override
                    public Observable<? extends List<Book>> call(List<BookResponse> bookResponses) {
                        return Observable.just(getParsedData(bookResponses));
                    }
                })
                .doOnNext(new Action1<List<Book>>() {
                    @Override
                    public void call(List<Book> books) {
                        bookDataSource.saveBooks(books);
                    }
                })
                .flatMap(new Func1<List<Book>, Observable<? extends ControllerResult<List<Book>>>>() {
                    @Override
                    public Observable<? extends ControllerResult<List<Book>>> call(List<Book> books) {
                        return Observable.just(new ControllerResult<>(bookDataSource.getBooks()));
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ControllerResult<List<Book>>>>() {
                    @Override
                    public Observable<? extends ControllerResult<List<Book>>> call(Throwable e) {
                        return Observable.just(new ControllerResult<>(e, bookDataSource.getBooks()));
                    }
                });

        final String methodTag = "loadBooks";
        executeInBackground(subscriptor, methodTag, forceRefresh, observable, callback);
    }

    private List<Book> getParsedData(List<BookResponse> bookResponses) {
        List<Book> books = new ArrayList<>(bookResponses.size());
        if (bookResponses.size() > 0) {
            for (BookResponse response : bookResponses) {
                books.add(new Book(null, response.title, response.imageUrl, response.author));
            }
        }

        return books;
    }
}
