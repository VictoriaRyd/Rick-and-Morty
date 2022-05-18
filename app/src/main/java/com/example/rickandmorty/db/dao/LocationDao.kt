package com.example.rickandmorty.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.db.entity.LocationEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocations(location: LocationEntity)

    @Query("SELECT * FROM location_table WHERE (:name IS NULL OR name LIKE '%' || :name || '%') " +
            "AND (:type IS NULL OR type LIKE '%' || :type || '%')" +
            "And (:dimension IS NULL OR dimension LIKE '%' || :dimension || '%')")
    fun getFilteredLocations (name: String?, type: String?, dimension: String?): List<LocationEntity>

    @Query("SELECT * FROM location_table")
    fun getAllLocations(): List<LocationEntity>

    @Query("SELECT * FROM location_table WHERE id = :id")
    fun getLocationById(id: Int): LocationEntity
}