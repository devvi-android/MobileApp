package com.app.citycare.models.news

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "article"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var author: String,
    var content: String,
    val description: String,
    var publishedAt: String,
    val source: Source,
    val title: String,
    var url: String,
    val urlToImage: String
) : Serializable