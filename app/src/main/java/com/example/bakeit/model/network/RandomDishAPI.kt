package com.example.bakeit.model.network

import com.example.bakeit.model.entities.RandomDish
import com.example.bakeit.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomDishAPI {

    @GET(Constants.API_ENDPOINT)
    fun getRandomDish(
        @Query(Constants.API_KEY) apiKey:String,
        @Query(Constants.LIMIT_LICENSE) limitLicense:Boolean,
        @Query(Constants.TAGS) tags:String,
        @Query(Constants.NUMBER) number:Int,
    ):Single<RandomDish.Recipes>    // single is basically like observable it helps in performing these calls asynchronously and running them in seperate thread not in main thread it is from rxJAVA
}