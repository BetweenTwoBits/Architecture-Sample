package com.possible.tourrefactorsample.di;

import android.app.Activity;

import com.possible.tourrefactorsample.ui.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);

    Activity activity();
}
