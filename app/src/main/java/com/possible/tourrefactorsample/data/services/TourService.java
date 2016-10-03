package com.possible.tourrefactorsample.data.services;

import com.possible.tourrefactorsample.data.network.responses.BookResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface TourService {
    @GET()
    Observable<List<BookResponse>> getBooks(@Url String url);
}
