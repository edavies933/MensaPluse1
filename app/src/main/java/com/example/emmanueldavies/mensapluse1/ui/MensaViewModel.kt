package com.example.emmanueldavies.mensapluse1.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.emmanueldavies.mensapluse1.CityNameGeoCoder
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.newMensaplus.resipotory.MensaRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.spacenoodles.makingyourappreactive.viewModel.state.MainActivityState
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger
import javax.inject.Inject

class MensaViewModel @Inject constructor(val repository: MensaRepository, private var geoCoder: CityNameGeoCoder) :
    ViewModel() {


    var canteenNames: MutableLiveData<MutableList<String>> = MutableLiveData()
    var canteens: MutableLiveData<List<Canteen>> = MutableLiveData()
    var state: MutableLiveData<MainActivityState> = MutableLiveData()


    var mealAdapter: MealAdapter =
        MealAdapter(mutableListOf())
    private lateinit var canteen: Canteen
    private var calendar: Calendar = Calendar.getInstance()

    private val log = Logger.getLogger(MensaViewModel::class.java.name)

    fun getCanteenNames(locationData: LocationData) {

        hasInternetConnection().doOnSuccess { internet ->

            if (internet) {
                repository.mRemoteDataSource.getCanteenDataWithCoordinates(locationData)

                    .map {
                        //
                        canteens.postValue(it)
                        var listOfCanteenNames: MutableList<String> = mutableListOf()

                        for (item in it) {
                            listOfCanteenNames.add(item.name!!)
                        }
                        repository.mLocalDataSource.saveCanteensToDataBase(it)

                        listOfCanteenNames
                    }.observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess {

                        this.canteenNames.postValue(it)
                    }.subscribe()

            } else {
                var cityName = geoCoder.convertLatLonToCityName(locationData.Latitude, locationData.Longitude)
                if (cityName != null) {
                    repository.mLocalDataSource.queryForCanteensWithCity(cityName).subscribeOn(Schedulers.io())
                        .map {
                            var listOfCanteenNames: MutableList<String> = mutableListOf()

                            if (it.count() >= 1) {
                                this.canteens.postValue(it)
                                for (item in it) {
                                    listOfCanteenNames.add(item.name!!)
                                }
                                state.postValue(MainActivityState.noInternet())

                            }
                            listOfCanteenNames

                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess {
                            this.canteenNames.postValue(it)
                            state.postValue(MainActivityState.noInternet())
                            }
                        .subscribe()
                } else {
                    state.postValue(MainActivityState.noLocationFound())
                }
            }

        }.subscribe()

//
//        mealAdapter.clearAdapter()
//        var x = repository.getCanteenDataWithCoordinates(locationData)!!.subscribeOn(Schedulers.io()).doOnSubscribe {
//
//            state.postValue(MainActivityState.loading())
//
//        }
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSuccess {
//                this.canteens.postValue(it)
//                state.postValue(MainActivityState.success())
//            }
//            .map {
//
//                var listOfCanteenNames: MutableList<String> = mutableListOf()
//
//                for (item in it) {
//                    listOfCanteenNames.add(item.name!!)
//                }
//                listOfCanteenNames
//            }
//            .subscribe({
//                canteenNames.postValue(it)
//            },
//                {
//                    state.postValue(MainActivityState.error(it))
//
//                })
    }

    fun getMeals(canteenName: String) {
        mealAdapter.clearAdapter()
        canteen = this.canteens.value!!.first { canteen -> canteen.name.equals(canteenName) }
        log.info("selected Canteen is $canteen")
        getMealAtThisDay(canteen, calendar.time.toString())
    }


    private fun FormatMeals(it: List<Meal>) {
        mealAdapter.clearAdapter()
        mealAdapter.listOfMeals = it.toMutableList()
        mealAdapter.notifyDataSetChanged()
    }

    private fun getMealAtThisDay(canteen: Canteen, date: String) {
        var x = repository.getMealsByCanteenId(canteen?.id!!, date).doOnSubscribe {

            state.postValue(MainActivityState.loading())

        }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { it ->
                FormatMeals(it)
                state.postValue(MainActivityState.success())

            }
            .subscribe({},
                {
                    state.postValue(MainActivityState.error(it))

                })
    }


    fun getMealAtACertainDateInFuture(daysLater: String) {
        mealAdapter.notifyDataSetChanged()
        getMealAtThisDay(canteen, daysLater)
    }

    fun getFormatedTitleDate(daysLater: Int): String {
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, daysLater)
        var sdf = SimpleDateFormat("E dd-MM-yyyy")
        return sdf.format(calendar.time)
    }

    fun getFormattedDayName(daysLater: Int): String {
        when (daysLater) {
            0 -> return "Today's Menu"
            1 -> return "Tomorrow's Menu"
        }
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, daysLater)
        var sdf = SimpleDateFormat("EEEE, MMM d, ''yy")
        return "Menu on " + sdf.format(calendar.time)

    }


    fun hasInternetConnection(): Single<Boolean> {
        return Single.fromCallable {
            try {
                // Connect to Google DNS to check for connection
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                false
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
