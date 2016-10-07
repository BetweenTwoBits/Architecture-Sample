package com.possible.architecturesample.data.network

open class ControllerCallback<in T> {
    open fun onControllerNext(result: T) {}

    open fun onControllerComplete() {}

    open fun onControllerError(exception: Exception) {}
}
