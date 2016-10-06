package com.possible.tourrefactorsample.data.network.requests;

public class BookRequest extends BaseRequest {

    public String getUrl() {
        return createUrl("http://de-coding-test.s3.amazonaws.com/books.json", null);
    }
}
