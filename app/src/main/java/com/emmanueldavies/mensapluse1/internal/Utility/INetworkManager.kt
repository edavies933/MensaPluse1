package com.emmanueldavies.mensapluse1.internal.Utility

import io.reactivex.Single

interface INetworkManager {

    fun hasInternetConnection(): Single<Boolean>
}