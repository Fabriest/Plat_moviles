package com.moviles.lab8.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: Int): Flow<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characters: List<CharacterEntity>)

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("DELETE FROM characters")
    fun clearAll()
}

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun getLocationById(id: Int): Flow<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: List<LocationEntity>)

    @Query("DELETE FROM locations")
    fun clearAll()
}