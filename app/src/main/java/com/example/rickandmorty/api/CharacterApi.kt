package com.example.rickandmorty.api

import com.example.rickandmorty.ui.character.Character
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApi {

    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharacterList>

    @GET("api/character/{id}")
    suspend fun getCharacterId(
        @Path("id") id : Int
    ): Response<Character>

    @GET("api/character")
    suspend fun getCharacterFilter(
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?
    ): Response<CharacterList>
}