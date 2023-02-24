package com.example.bakeit.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bakeit.model.entities.Bakeit
import kotlinx.coroutines.flow.Flow

@Dao
interface BakeitDao {

    @Insert
    suspend fun insertBakeitDetails(bakeit: Bakeit)

    @Query("SELECT * FROM Bakeit_table ORDER BY ID")
    fun getAllDishesList(): Flow<List<Bakeit>>

    @Update
    suspend fun updateBakeitDetails(bakeit: Bakeit)

    @Query("SELECT * FROM Bakeit_table WHERE favorite_dish = 1")
    fun getFavoriteDishList(): Flow<List<Bakeit>>

}