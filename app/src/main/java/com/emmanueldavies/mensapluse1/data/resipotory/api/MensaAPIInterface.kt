package com.emmanueldavies.mensapluse1.data.resipotory.api

import com.emmanueldavies.mensapluse1.domain.model.Canteen
import com.emmanueldavies.mensapluse1.domain.model.Meal
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

 interface MensaAPIInterface {

    @GET("canteens")
    fun getCanteens(@Query("near[lat]") latitude: Double, @Query("near[lng]") longitude: Double, @Query("near[dist]") distance: Int): Maybe<List<Canteen>>

    //    http://openmensa.org/api/v2/canteens/1/days/2018-12-05/meals

    @GET("canteens/{canteenId}/days/{date}/meals")
    fun getMeals(@Path("canteenId") canteenId: Int, @Path("date") date: String): Maybe<List<Meal>>

}


// http://openmensa.org/api/v2/canteens?near[lat]=50.9787&near[lng]=11.03283

