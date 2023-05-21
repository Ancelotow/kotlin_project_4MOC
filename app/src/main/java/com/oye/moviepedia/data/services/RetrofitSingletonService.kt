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
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzZjQ2ZTkxNmIzMzU3NWI5YzkwYTIxOGIyZmM2ODBkMyIsInN1YiI6IjYxZmIxNWM0Y2I4MDI4MDA5MjFmMzkyZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.chNYMRN1RzO6hc-w_5mUE2vu8D_tsIWx8o8hRVf8_A8").build()
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

class LocalDateDeserializer : JsonDeserializer<LocalDate> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        val dateString = json?.asString
        return LocalDate.parse(dateString, formatter)
    }
}