package com.emmanueldavies.mensapluse1.internal.di.modules

import com.emmanueldavies.mensapluse1.presentation.ui.MenuListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeRepoFragment(): MenuListFragment

}