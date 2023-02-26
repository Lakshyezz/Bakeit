package com.example.bakeit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bakeit.model.database.BakeitRepository
import com.example.bakeit.model.entities.Bakeit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BakeitViewModel(private val repository: BakeitRepository): ViewModel() {

       fun insert(dish: Bakeit) = viewModelScope.launch{
        repository.insertBakeitData(dish)
    }

    val allDishesList: LiveData<List<Bakeit>> = repository.allDishesList.asLiveData()

    fun  update(dish: Bakeit) = viewModelScope.launch {
        repository.updateBakeitData(dish)
    }

    val favoriteDishes: LiveData<List<Bakeit>> = repository.favoriteDishes.asLiveData()

    fun  delete(dish: Bakeit) = viewModelScope.launch {
        repository.deleteBakeitData(dish)
    }

}
class BakeitViewModelFactory(private val repository: BakeitRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(BakeitViewModel::class.java)){
           @Suppress("UNCHECKED_CAST")
           return BakeitViewModel(repository) as T
       }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}