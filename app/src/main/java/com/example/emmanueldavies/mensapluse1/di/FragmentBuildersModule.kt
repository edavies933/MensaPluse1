package com.example.emmanueldavies.mensapluse1.di

import com.example.emmanueldavies.mensapluse1.ui.MenuListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeRepoFragment(): MenuListFragment

}