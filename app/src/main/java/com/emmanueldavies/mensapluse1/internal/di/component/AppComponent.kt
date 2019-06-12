package com.emmanueldavies.mensapluse1.internal.di.component

import android.app.Application
import com.emmanueldavies.mensapluse1.internal.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityModule::class, AppModule::class, ThreadModule::class, RepositoryModule::class, ViewModelModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

        fun inject(app: MensaApplication)


    }

