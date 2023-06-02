package com.app.citycare.models.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class User(
    var uid: String,
    var name: String,
    var number: String,
    val imageUrl: String
)