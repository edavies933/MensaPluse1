package com.emmanueldavies.mensapluse1.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.emmanueldavies.mensapluse1.Utility.MensaAppViewModelFactory
import com.emmanueldavies.mensapluse1.ui.MensaViewModel
import com.emmanueldavies.mensapluse1.ui.mapView.MapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MensaViewModel::class)
    abstract fun bindMealViewModel(mealViewModel: MensaViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: MensaAppViewModelFactory): ViewModelProvider.Factory
}

