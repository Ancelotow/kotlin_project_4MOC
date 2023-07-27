package com.oye.moviepedia.di

import com.google.gson.GsonBuilder
import com.oye.moviepedia.common.API_TOKEN
import com.oye.moviepedia.common.BASE_URL
import com.oye.moviepedia.common.LocalDateDeserializer
import com.oye.moviepedia.data.data_sources.AuthDataSource
import com.oye.moviepedia.data.data_sources.DynamicLinkDataSource
import com.oye.moviepedia.data.data_sources.LogoutDataSource
import com.oye.moviepedia.data.data_sources.MovieDataSource
import com.oye.moviepedia.data.data_sources.PlaylistDataSource
import com.oye.moviepedia.data.data_sources.firebase.FirebaseDynamicLinkDataSource
import com.oye.moviepedia.data.data_sources.remote.RemoteAuthDataSource
import com.oye.moviepedia.data.data_sources.remote.RemoteLogoutDataSource
import com.oye.moviepedia.data.data_sources.remote.RemoteMovieDataSource
import com.oye.moviepedia.data.data_sources.remote.RemotePlaylistDataSource
import com.oye.moviepedia.data.repositories.FirebaseDynamicLinkRepository
import com.oye.moviepedia.data.repositories.RemoteAuthRepository
import com.oye.moviepedia.data.repositories.RemoteLogoutRepository
import com.oye.moviepedia.data.repositories.RemoteMovieRepository
import com.oye.moviepedia.data.repositories.RemotePlaylistRepository
import com.oye.moviepedia.data.services.ApiService
import com.oye.moviepedia.domain.repositories.AuthRepository
import com.oye.moviepedia.domain.repositories.DynamicLinkRepository
import com.oye.moviepedia.domain.repositories.LogoutRepository
import com.oye.moviepedia.domain.repositories.MovieRepository
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): ApiService {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer $API_TOKEN").build()
                chain.proceed(request)
            }.build())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(apiService: ApiService): MovieDataSource =
        RemoteMovieDataSource(apiService)

    @Provides
    @Singleton
    fun provideAuthDataSource(apiService: ApiService): AuthDataSource =
        RemoteAuthDataSource(apiService)

    @Provides
    @Singleton
    fun provideLogoutDataSource(apiService: ApiService): LogoutDataSource =
        RemoteLogoutDataSource(apiService)

    @Provides
    @Singleton
    fun provideDynamicLinkDataSource(): DynamicLinkDataSource = FirebaseDynamicLinkDataSource()

    @Provides
    @Singleton
    fun provideMovieRepository(dataSource: MovieDataSource): MovieRepository =
        RemoteMovieRepository(dataSource)

    @Provides
    @Singleton
    fun provideAuthRepository(dataSource: AuthDataSource): AuthRepository =
        RemoteAuthRepository(dataSource)

    @Provides
    @Singleton
    fun provideLogoutRepository(dataSource: LogoutDataSource): LogoutRepository =
        RemoteLogoutRepository(dataSource)

    @Provides
    @Singleton
    fun provideDynamicLinkRepository(dataSource: DynamicLinkDataSource): DynamicLinkRepository =
        FirebaseDynamicLinkRepository(dataSource)

    @Provides
    @Singleton
    fun providePlaylistDataSource(apiService: ApiService): PlaylistDataSource = RemotePlaylistDataSource(apiService)

    @Provides
    @Singleton
    fun providePlaylistRepository(dataSource: PlaylistDataSource): PlaylistRepository = RemotePlaylistRepository(dataSource)
}