package com.possible.tourrefactorsample;

import android.app.Application;

import com.possible.tourrefactorsample.data.models.DaoMaster;
import com.possible.tourrefactorsample.data.models.DaoSession;
import com.possible.tourrefactorsample.di.ApplicationComponent;
import com.possible.tourrefactorsample.di.ApplicationModule;
import com.possible.tourrefactorsample.di.DaggerApplicationComponent;

import org.greenrobot.greendao.database.Database;


public class App extends Application {

    protected ApplicationComponent appComponent;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeInjector();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "tour.db");
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
