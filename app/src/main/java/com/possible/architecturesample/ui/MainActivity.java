package com.possible.architecturesample.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.possible.architecturesample.BookApplication;
import com.possible.architecturesample.R;
import com.possible.architecturesample.data.ControllerResult;
import com.possible.architecturesample.data.Subscriptor;
import com.possible.architecturesample.data.controllers.BaseController;
import com.possible.architecturesample.data.controllers.BookController;
import com.possible.architecturesample.data.models.Book;
import com.possible.architecturesample.data.network.ControllerCallback;
import com.possible.architecturesample.data.network.requests.BookRequest;
import com.possible.architecturesample.di.ActivityComponent;
import com.possible.architecturesample.di.ActivityModule;
import com.possible.architecturesample.di.DaggerActivityComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements Subscriptor {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject BookController bookController;

    private ActivityComponent activityComponent;

    private List<BaseController> subscriptedControllers = new ArrayList<>();
    private BookAdapter adapter;

    private RecyclerView recyclerView;
    private TextView errorText;
    private ProgressBar progressBar;

    private boolean destroyedBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        errorText = (TextView) findViewById(R.id.error_text);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        adapter = new BookAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        bookController.loadBooks(this, true, new BookRequest(), new ControllerCallback<ControllerResult<List<Book>>>() {
            @Override
            public void onControllerNext(ControllerResult<List<Book>> result) {
                onBooksReceived(result.getResult(), result.isNetworkError());
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        destroyedBySystem = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        destroyedBySystem = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribeAllControllers();
    }

    @Override
    public String getSubscriptorTag() {
        return TAG;
    }

    private void unsubscribeAllControllers() {
        String tag = getSubscriptorTag();
        for (BaseController controller : subscriptedControllers) {
            controller.unsubscribe(tag, !destroyedBySystem);
        }
    }

    @Override
    public void addSubscriptedController(BaseController controller) {
        if (!subscriptedControllers.contains(controller)) {
            subscriptedControllers.add(controller);
        }
    }

    private void onBooksReceived(List<Book> result, boolean networkError) {
        if (networkError) {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            errorText.setVisibility(View.GONE);
            adapter.setBookList(result);
        }
    }

    private void injectDependencies() {
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((BookApplication) getApplication()).getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
        inject(activityComponent);
    }

    private void inject(ActivityComponent component) {
        component.inject(this);
    }

    private void copyDb() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.possible.architecturesample//databases//books.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, "books.db");
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
