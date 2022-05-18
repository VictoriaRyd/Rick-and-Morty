package com.example.rickandmorty.api

import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.episodes.Episode
import com.example.rickandmorty.ui.location.Locations

data class CharacterList(
    val results: List<Character>,
    val info : Result
)

data class EpisodesList(
    var results: List<Episode>,
    val info : Result
)

data class LocationList(
    var results: List<Locations>,
    val info : Result
)

data class Result(
    val pages: Int
)