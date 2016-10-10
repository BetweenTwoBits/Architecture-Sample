package com.possible.architecturesample.di

import android.app.Activity

import com.possible.architecturesample.ui.MainActivity

import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(activity: MainActivity)

    fun activity(): Activity
}
