package com.possible.tourrefactorsample;

import android.app.Application;

import com.possible.tourrefactorsample.data.models.DaoMaster;
import com.possible.tourrefactorsample.data.models.DaoSession;

import org.greenrobot.greendao.database.Database;


public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "tour-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
