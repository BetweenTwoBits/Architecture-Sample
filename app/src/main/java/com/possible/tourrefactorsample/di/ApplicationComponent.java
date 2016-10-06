package com.possible.tourrefactorsample.di;

import android.app.Application;

import com.possible.tourrefactorsample.App;
import com.possible.tourrefactorsample.data.controllers.BookController;
import com.possible.tourrefactorsample.data.database.BookDataSource;
import com.possible.tourrefactorsample.data.network.NetworkDataSource;
import com.possible.tourrefactorsample.data.network.requests.BaseRequest;
import com.possible.tourrefactorsample.data.network.requests.BookRequest;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(App application);

    void inject(BaseRequest request);

    void inject(BookRequest request);

    Application application();

    Retrofit retrofit();

    NetworkDataSource networkDataSource();

    BookDataSource bookDataSource();

    BookController bookController();

}
