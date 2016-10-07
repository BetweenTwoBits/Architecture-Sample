package com.possible.architecturesample.di;

import android.app.Activity;

import com.possible.architecturesample.ui.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);

    Activity activity();
}
