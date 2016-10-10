package com.possible.architecturesample.di

import android.app.Application
import com.possible.architecturesample.BookApplication
import com.possible.architecturesample.data.controllers.BookController
import com.possible.architecturesample.data.database.BookDataSource
import com.possible.architecturesample.data.network.NetworkDataSource
import com.possible.architecturesample.data.network.requests.BaseRequest
import com.possible.architecturesample.data.network.requests.BookRequest
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: BookApplication)

    fun inject(request: BaseRequest)

    fun inject(request: BookRequest)

    fun application(): Application

    fun retrofit(): Retrofit

    fun networkDataSource(): NetworkDataSource

    fun bookDataSource(): BookDataSource

    fun bookController(): BookController
}
