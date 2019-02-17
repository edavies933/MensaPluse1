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
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("category")
    @Expose
    var category: String? = null,
    @SerializedName("prices")
    @Expose
    @Ignore
    var prices: Prices?,

    @PrimaryKey(autoGenerate = true)
    var mealPrimaryKey: Int,

    @SerializedName("notes")
    @Expose
    @Ignore
    var notes: List<String>? = null,
    @Ignore
    var date: Date


){
    constructor():this(null,"",null,null,0,null,
        Date())
}



