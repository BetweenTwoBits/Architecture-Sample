package com.possible.tourrefactorsample;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface TourService {
    @GET("books.json")
    Observable<List<BookModel>> getBooks();
}
