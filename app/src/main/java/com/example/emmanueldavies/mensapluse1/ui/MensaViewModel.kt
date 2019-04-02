package com.example.emmanueldavies.mensapluse1.ui

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.example.emmanueldavies.mensapluse1.R
import com.example.emmanueldavies.mensapluse1.Utility.ICityNameGeoCoder
import com.example.emmanueldavies.mensapluse1.Utility.INetworkManager
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.domain.interactor.LoadCanteenUseCase
import com.example.emmanueldavies.mensapluse1.domain.interactor.LoadMealUseCase
import com.example.emmanueldavies.mensapluse1.domain.model.MenuAtDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.spacenoodles.makingyourappreactive.viewModel.state.ViewState
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MensaViewModel  @Inject constructor(
    private var geoCoder: ICityNameGeoCoder,
    private var netWorkManager: INetworkManager,
    private var loadCanteenUseCase: LoadCanteenUseCase,
    private var loadMealUseCase: LoadMealUseCase, application: Application

) :
    AndroidViewModel(application) {
    private var disposables: CompositeDisposable = CompositeDisposable()
    private var hasInternet = false
    var canteenNames: MutableLiveData<MutableList<String>> = MutableLiveData()
    var canteens: MutableLiveData<List<Canteen>> = MutableLiveData()
    var state: MutableLiveData<ViewState> = MutableLiveData()
    var currentTabNumber : Int = 0
    var currentCanteen: Int  = 0


    var mealAdapter: MealAdapter =
        MealAdapter(mutableListOf())
    private lateinit var canteen: Canteen
    private var calendar: Calendar = Calendar.getInstance()
    private var context: Application = application


    fun getCanteenNames(locationData: LocationData) {
        addSub(

            geoCoder.convertLatLonToCityName(
                locationData.Latitude,
                locationData.Longitude
            )?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({ cityName ->

                    locationData.cityName = cityName
                    loadCanteenUseCase.execute(CanteenObserver(), locationData)
                },
                    {
                        state.postValue(ViewState.noLocationFound())

                    })

        )

    }


    fun getMeals(canteenName: String) {
        mealAdapter.clearAdapter()
        canteen = this.canteens.value!!.first { canteen -> canteen.name.equals(canteenName) }
        getMealAtThisDay(canteen, getFormattedApiDate(0))
    }


    private fun formatMeals(it: List<Meal>) {
        mealAdapter.clearAdapter()
        mealAdapter.listOfMeals = it.toMutableList()
        mealAdapter.notifyDataSetChanged()

    }

    private fun getMealAtThisDay(canteen: Canteen, date: String) {

        addSub(
            netWorkManager.hasInternetConnection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ internet ->

                    if (internet) {
                        loadMealUseCase.execute(MealObserver(), MenuAtDate(canteen.id!!, date, hasInternet))

                    } else {
                        loadMealUseCase.execute(MealObserver(), MenuAtDate(canteen.id!!, date))
                    }
                    hasInternet = internet
                },
                    {
                        state.postValue(ViewState.error(it))
                    })
        )

    }


    fun getMealAtACertainDateInFuture(daysLater: String) {
        mealAdapter.notifyDataSetChanged()
        if (canteen != null) {
            getMealAtThisDay(canteen, daysLater)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedTitleDate(daysLater: Int): String {
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, daysLater)
        var sdf = SimpleDateFormat(context.getString(R.string.title_date_format))
        return sdf.format(calendar.time)
    }


    @SuppressLint("SimpleDateFormat")
    fun getFormattedApiDate(daysLater: Int): String {
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, daysLater)
        var sdf = SimpleDateFormat(context.getString(R.string.api_formatted_date))
        return sdf.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedDayName(daysLater: Int): String {
        when (daysLater) {
            0 -> return context.getString(R.string.todays_menu_title)
            1 -> return context.getString(R.string.tomorrow_menu_title)
        }
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, daysLater)
        var sdf = SimpleDateFormat(context.getString(R.string.date_format))
        return context.getString(R.string.menu_on) + sdf.format(calendar.time)

    }

    private inner class CanteenObserver : DisposableSingleObserver<List<Canteen>>() {

        override fun onSuccess(t: List<Canteen>) {
            state.postValue(ViewState.loading())
            canteens.postValue(t.toMutableList())
            var names = mutableListOf<String>()
            if (t.count()-1 < currentCanteen){
                currentCanteen = 0
            }
            for (canteen in t) {
                names.add(canteen.name!!)
            }
            canteenNames.postValue(names)
        }

        override fun onError(e: Throwable) {
            state.postValue(ViewState.noInternet())
        }
    }

    private inner class MealObserver : DisposableSingleObserver<List<Meal>>() {

        override fun onSuccess(meals: List<Meal>) {
            formatMeals(meals)
            state.postValue(ViewState.success())

            if (!hasInternet) {
                state.postValue(ViewState.noInternet())
            }
        }

        override fun onError(e: Throwable) {
            mealAdapter.clearAdapter()
            addSub(
                netWorkManager.hasInternetConnection()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ hasInternet ->

                        if (hasInternet) {

                            state.postValue(ViewState.noDataFound())
                        } else {
                            state.postValue(ViewState.noInternet())

                        }
                    },
                        {
                            state.postValue(ViewState.error(it))
                        })
            )
        }
    }


    @Synchronized
    private fun addSub(disposable: Disposable?) {
        if (disposable == null) return
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) disposables.dispose()
    }


}




