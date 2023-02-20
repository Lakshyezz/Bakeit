package com.example.bakeit.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Bakeit_table")
data class Bakeit(           // all of them are properties of a single entry(dish)
  @ColumnInfo val image: String,
  @ColumnInfo(name = "image_source") val imageSource: String,
  @ColumnInfo val title: String,
  @ColumnInfo val type: String,
  @ColumnInfo val category: String,
  @ColumnInfo val ingredients: String,

  @ColumnInfo(name = "cooking_time") val cookingTime: String,
  @ColumnInfo(name = "instructions") val directionsToCook: String,
  @ColumnInfo(name = "favorite_dish") val favoriteDish: Boolean = false,
  @PrimaryKey(autoGenerate = true) val id: Int = 0,   // this 'll be unique for every dish

 ):Parcelable