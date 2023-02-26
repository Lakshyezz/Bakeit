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

    @WorkerThread
     suspend fun updateBakeitData(bakeit: Bakeit){
         bakeitDao.updateBakeitDetails(bakeit)
     }

    val favoriteDishes: Flow<List<Bakeit>> = bakeitDao.getFavoriteDishList()

    @WorkerThread
    suspend fun deleteBakeitData(bakeit: Bakeit){
        bakeitDao.deleteBakeitDetails(bakeit)
    }

    fun filteredListDishes(value: String): Flow<List<Bakeit>> =
                bakeitDao.getFilteredDishesList(value)
}