package com.example.emmanueldavies.mensapluse1.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.example.emmanueldavies.mensapluse1.LocaionManager.ILocationDetector
import com.example.emmanueldavies.mensapluse1.LocaionManager.LocationDetector
import com.example.emmanueldavies.mensapluse1.Utility.CityNameGeoCoder
import com.example.emmanueldavies.mensapluse1.Utility.ICityNameGeoCoder
import com.example.emmanueldavies.mensapluse1.Utility.INetworkManager
import com.example.emmanueldavies.mensapluse1.Utility.NetWorkManagerImpl
import com.example.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.example.emmanueldavies.mensapluse1.data.resipotory.IRemoteDataSource
import com.example.emmanueldavies.mensapluse1.data.resipotory.IRepository
import com.example.emmanueldavies.mensapluse1.rooom.CanteenDao
import com.example.emmanueldavies.mensapluse1.rooom.MensaDao
import com.example.emmanueldavies.mensapluse1.rooom.MensaDatabase
import com.example.emmanueldavies.newMensaplus.resipotory.*
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideGithubService(okHttpClient: OkHttpClient): MensaAPIInterface {
        return Retrofit.Builder()
            .baseUrl("https://openmensa.org/api/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

            .create(MensaAPIInterface::class.java)
    }


    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor () :HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    }

    @Provides
    @Singleton
    fun providesOkHttpClient (httpLoggingInterceptor: HttpLoggingInterceptor) :OkHttpClient {


        return OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
        }.build()

    }


    @Provides
    @Reusable
    internal fun getMensaRepository(localDataSource: ILocalDataSource, remoteDataSource: IRemoteDataSource
    ): IRepository {
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
            "meal_database.db").fallbackToDestructiveMigration().build()
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

    @Provides
    @Singleton
    fun  providesLocationDetector () : ILocationDetector {

        return LocationDetector()
    }

    @Provides
    @Singleton
    fun  providesINetWorkManager () : INetworkManager {

        return NetWorkManagerImpl()
    }

    @Provides
    @Singleton
    fun  providesIICityNameGeoCoder (context: Application ) : ICityNameGeoCoder {

        return CityNameGeoCoder(context)
    }

}


