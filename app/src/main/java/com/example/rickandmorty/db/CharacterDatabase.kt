package com.example.rickandmorty.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.db.dao.CharacterDao
import com.example.rickandmorty.db.dao.EpisodesDao
import com.example.rickandmorty.db.dao.LocationDao
import com.example.rickandmorty.db.entity.CharacterEntity
import com.example.rickandmorty.db.entity.EpisodesEntity
import com.example.rickandmorty.db.entity.LocationEntity

@Database(entities = [CharacterEntity::class, LocationEntity::class, EpisodesEntity::class], version = 1)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun getCharacterDao(): CharacterDao
    abstract fun getEpisodesDao(): EpisodesDao
    abstract fun getLocationDao(): LocationDao

}