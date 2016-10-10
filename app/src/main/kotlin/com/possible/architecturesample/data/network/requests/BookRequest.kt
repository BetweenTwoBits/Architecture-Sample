package com.possible.architecturesample.data.network.requests

class BookRequest : BaseRequest() {

    val url: String
        get() = createUrl("http://de-coding-test.s3.amazonaws.com/books.json", null)
}
