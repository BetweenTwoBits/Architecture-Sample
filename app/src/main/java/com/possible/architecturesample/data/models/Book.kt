package com.possible.architecturesample.data.models

import org.greenrobot.greendao.annotation.Entity
import org.greenrobot.greendao.annotation.Generated
import org.greenrobot.greendao.annotation.Id

@Entity
class Book {
    @Id(autoincrement = true)
    var id: Long? = null
    var title: String? = null
    var imageUrl: String? = null
    var author: String? = null

    @Generated(hash = 931745658)
    constructor(id: Long?, title: String, imageUrl: String, author: String) {
        this.id = id
        this.title = title
        this.imageUrl = imageUrl
        this.author = author
    }

    @Generated(hash = 1839243756)
    constructor() {
    }
}
