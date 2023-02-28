package com.example.bakeit.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.bakeit.model.entities.RandomDish
import com.example.bakeit.model.network.RandomDishApiService
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RandomDishViewModel {

    private val randomRecipeApiService = RandomDishApiService()

    private val compositeDisposable = CompositeDisposable() // work in disposing of containers when infinite data stream comes from internet(api)

    val loadRandomDish = MutableLiveData<Boolean>()
    val randomDishResponse = MutableLiveData<RandomDish.Recipes>()
    val randomDishLoadingError = MutableLiveData<Boolean>()

    fun getRandomRecipeFromApi(){
        loadRandomDish.value = true
    }
}