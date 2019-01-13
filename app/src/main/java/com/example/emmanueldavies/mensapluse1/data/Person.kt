package com.example.emmanueldavies.mensapluse1.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Person ( val name: String, val age : Int)

class Meal {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("prices")
    @Expose
    var prices: Prices? = null
    @SerializedName("notes")
    @Expose
    var notes: List<String>? = null

}

class Prices {

    @SerializedName("students")
    @Expose
    var students: Double? = null
    @SerializedName("employees")
    @Expose
    var employees: Double? = null
    @SerializedName("pupils")
    @Expose
    var pupils: Any? = null
    @SerializedName("others")
    @Expose
    var others: Double? = null

}


class Canteen {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("coordinates")
    @Expose
    var coordinates: List<Double>? = null

}

class LocationData {

    var longitude: Double = 0.toDouble()
    var latitude: Double = 0.toDouble()

}