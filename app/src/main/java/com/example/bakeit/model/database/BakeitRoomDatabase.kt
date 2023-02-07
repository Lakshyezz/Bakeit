package com.example.bakeit.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bakeit.model.entities.Bakeit

@Database(entities = [Bakeit:: class], version = 1)
abstract class BakeitRoomDatabase:RoomDatabase() {

    abstract fun bakeitDao():BakeitDao

    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: BakeitRoomDatabase? = null

        fun  getDatabase(context: Context): BakeitRoomDatabase {
                // if the INSTANCE is not null, then return it.
                // if it is, then create one
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BakeitRoomDatabase::class.java,
                    "bakeit_database"
                )
//                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE  = instance
                // return instance
                return instance
            }
            }
    }
}