package com.possible.architecturesample.data.network.responses;

import com.google.gson.annotations.SerializedName;

public class BookResponse {
    public String title;
    @SerializedName("imageURL")
    public String imageUrl;
    public String author;
}
