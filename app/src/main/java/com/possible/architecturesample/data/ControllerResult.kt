package com.possible.architecturesample.data

import java.net.UnknownHostException

class ControllerResult<T> {
    var exception: Throwable? = null
    lateinit var result: T

    constructor(result: T) {
        this.result = result
    }

    constructor(exception: Throwable, result: T) {
        this.exception = exception
        this.result = result
    }

    val isNetworkError: Boolean
        get() = exception != null && exception is UnknownHostException
}
