package com.emmanueldavies.mensapluse1.internal.di.modules

import android.app.Application
import com.emmanueldavies.mensapluse1.internal.Utility.LocaionManager.ILocationDetector
import com.emmanueldavies.mensapluse1.internal.Utility.LocaionManager.LocationDetector
import com.emmanueldavies.mensapluse1.internal.Utility.CityNameGeoCoder
import com.emmanueldavies.mensapluse1.internal.Utility.ICityNameGeoCoder
import com.emmanueldavies.mensapluse1.internal.Utility.INetworkManager
import com.emmanueldavies.mensapluse1.internal.Utility.NetWorkManagerImpl
import com.emmanueldavies.mensapluse1.data.resipotory.api.MensaAPIInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideMensaApiService(okHttpClient: OkHttpClient): MensaAPIInterface {
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


