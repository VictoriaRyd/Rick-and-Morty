package com.example.rickandmorty.db.converter

import androidx.room.TypeConverter

class EpisodesConverter {

    @TypeConverter
    fun fromEpisodes(episodes: List<String>): String {
        return episodes.joinToString(separator = "\n")
    }

    @TypeConverter
    fun toEpisodes(string: String) : List<String> {
        return string.lines()
    }
}
