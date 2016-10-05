package com.possible.tourrefactorsample.data.network;

import com.possible.tourrefactorsample.data.network.responses.BookResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface NetworkDataSource {
    @GET()
    Observable<List<BookResponse>> getObservableBooks(@Url String url);
}
