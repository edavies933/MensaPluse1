package com.example.emmanueldavies.mensapluse1.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.example.emmanueldavies.mensapluse1.CityNameGeoCoder
import com.example.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.example.emmanueldavies.mensapluse1.rooom.CanteenDao
import com.example.emmanueldavies.mensapluse1.rooom.MensaDao
import com.example.emmanueldavies.mensapluse1.rooom.MensaDatabase
import com.example.emmanueldavies.newMensaplus.resipotory.*
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

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
    internal fun getMensaRepository(localDataSource: ILocalDataSource, remoteDataSource: IRemoteDataSource
    ): MensaRepository {
        return MensaRepository(remoteDataSource, localDataSource)
    }

    @Provides
    @Reusable
    internal fun providesLocalDataSource(mensaDao: MensaDao,canteenDao: CanteenDao): ILocalDataSource {

        return LocalDataSource(mensaDao,canteenDao)
    }

    @Provides
    @Reusable
    internal fun provideRemoteDataSource(apiInterface: MensaAPIInterface): IRemoteDataSource {

        return RemoteDataSource(apiInterface)
    }


    @Provides
    @Singleton
    fun providesMealDataBase(application: Application):MensaDatabase{

        var mensalDataBase = Room.databaseBuilder(application,MensaDatabase::class.java,
            "meal_database.db").build()
        return  mensalDataBase
    }

    @Provides
    @Singleton
    fun providesMealDao (mensaDatabase: MensaDatabase) : MensaDao {

        return  mensaDatabase.mealDatabaseDao()
    }

    @Provides
    @Singleton
    fun providesCanteenDao (mensaDatabase: MensaDatabase) : CanteenDao {

        return  mensaDatabase.canteenDatabaseDao()
    }

//    @Provides
//    @Singleton
//    fun providesCityGeoCoder (context: Application) : CityNameGeoCoder {
//
//        return  mensaDatabase.canteenDatabaseDao()
//    }

}


