package com.example.emmanueldavies.mensapluse1.di

import android.app.Activity
import android.app.Application
import com.example.emmanueldavies.mensapluse1.di.AppInjector
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
//        DaggerAppComponentxx.builder().application(this).build().inject(this)

    }

}