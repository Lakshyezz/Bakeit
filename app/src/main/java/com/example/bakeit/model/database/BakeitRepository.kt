package com.example.bakeit.model.database

import androidx.annotation.WorkerThread
import com.example.bakeit.model.entities.Bakeit

class BakeitRepository(private val bakeitDao: BakeitDao) {

    @WorkerThread
    suspend fun inserBakeitData(bakeit: Bakeit){
        bakeitDao.insertFavDishDetails(bakeit)
    }
}