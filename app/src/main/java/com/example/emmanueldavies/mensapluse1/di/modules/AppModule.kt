package com.example.emmanueldavies.mensapluse1.di.modules

import android.content.Context
import com.example.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.example.emmanueldavies.mensapluse1.data.Person
import com.example.emmanueldavies.newMensaplus.resipotory.LocalDataSource
import com.example.emmanueldavies.newMensaplus.resipotory.MensaRepository
import com.example.emmanueldavies.newMensaplus.resipotory.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideApplication(): Person =
        Person("emmanuel", 25)


    @Singleton
    @Provides
    fun provideGithubService(): MensaAPIInterface {
        return Retrofit.Builder()
            .baseUrl("https://openmensa.org/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

            .create(MensaAPIInterface::class.java)
    }


    @Provides
    @Reusable
    internal fun getMensaRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): MensaRepository {
        return MensaRepository(remoteDataSource, localDataSource)
    }

    @Provides
    @Reusable
    internal fun providesLocalDataSource(): LocalDataSource {

        return LocalDataSource()
    }

    @Provides
    @Reusable
    internal fun provideRemoteDataSource(apiInterface: MensaAPIInterface): RemoteDataSource {

        return RemoteDataSource(apiInterface)
    }
}


