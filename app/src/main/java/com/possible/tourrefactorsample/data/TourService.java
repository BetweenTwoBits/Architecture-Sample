package com.possible.tourrefactorsample.data;

import com.possible.tourrefactorsample.data.model.BookModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface TourService {
    @GET()
    Observable<List<BookModel>> getBooks(@Url String url);
}
