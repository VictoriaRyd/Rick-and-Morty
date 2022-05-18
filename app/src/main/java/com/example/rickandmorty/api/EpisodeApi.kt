package com.example.rickandmorty.api

import com.example.rickandmorty.ui.episodes.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeApi {

    @GET("api/episode")
    suspend fun getEpisodes(
        @Query("page") page: Int
    ): Response<EpisodesList>

    @GET("api/episode/{id}")
    suspend fun getEpisodeId(
        @Path("id") id: Int
    ): Response<Episode>

    @GET("api/episode")
    suspend fun getEpisodeFilter(
        @Query("name") name: String?,
        @Query("episode") code: String?
    ): Response<EpisodesList>
}