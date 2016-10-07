package com.possible.architecturesample;

import android.app.Application;

import com.possible.architecturesample.data.models.DaoMaster;
import com.possible.architecturesample.data.models.DaoSession;
import com.possible.architecturesample.di.ApplicationComponent;
import com.possible.architecturesample.di.ApplicationModule;
import com.possible.architecturesample.di.DaggerApplicationComponent;

import org.greenrobot.greendao.database.Database;


public class BookApplication extends Application {

    protected ApplicationComponent appComponent;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeInjector();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "books.db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    protected void initializeInjector() {
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        appComponent.inject(this);
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
