package com.example.emmanueldavies.newMensaplus.resipotory

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.example.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource(var apiInterface: MensaAPIInterface) :
    IDataSource<List<Canteen>> {

    private val mCanteensApiData =
        MutableLiveData<List<Canteen>>()
    private val mMealsApiData =
        MutableLiveData<List<Meal>>()

    override val canteenData: LiveData<List<Canteen>>
        get() = mCanteensApiData

    val meals: LiveData<List<Meal>>
        get() = mMealsApiData


    //use interface segregation here
    fun getCanteenDataWithCoordinates(locationData: LocationData) {
        EnqueueCanteenWithLocationData(locationData)
    }

    private fun EnqueueCanteenWithLocationData(locationData: LocationData) {
        //    @GET("canteens?near[lat]=52.393535&near[lng]=13.127814")

        apiInterface.getCanteens(locationData.latitude, locationData.longitude).enqueue(object :
            Callback<List<Canteen>> {
            override fun onResponse(call: Call<List<Canteen>>, response: Response<List<Canteen>>) {
                mCanteensApiData.setValue(response.body())
            }

            override fun onFailure(call: Call<List<Canteen>>, t: Throwable) {

            }
        })
    }



    fun queryForMealsByCanteenId(canteenId: Int): MutableLiveData<List<Meal>> {
        apiInterface.getMeals(1, "2019-1-14").enqueue(object :
            Callback<List<Meal>> {
            override fun onResponse(call: Call<List<Meal>>, response: Response<List<Meal>>) {
                mMealsApiData.setValue(response.body())
            }

            override fun onFailure(call: Call<List<Meal>>, t: Throwable) {

            }
        })

        return mMealsApiData
    }


}