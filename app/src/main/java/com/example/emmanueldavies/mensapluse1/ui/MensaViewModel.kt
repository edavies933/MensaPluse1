package com.example.emmanueldavies.mensapluse1.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.newMensaplus.resipotory.MensaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger
import javax.inject.Inject

class MensaViewModel @Inject constructor(val repository: MensaRepository) : ViewModel() {


    var canteenNames: MutableLiveData<MutableList<String>> = MutableLiveData()
    var canteens: MutableLiveData<List<Canteen>> = MutableLiveData()
    var mealAdapter: MealAdapter =
        MealAdapter(mutableListOf())
    private lateinit var canteen: Canteen
    private var calendar: Calendar = Calendar.getInstance()

    private val log = Logger.getLogger(MensaViewModel::class.java.name)

    fun getCanteenNames(locationData: LocationData) {

        var x = repository.getCanteenDataWithCoordinates(locationData)!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                log.info("on do Success for canteen call " + it.toString())

                this.canteens.postValue(it)
            }
            .map {

                log.info("on do map for canteen call " + it.toString())

                var listOfCanteenNames: MutableList<String> = mutableListOf()

                for (item in it) {
                    listOfCanteenNames.add(item.name!!)
                }
                listOfCanteenNames
            }
            .subscribe {
                log.info("observable on subscribed for canteen name$it")
                canteenNames.postValue(it)
            }
    }

    fun getMeals(canteenName: String) {
        0
        canteen = this.canteens.value!!.single { canteen -> canteen.name.equals(canteenName) }
        log.info("selected Canteen is $canteen")
        getMealAtThisDay(canteen, calendar.time.toString())
    }


    private fun FormatMeals(it: List<Meal>) {
        mealAdapter.listOfMeals.clear()
        mealAdapter.listOfMeals = it.toMutableList()
        mealAdapter.notifyDataSetChanged()
    }

    private fun getMealAtThisDay(canteen: Canteen, date: String) {
        repository.getMealsByCanteenId(canteen?.id!!, date)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { it ->
                FormatMeals(it)
            }
            .subscribe({},
                {
                    var error = it //Todo handle this

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
            0 -> return "Today"
            1 -> return "Tomorrow"
        }
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, daysLater)
        var sdf = SimpleDateFormat("EEEE")
        return sdf.format(calendar.time)

    }
}
