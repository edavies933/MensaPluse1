package com.emmanueldavies.mensapluse1.di.modules

import com.emmanueldavies.mensapluse1.di.FragmentBuildersModule
import com.emmanueldavies.mensapluse1.ui.aboutView.AboutActivity
import com.emmanueldavies.mensapluse1.ui.MensaView.MainActivity
import com.emmanueldavies.mensapluse1.ui.SplashActivity
import com.emmanueldavies.mensapluse1.ui.mapView.MapActivity
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

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity
}
