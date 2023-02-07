package com.example.bakeit.model.database

import androidx.annotation.WorkerThread
import com.example.bakeit.model.entities.Bakeit
import kotlinx.coroutines.flow.Flow

class BakeitRepository(private val bakeitDao: BakeitDao) {

    @WorkerThread
    suspend fun insertBakeitData(bakeit: Bakeit){
        bakeitDao.insertBakeitDetails(bakeit)
    }

    val allDishesList: Flow<List<Bakeit>> = bakeitDao.getAllDishesList()        // flow is like observable it helps in controlling the flow of data coming from database and prevent instant loading of all data
}