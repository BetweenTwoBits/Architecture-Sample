package com.possible.architecturesample.data

import com.possible.architecturesample.data.controllers.BaseController

interface Subscriptor {
    val subscriptorTag: String

    fun addSubscriptedController(controller: BaseController)
}
