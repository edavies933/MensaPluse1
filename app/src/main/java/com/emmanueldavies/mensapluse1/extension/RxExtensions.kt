package com.emmanueldavies.mensapluse1.extension

import io.reactivex.Maybe

fun <T> maybe(value: T?): Maybe<T> {
    return if(value == null) Maybe.empty()
    else Maybe.just(value)
}