package com.example.rickandmorty.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.db.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character_table")
    fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM character_table WHERE id = :id")
    fun getCharacterById(id: Int): CharacterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCharacters(character: CharacterEntity)

    @Query("SELECT * FROM character_table WHERE (:name IS NULL OR name LIKE '%' || :name || '%')" +
            "AND (:status IS NULL OR status LIKE :status)" +
            "AND (:species IS NULL OR species LIKE '%' || :species || '%')" +
            "AND (:type IS NULL OR type LIKE '%' || :type || '%')" +
            "AND (:gender IS NULL OR gender LIKE :gender)")
    fun getFilteredCharacters(name: String?, status: String?, species: String?,
                              type: String?, gender: String?): List<CharacterEntity>
}