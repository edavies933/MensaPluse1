package com.emmanueldavies.mensapluse1.internal.di.component

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MensaApplication : Application(), HasActivityInjector {

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
//        DaggerAppComponent.builder().application(this).build().inject(this)

    }

}