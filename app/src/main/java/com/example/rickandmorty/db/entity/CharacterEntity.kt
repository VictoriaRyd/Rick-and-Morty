package com.example.rickandmorty.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rickandmorty.db.converter.EpisodesConverter

@Entity(tableName = "character_table")
data class CharacterEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val name: String,
        val image: String,
        val species: String,
        val status: String,
        val type: String,
        val created: String,
        val gender: String,
        val locationName: String,
        val locationUrl: String,
        val originName: String,
        val originUrl: String,
        @field:TypeConverters(EpisodesConverter::class)
        val episode: List<String>
        )