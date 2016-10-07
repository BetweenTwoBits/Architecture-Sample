package com.possible.architecturesample.di;

import android.app.Application;

import com.possible.architecturesample.BookApplication;
import com.possible.architecturesample.data.controllers.BookController;
import com.possible.architecturesample.data.database.BookDataSource;
import com.possible.architecturesample.data.network.NetworkDataSource;
import com.possible.architecturesample.data.network.requests.BaseRequest;
import com.possible.architecturesample.data.network.requests.BookRequest;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BookApplication application);

    void inject(BaseRequest request);

    void inject(BookRequest request);

    Application application();

    Retrofit retrofit();

    NetworkDataSource networkDataSource();

    BookDataSource bookDataSource();

    BookController bookController();

}
