package com.example.emmanueldavies.mensapluse1.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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