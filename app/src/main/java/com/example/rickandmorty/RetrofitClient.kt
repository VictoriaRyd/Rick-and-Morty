package com.example.rickandmorty

import com.example.rickandmorty.api.CharacterApi
import com.example.rickandmorty.api.EpisodeApi
import com.example.rickandmorty.api.LocationApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitClient {

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/"
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideCharactersApi(retrofit: Retrofit): CharacterApi {
        return retrofit.create(CharacterApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEpisodesApi(retrofit: Retrofit): EpisodeApi {
        return retrofit.create(EpisodeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLocationsApi(retrofit: Retrofit): LocationApi {
        return retrofit.create(LocationApi::class.java)
    }

}