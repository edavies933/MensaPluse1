package com.example.emmanueldavies.mensapluse1.api

import android.arch.lifecycle.LiveData
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.Meal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface MensaAPIInterface {


    @GET("canteens")
    fun getCanteens(@Query("near[lat]") latitude: Double, @Query("near[lng]") longitude: Double): Call<List<Canteen>>

    //    http://openmensa.org/api/v2/canteens/1/days/2018-12-05/meals

    @GET("canteens/{canteenId}/days/{date}/meals")
    fun getMeals(@Path("canteenId") canteenId: Int, @Path("date") date: String): Call<List<Meal>>

}



