package com.oye.moviepedia.domain.entities
import com.google.gson.annotations.SerializedName

data class NewItem(
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("media_id")
    val mediaId: Int
)
