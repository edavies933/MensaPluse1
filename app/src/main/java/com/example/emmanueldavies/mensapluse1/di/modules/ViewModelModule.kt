package com.example.emmanueldavies.mensapluse1.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.emmanueldavies.mensapluse1.MensaAppViewModelFactory
import com.example.emmanueldavies.mensapluse1.ui.MensaViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MensaViewModel::class)
    abstract fun bindUserViewModel(userViewModel: MensaViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: MensaAppViewModelFactory): ViewModelProvider.Factory
}

