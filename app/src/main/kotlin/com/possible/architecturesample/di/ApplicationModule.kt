package com.possible.architecturesample.di

import android.app.Application

import com.possible.architecturesample.BookApplication
import com.possible.architecturesample.data.controllers.BookController
import com.possible.architecturesample.data.database.BookDataSource
import com.possible.architecturesample.data.models.DaoSession
import com.possible.architecturesample.data.network.NetworkDataSource

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApplicationModule(private val application: BookApplication) {

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://fakeurl.com").addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun providesNetworkDataSource(retrofit: Retrofit): NetworkDataSource {
        return retrofit.create(NetworkDataSource::class.java)
    }

    @Provides
    @Singleton
    fun providesDaoSession(): DaoSession {
        return application.daoSession
    }

    @Provides
    @Singleton
    fun providesBookDataSource(daoSession: DaoSession): BookDataSource {
        return BookDataSource(daoSession)
    }

    @Provides
    @Singleton
    fun providesBookController(application: Application,
                                        networkDataSource: NetworkDataSource,
                                        bookDataSource: BookDataSource): BookController {
        return BookController(application, networkDataSource, bookDataSource)
    }
}
