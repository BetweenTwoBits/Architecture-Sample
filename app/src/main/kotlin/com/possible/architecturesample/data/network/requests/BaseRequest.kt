package com.possible.architecturesample.data.network.requests

import java.util.HashMap

open class BaseRequest {

    protected fun createUrl(url: String, map: HashMap<String, String>?): String {
        var newUrl = url
        if (map != null) {
            for ((key, value) in map) {
                newUrl = newUrl.replace(key, value)
            }
        }
        return newUrl
    }
}
