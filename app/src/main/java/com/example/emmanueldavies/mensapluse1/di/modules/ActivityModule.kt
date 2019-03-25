package com.example.emmanueldavies.mensapluse1.di.modules

import com.example.emmanueldavies.mensapluse1.LocaionManager.ILocationDetector
import com.example.emmanueldavies.mensapluse1.LocaionManager.LocationDetector
import com.example.emmanueldavies.mensapluse1.di.FragmentBuildersModule
import com.example.emmanueldavies.mensapluse1.rooom.CanteenDao
import com.example.emmanueldavies.mensapluse1.rooom.MensaDatabase
import com.example.emmanueldavies.mensapluse1.ui.MainActivity
import com.example.emmanueldavies.mensapluse1.ui.MapActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeMapActivity(): MapActivity

        // Add bindings for other sub-components here
}