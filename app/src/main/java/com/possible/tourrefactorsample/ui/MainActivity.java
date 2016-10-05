package com.possible.tourrefactorsample.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.possible.tourrefactorsample.App;
import com.possible.tourrefactorsample.R;
import com.possible.tourrefactorsample.data.models.Book;
import com.possible.tourrefactorsample.data.models.BookDao;
import com.possible.tourrefactorsample.data.models.DaoSession;
import com.possible.tourrefactorsample.data.network.responses.BookResponse;
import com.possible.tourrefactorsample.data.services.TourService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://de-coding-test.s3.amazonaws.com/books.json";
    private static final String TAG = MainActivity.class.getSimpleName();

    private Subscriber<List<BookResponse>> bookSubscriber;
    private BookDao bookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final TourAdapter adapter = new TourAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        DaoSession session = ((App) getApplication()).getDaoSession();
        bookDao = session.getBookDao();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fakeurl.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TourService tourService = retrofit.create(TourService.class);

        bookSubscriber = new Subscriber<List<BookResponse>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onComplete called");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error fetching file:", t);
            }

            @Override
            public void onNext(List<BookResponse> books) {
                for (BookResponse bookResponse : books) {
                    Book book = new Book();
                    book.setTitle(bookResponse.title);
                    book.setImageUrl(bookResponse.imageUrl);
                    book.setAuthor(bookResponse.author);
                    bookDao.insert(book);
                }

                adapter.setBookList(books);
            }
        };

        tourService.getBooks(BASE_URL)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main_menu, menu);

        invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_database) {
            copyDb();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void copyDb() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.possible.tourrefactorsample//databases//tour.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, "tour.db");
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(this, "DB Exported to " + backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "DB Not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Can't write to SD card", Toast.LENGTH_SHORT).show();
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "FileNotFound: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "IOException: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
