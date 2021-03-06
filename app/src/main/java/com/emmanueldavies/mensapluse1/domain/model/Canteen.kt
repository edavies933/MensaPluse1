package com.emmanueldavies.mensapluse1.domain.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "canteen_table")
class Canteen(

    @PrimaryKey(autoGenerate = true)
    var canteenPrimaryKey: Int?,
    @SerializedName("id")
    @Expose
    var id: Int?,
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("city")
    @Expose
    var city: String?,
    @SerializedName("address")
    @Expose
    @Ignore
    var address: String?,
    @SerializedName("coordinates")
    @Expose
    var coordinates: List<Double>?

){
    constructor() : this(null,0,"","","",null)
}