package com.example.emmanueldavies.mensapluse1.data
import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "meal_table")
data class Meal(
    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = false)
    var id: Int?,
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("category")
    @Expose
    var category: String?,
    var canteenId: Int,

    @SerializedName("prices")
    @Expose
    var prices: Prices?,

    @SerializedName("notes")
    @Expose
    var notes: List<String>?,

    var date: String

){

    constructor() : this(0,"","",0,null,null,"")

}



