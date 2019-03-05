package com.example.emmanueldavies.mensapluse1.di.component

import android.app.Application
import com.example.emmanueldavies.mensapluse1.MensaApplication
import com.example.emmanueldavies.mensapluse1.di.modules.ActivityModule
import com.example.emmanueldavies.mensapluse1.di.modules.AppModule
import com.example.emmanueldavies.mensapluse1.di.modules.ThreadModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityModule::class, AppModule::class,ThreadModule::class]
)
interface AppComponentxx {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponentxx
    }

        fun inject(app: MensaApplication)


    }

