package com.possible.architecturesample.data.network

import com.possible.architecturesample.data.ControllerResult

open class ControllerCallback<in T> {
    open fun onControllerNext(result: ControllerResult<out T>) {}

    open fun onControllerComplete() {}

    open fun onControllerError(exception: Exception) {}
}
