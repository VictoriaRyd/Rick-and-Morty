package com.example.rickandmorty.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rickandmorty.db.converter.CharactersConverter

@Entity(tableName = "episodes_table")
class EpisodesEntity (
        @PrimaryKey
        val id: Int,
        val name: String,
        val episode: String,
        val airDate: String,
        @field:TypeConverters(CharactersConverter::class)
        val characters: List<String>?
)