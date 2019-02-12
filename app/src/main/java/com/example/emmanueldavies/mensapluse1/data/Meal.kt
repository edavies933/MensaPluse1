package com.example.emmanueldavies.mensapluse1.data

import android.arch.persistence.room.Entity
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
    var prices: Prices?,

    @SerializedName("notes")
    @Expose
    var notes: List<String>? = null,

    @PrimaryKey(autoGenerate = true)
    private var meal_id: Long = 0,

    var date: Date
)



