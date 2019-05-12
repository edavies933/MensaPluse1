package com.emmanueldavies.mensapluse1.Utility

import io.reactivex.Single

interface INetworkManager {

    fun hasInternetConnection(): Single<Boolean>
}