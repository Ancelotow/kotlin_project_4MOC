package com.oye.moviepedia.data.services

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class RetrofitSingletonService private constructor() {

    val service: ApiService

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .create()
        service = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YTY0ZTkyZGZkOGZiYzFiNGIyMTUxZDZmNjE0N2RmNiIsInN1YiI6IjY0YTE1MmIyOGMwYTQ4MDBhZTI0OGY4NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.VBAr8Y4MNuG7ygqfqF6k_TCeFSliB2n-h74IUvc3Rj8").build()
                chain.proceed(request)
            }.build())
            .build()
            .create(ApiService::class.java);
    }

    companion object {
        private var instance: RetrofitSingletonService? = null;

        fun getInstance(): RetrofitSingletonService {
            if (instance == null) {
                instance = RetrofitSingletonService();
            }
            return instance!!
        }
    }

}

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