package com.app.citycare.models.events

import java.io.Serializable

class Model : Serializable {
    var tittle: String? = null
    var desc: String? = null
    var author: String? = null
    var date: String? = null
    var img: String? = null
    var share_count: String? = null
    var id: String? = null
    var timestamp: String? = null

    constructor(
        tittle: String?,
        desc: String?,
        author: String?,
        date: String?,
        img: String?,
        share_count: String?,
        id: String?,
        timestamp: String?
    ) {
        this.tittle = tittle
        this.desc = desc
        this.author = author
        this.date = date
        this.img = img
        this.share_count = share_count
        this.id = id
        this.timestamp = timestamp
    }

    constructor() {}
}