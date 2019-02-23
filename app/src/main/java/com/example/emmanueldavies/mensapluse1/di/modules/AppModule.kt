package com.example.emmanueldavies.mensapluse1.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.example.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.example.emmanueldavies.mensapluse1.rooom.MealDao
import com.example.emmanueldavies.mensapluse1.rooom.MealDatabase
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
    internal fun providesLocalDataSource(mealDao: MealDao): ILocalDataSource {

        return LocalDataSource(mealDao)
    }

    @Provides
    @Reusable
    internal fun provideRemoteDataSource(apiInterface: MensaAPIInterface): IRemoteDataSource {

        return RemoteDataSource(apiInterface)
    }


    @Provides
    @Singleton
    fun providesMealDataBase(application: Application):MealDatabase{

        var mealDataBase = Room.databaseBuilder(application,MealDatabase::class.java,
            "meal_database.db").build()
        return  mealDataBase
    }

    @Provides
    @Singleton
    fun providesMealDao (mealDatabase: MealDatabase) : MealDao {

        return  mealDatabase.mealDatabaseDao()
    }


//    @Provides
//    @Singleton
//    fun providesCanteenDataBase(application: Application):CanteenDatabase{
//
//        var canteenDatabase = Room.databaseBuilder(application,CanteenDatabase::class.java,
//            "canteen_database.db").build()
//        return  canteenDatabase
//    }
//
//    @Provides
//    @Singleton
//    fun providesCanteenDao (canteenDatabase: CanteenDatabase) : CanteenDao {
//
//        return  canteenDatabase.canteenDatabaseDao()
//    }



}


