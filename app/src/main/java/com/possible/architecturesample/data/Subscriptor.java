package com.possible.architecturesample.data;

import com.possible.architecturesample.data.controllers.BaseController;

public interface Subscriptor {
    String getSubscriptorTag();

    void addSubscriptedController(BaseController controller);
}
