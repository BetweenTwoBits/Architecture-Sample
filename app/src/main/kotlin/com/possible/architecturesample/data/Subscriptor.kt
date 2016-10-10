package com.possible.architecturesample.data

import com.possible.architecturesample.data.controllers.BaseController

interface Subscriptor {
    fun getSubscriptorTag(): String

    fun addSubscriptedController(controller: BaseController)
}
