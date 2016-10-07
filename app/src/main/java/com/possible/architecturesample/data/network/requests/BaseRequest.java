package com.possible.architecturesample.data.network.requests;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {

    protected String createUrl(String url, HashMap<String, String> map) {
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                url = url.replace(entry.getKey(), entry.getValue());
            }
        }
        return url;
    }
}
