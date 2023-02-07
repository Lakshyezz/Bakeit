package com.example.bakeit.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bakeit.model.entities.Bakeit
import kotlinx.coroutines.flow.Flow

@Dao
interface BakeitDao {

    @Insert
    suspend fun insertBakeitDetails(bakeit: Bakeit)

    @Query("SELECT * FROM Bakeit_table ORDER BY ID")
    fun getAllDishesList(): Flow<List<Bakeit>>

}