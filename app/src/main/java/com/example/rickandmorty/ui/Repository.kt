package com.example.rickandmorty.ui

import com.example.rickandmorty.api.CharacterList
import com.example.rickandmorty.api.EpisodesList
import com.example.rickandmorty.api.LocationList
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.character.CharactersFilter
import com.example.rickandmorty.ui.episodes.Episode
import com.example.rickandmorty.ui.episodes.EpisodesFilter
import com.example.rickandmorty.ui.location.Locations
import com.example.rickandmorty.ui.location.LocationsFilter

interface Repository {

    suspend fun getCharacters(page: Int, filter: CharactersFilter): CharacterList?

    suspend fun getCharacterId(id : Int) : Character?

    suspend fun getEpisodes(page: Int, filter: EpisodesFilter): EpisodesList?

    suspend fun getEpisodeId(id : Int): Episode?

    suspend fun getLocation(page: Int, filter: LocationsFilter): LocationList?

    suspend fun getLocationId(id: Int): Locations?

    suspend fun getLocationFilter(filter: LocationsFilter): LocationList?

    suspend fun getCharacterFilter(filter: CharactersFilter): CharacterList?

    suspend fun getEpisodeFilter(filter: EpisodesFilter): EpisodesList?
}