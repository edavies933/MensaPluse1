package com

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
     var mealAdapter: MealAdapter = MealAdapter(mutableListOf())
    private lateinit var canteen: Canteen
    private var calendar: Calendar = Calendar.getInstance()

    val Log = Logger.getLogger(MensaViewModel::class.java.name)

    fun getCanteenNames(locationData: LocationData) {

        var x = repository.getCanteenDataWithCoordinates(locationData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Log.info("on do Success for canteen call " + it.toString())

                canteens.postValue(it)
                for (x in it) {
                    Log.info("this are the canteens " + x.toString())

                }
            }
            .map {

                Log.info("on do map for canteen call " + it.toString())

                var xx: MutableList<String> = mutableListOf()

                for (item in it) {

                    xx.add(item.name!!)
                }
                xx

            }


            .subscribe {
                Log.info("observerble on subcribed for canteen name" + it.toString())
                canteenNames.postValue(it)
            }
    }

    fun getMeals(canteenName: String) {


        var ChoosenCanteen = this.canteens.value!!.single { canteen -> canteen.name.equals(canteenName) }
        canteen = ChoosenCanteen
        Log.info("selected Canteen is " + canteen.toString())

        getMealAtThisDay(canteen, calendar.time.toString())
    }


    private fun FormatMeals(it: List<Meal>) {
//        for (meal in it) if (meal.prices?.students == null) {
//            meal.prices?.students = meal.prices?.students ?: "--"
//            meal.prices?.students.plus("€")
//
//            meal.prices?.employees = meal.prices?.employees ?: "--"
//            meal.prices?.employees.plus("€")
//
//            meal.prices?.others = meal.prices?.others ?: "--"
//            meal.prices?.others.plus("€")
//
//
//        }

        mealAdapter.listOfMeals = it.toMutableList()
        mealAdapter.notifyDataSetChanged()
    }

    fun getMealAtThisDay(canteen: Canteen, date: String) {

        repository.getMealsByCanteenId(canteen?.id!!, date)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { it ->
                var x = it
                FormatMeals(it)
                for (x in it) {
                    Log.info("on do Success for meal call " + x.toString())

                }

            }
            .subscribe({
                Log.info("on onsubscribed for meal call " + it.toString())


            }, {
                var x = it

            })
    }


    fun getMealAtAcertainDateInFuture(daysLater: String) {


        mealAdapter.notifyDataSetChanged()
//        calendar.add(Calendar.DAY_OF_MONTH,daysLater)
        var calender1: Calendar = Calendar.getInstance()
//        calender1.add(Calendar.DAY_OF_MONTH, daysLater)
        getMealAtThisDay(canteen, daysLater)
//        calender1.add(Calendar.DAY_OF_MONTH, -daysLater)
    }

    fun getFormatedTitleDate(daysLater: Int): String {
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, daysLater)
        var sdf = SimpleDateFormat("E dd-MM-yyyy")

        return sdf.format(calendar.time)
    }

    fun getFormatedDayName (daysLater: Int): String{

        when(daysLater){
            0 -> return  "Today"
            1 -> return  "Tomorrow"
        }
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, daysLater)
        var sdf = SimpleDateFormat("EEEE")
        return sdf.format(calendar.time)

    }
}
