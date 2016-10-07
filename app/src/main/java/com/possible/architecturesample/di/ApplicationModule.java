package com.possible.architecturesample.di;

import android.app.Application;

import com.possible.architecturesample.BookApplication;
import com.possible.architecturesample.data.controllers.BookController;
import com.possible.architecturesample.data.database.BookDataSource;
import com.possible.architecturesample.data.models.DaoSession;
import com.possible.architecturesample.data.network.NetworkDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    protected final BookApplication application;

    public ApplicationModule(BookApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://fakeurl.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    NetworkDataSource providesNetworkDataSource(Retrofit retrofit) {
        return retrofit.create(NetworkDataSource.class);
    }

    @Provides
    @Singleton
    DaoSession providesDaoSession() {
        return application.getDaoSession();
    }

    @Provides
    @Singleton
    BookDataSource providesBookDataSource(DaoSession daoSession) {
        return new BookDataSource(daoSession);
    }

    @Provides
    @Singleton
    BookController providesBookController(Application application,
                                          NetworkDataSource networkDataSource,
                                          BookDataSource bookDataSource) {
        return new BookController(application, networkDataSource, bookDataSource);
    }
}
