package com.possible.tourrefactorsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.possible.tourrefactorsample.data.model.BookModel;
import com.possible.tourrefactorsample.data.TourService;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://de-coding-test.s3.amazonaws.com/";
    private static final String TAG = MainActivity.class.getSimpleName();

    private Subscriber<List<BookModel>> bookSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final TourAdapter adapter = new TourAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TourService tourService = retrofit.create(TourService.class);

        bookSubscriber = new Subscriber<List<BookModel>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onComplete called");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error fetching file:", t);
            }

            @Override
            public void onNext(List<BookModel> bookModels) {
                adapter.setBookList(bookModels);
            }
        };

        tourService.getBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookSubscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookSubscriber != null) {
            bookSubscriber.unsubscribe();
        }
    }
}
