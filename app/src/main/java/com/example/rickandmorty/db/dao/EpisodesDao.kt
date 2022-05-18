package com.example.rickandmorty.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.db.entity.EpisodesEntity

@Dao
interface EpisodesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEpisodes(episodes: EpisodesEntity)

    @Query("SELECT * FROM episodes_table WHERE id = :id")
    fun getEpisodeById(id: Int): EpisodesEntity

    @Query("SELECT * FROM episodes_table WHERE (:name IS NULL OR name LIKE '%' || :name || '%') " +
            "AND (:episode IS NULL OR episode LIKE '%' || :episode || '%')")
    fun getFilteredEpisodes(name: String?, episode: String?): List<EpisodesEntity>


}