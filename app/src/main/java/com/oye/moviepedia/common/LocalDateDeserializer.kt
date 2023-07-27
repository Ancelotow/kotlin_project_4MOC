package com.oye.moviepedia.common

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateDeserializer : JsonDeserializer<LocalDate?> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate? {
        val dateString = json?.asString
        if(dateString.isNullOrEmpty()) {
            return null
        }
        return LocalDate.parse(dateString, formatter)
    }
}