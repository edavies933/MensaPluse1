package com.example.emmanueldavies.mensapluse1.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.emmanueldavies.mensapluse1.Utility.CityNameGeoCoder
import com.example.emmanueldavies.mensapluse1.Utility.ICityNameGeoCoder
import com.example.emmanueldavies.mensapluse1.Utility.INetworkManager
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.resipotory.IRepository
import com.example.emmanueldavies.newMensaplus.resipotory.MensaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.spacenoodles.makingyourappreactive.viewModel.state.MainActivityState
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger
import javax.inject.Inject

class MensaViewModel @Inject constructor(private val repository: IRepository, private var geoCoder: ICityNameGeoCoder, private var netWorkManager: INetworkManager) :
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
        var cityName = geoCoder.convertLatLonToCityName(locationData.Latitude, locationData.Longitude)

        if (cityName == null) {

            state.postValue(MainActivityState.noLocationFound())
        } else {
            var dispose = repository.getCanteenNames(locationData, cityName).map {
                //
                canteens.postValue(it)
                var listOfCanteenNames: MutableList<String> = mutableListOf()

                for (item in it) {
                    listOfCanteenNames.add(item.name!!)
                }

                listOfCanteenNames
            }.observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    state.postValue(MainActivityState.loading())

                    this.canteenNames.postValue(it)
                }.doOnError {

                    state.postValue(MainActivityState.noInternet())
                    var exception: HttpException = (it as HttpException)
                    var code = exception.code()
                    var x = it
                }.subscribe({},
                    {
                        var x = it
                        state.postValue(MainActivityState.noInternet())


                    })

        }

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
        state.postValue(MainActivityState.loading())

        var disposeLater = repository.getMealsByCanteenId(canteen.id!!, date)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                FormatMeals(it)
                state.postValue(MainActivityState.success())

            },
                {
                    state.postValue(MainActivityState.loading())

                    mealAdapter.clearAdapter()

                    netWorkManager.  hasInternetConnection()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ hasInternet ->

                            if (hasInternet) {

                                state.postValue(MainActivityState.noDataFound())
                            } else {
                                state.postValue(MainActivityState.noInternet())

                            }
                        },
                            {

                            })



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






}




