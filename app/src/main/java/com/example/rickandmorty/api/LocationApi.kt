package com.example.rickandmorty.api

import com.example.rickandmorty.ui.location.Locations
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApi {

    @GET("api/location")
    suspend fun getLocation(
        @Query("page") page: Int
    ): Response<LocationList>

    @GET("api/location/{id}")
    suspend fun getLocationId(
        @Path("id") id: Int
    ): Response<Locations>

    @GET("api/location")
    suspend fun getLocationFilter(
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?
    ): Response<LocationList>
}