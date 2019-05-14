package com.emmanueldavies.mensapluse1.di.modules


import android.app.Application
import android.arch.persistence.room.Room
import com.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.emmanueldavies.mensapluse1.data.resipotory.IRemoteDataSource
import com.emmanueldavies.mensapluse1.data.resipotory.IRepository
import com.emmanueldavies.mensapluse1.data.rooom.CanteenDao
import com.emmanueldavies.mensapluse1.data.rooom.MensaDao
import com.emmanueldavies.mensapluse1.data.rooom.MensaDatabase
import com.emmanueldavies.newMensaplus.resipotory.ILocalDataSource
import com.emmanueldavies.newMensaplus.resipotory.LocalDataSource
import com.emmanueldavies.newMensaplus.resipotory.MensaRepository
import com.emmanueldavies.newMensaplus.resipotory.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton


//@Module(includes = [RetrofitModule::class, ContextModule::class])
@Module
class RepositoryModule() {

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
    fun providesMealDataBase(application: Application):MensaDatabase{

        var mensalDataBase = Room.databaseBuilder(application,MensaDatabase::class.java,
            "meal_database.db").fallbackToDestructiveMigration().build()
        return  mensalDataBase
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
}