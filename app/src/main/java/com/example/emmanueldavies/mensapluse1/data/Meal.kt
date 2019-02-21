package com.example.emmanueldavies.mensapluse1.data
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "meal_table")
data class Meal(
    @SerializedName("id")
    @Expose
    var id: Int?,
    @SerializedName("name")
    @Expose
    var name: String? ,
    @SerializedName("category")
    @Expose
    var category: String? ,
    var canteenId: Int,

    @SerializedName("prices")
    @Expose
    @Ignore
    var prices: Prices?,

    @PrimaryKey(autoGenerate = true)
    var mealPrimaryKey: Int? ,

    @SerializedName("notes")
    @Expose
    @Ignore
    var notes: List<String>? ,

    var date: String

){

    constructor() : this(0,"","",0,null,null,null,"")

}



