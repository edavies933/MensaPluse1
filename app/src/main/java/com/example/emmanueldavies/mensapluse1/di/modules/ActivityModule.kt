package com.example.emmanueldavies.mensapluse1.di.modules

import com.example.emmanueldavies.mensapluse1.di.FragmentBuildersModule
import com.example.emmanueldavies.mensapluse1.ui.AboutActivity
import com.example.emmanueldavies.mensapluse1.ui.MainActivity
import com.example.emmanueldavies.mensapluse1.ui.mapView.MapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeMapActivity(): MapActivity


    @ContributesAndroidInjector
    abstract fun contributeAboutActivity(): AboutActivity
}
