package com.possible.architecturesample.di

import android.app.Activity

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @PerActivity
    fun providesActivity(): Activity {
        return this.activity
    }
}
