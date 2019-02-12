package com.example.emmanueldavies.newMensaplus.resipotory

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.text.method.TextKeyListener.clear
import com.example.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.extension.maybe
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class RemoteDataSource(var apiInterface: MensaAPIInterface) :
    IDataSource<List<Canteen>> {

    private val mCanteensApiData =
        MutableLiveData<List<Canteen>>()
    private val mMealsApiData =
        MutableLiveData<List<Meal>>()



    val meals: LiveData<List<Meal>>
        get() = mMealsApiData

    //use interface segregation here
    fun getCanteenDataWithCoordinates(locationData: LocationData) : Maybe<List<Canteen>>{
       return EnqueueCanteenWithLocationData(locationData)
    }

    @SuppressLint("CheckResult")
    private fun EnqueueCanteenWithLocationData(locationData: LocationData) : Maybe<List<Canteen>>{
        //    @GET("canteens?near[lat]=52.393535&near[lng]=13.127814")
        var cache = MensaCahe()
        return Maybe.concat(cache.getPageOfImagePosts(1).subscribeOn(Schedulers.io()),
            apiInterface.getCanteens(locationData.Latitude,locationData.Longitude)
                .doOnSuccess{
                    cache.update(1,it)
                }.subscribeOn(Schedulers.io()))
                .firstElement()
    }


    fun  queryForMealsByCanteenId (canteenId : Int, date: String) : Maybe<List<Meal>> {

     return   apiInterface.getMeals(canteenId,date).subscribeOn(Schedulers.io())

    }

    class MensaCahe {
        private  var cachedMensaPost = HashMap<Int,List<Canteen>>()
        private  fun  Clear (){
            cachedMensaPost.clear()
        }

        fun getPageOfImagePosts(page : Int): Maybe<List<Canteen>> {

            if (cachedMensaPost.keys.contains(page)) {
                return maybe(cachedMensaPost[page])
            }
            return Maybe.empty()
        }

        fun update(page: Int,data: List<Canteen>) {
            cachedMensaPost[page] = data
        }
    }
}





















//    fun queryForMealsByCanteenId(canteenId: Int): MutableLiveData<List<Meal>> {
//        apiInterface.getMeals(1, "2019-1-14").enqueue(object :
//            Callback<List<Meal>> {
//            override fun onResponse(call: Call<List<Meal>>, response: Response<List<Meal>>) {
//                mMealsApiData.setValue(response.body())
//            }
//
//            override fun onFailure(call: Call<List<Meal>>, t: Throwable) {
//
//            }
//        })
//
//        return mMealsApiData
//    }

