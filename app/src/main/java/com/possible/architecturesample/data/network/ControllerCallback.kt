package com.possible.architecturesample.data.network

class ControllerCallback<in T> {
    fun onControllerNext(t: T?) {}

    fun onControllerComplete() {}

    fun onControllerError(exception: Exception) {}
}
