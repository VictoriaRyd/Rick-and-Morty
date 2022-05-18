package com.example.rickandmorty.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rickandmorty.db.converter.CharactersConverter

@Entity(tableName = "location_table")
data class LocationEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    @field:TypeConverters(CharactersConverter::class)
    val residents: List<String>
)