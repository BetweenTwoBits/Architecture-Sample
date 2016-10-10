package com.possible.architecturesample.data.network.responses

import com.google.gson.annotations.SerializedName

class BookResponse {
    var title: String? = null
    @SerializedName("imageURL")
    var imageUrl: String? = null
    var author: String? = null
}
