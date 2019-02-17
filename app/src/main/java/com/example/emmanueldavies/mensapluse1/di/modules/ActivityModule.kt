package com.example.emmanueldavies.mensapluse1.di.modules

import com.example.emmanueldavies.mensapluse1.di.FragmentBuildersModule
import com.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity


    // Add bindings for other sub-components here
}