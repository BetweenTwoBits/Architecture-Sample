package com.possible.tourrefactorsample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TourService {
    @GET("books.json")
    Call<List<BookModel>> getBooks();
}
