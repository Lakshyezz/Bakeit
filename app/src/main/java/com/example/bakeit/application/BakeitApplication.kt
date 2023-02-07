package com.example.bakeit.application

import android.app.Application
import com.example.bakeit.model.database.BakeitRepository
import com.example.bakeit.model.database.BakeitRoomDatabase

class BakeitApplication : Application(){

    private val database by lazy{ BakeitRoomDatabase.getDatabase((this@BakeitApplication))}

    val repository by lazy { BakeitRepository(database.bakeitDao()) }
}