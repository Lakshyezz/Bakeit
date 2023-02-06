package com.example.bakeit.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.example.bakeit.model.entities.Bakeit

@Dao
interface BakeitDao {

    @Insert
    suspend fun insertFavDishDetails(bakeit: Bakeit)
}