package com.example.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.db.CharacterDatabase
import com.example.rickandmorty.db.dao.CharacterDao
import com.example.rickandmorty.db.dao.EpisodesDao
import com.example.rickandmorty.db.dao.LocationDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): CharacterDatabase {
        return Room.databaseBuilder(context, CharacterDatabase::class.java, "database")
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: CharacterDatabase): CharacterDao {
        return database.getCharacterDao()
    }

    @Provides
    @Singleton
    fun provideEpisodeDao(database: CharacterDatabase): EpisodesDao {
        return database.getEpisodesDao()
    }

    @Provides
    @Singleton
    fun provideLocationDao(database: CharacterDatabase): LocationDao {
        return database.getLocationDao()
    }

}