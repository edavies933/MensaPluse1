package com.emmanueldavies.mensapluse1.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Prices (

    @SerializedName("students")
    @Expose
    var students: String = "-",
    @SerializedName("employees")
    @Expose
    var employees: String =  "-",
    @SerializedName("pupils")
    @Expose
    var pupils: Any? =  "-",
    @SerializedName("others")
    @Expose
    var others: String =  "-"


)

