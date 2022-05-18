package com.example.rickandmorty.db.converter

import androidx.room.TypeConverter

class CharactersConverter {

    @TypeConverter
    fun fromCharacters(characters: List<String>): String {
        return characters.joinToString(separator = "\n")
    }

    @TypeConverter
    fun toCharacters(string: String) : List<String> {
        return string.lines()
    }

}