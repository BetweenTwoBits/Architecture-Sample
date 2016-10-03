package com.possible.tourrefactorsample.data.services;

import com.possible.tourrefactorsample.data.models.BookModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface TourService {
    @GET()
    Observable<List<BookModel>> getBooks(@Url String url);
}
