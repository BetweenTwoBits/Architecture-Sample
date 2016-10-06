package com.possible.tourrefactorsample.data;

import com.possible.tourrefactorsample.data.controllers.BaseController;

public interface Subscriptor {
    String getSubscriptorTag();

    void addSubscriptedController(BaseController controller);
}
