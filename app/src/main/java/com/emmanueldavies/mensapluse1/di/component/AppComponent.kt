package com.emmanueldavies.mensapluse1.di.component

import android.app.Application
import com.emmanueldavies.mensapluse1.di.MensaApplication
import com.emmanueldavies.mensapluse1.di.modules.ActivityModule
import com.emmanueldavies.mensapluse1.di.modules.AppModule
import com.emmanueldavies.mensapluse1.di.modules.RepositoryModule
import com.emmanueldavies.mensapluse1.di.modules.ThreadModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityModule::class, AppModule::class,ThreadModule::class, RepositoryModule::class]
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

