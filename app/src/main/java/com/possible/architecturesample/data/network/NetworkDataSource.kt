package com.possible.architecturesample.data.network

import com.possible.architecturesample.data.network.responses.BookResponse

import retrofit2.http.GET
import retrofit2.http.Url
import rx.Observable

interface NetworkDataSource {
    @GET
    fun getObservableBooks(@Url url: String): Observable<List<BookResponse>>
}
