package com.anderson.restaapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anderson.restaapp.model.ItemFood
import com.anderson.restaapp.repository.HomeRepository

class HomeViewModel : ViewModel() {
    private var dateBook: String = ""
    private var timeBook: String = "17:00"
    private var listFood =  ArrayList<ItemFood>()
    private var positionFood = 0
    private val homeRepository = HomeRepository()

    fun getDateBook(): String {return dateBook}
    fun setDateBook(s: String) {dateBook = s}
    fun getTimeBook(): String {return timeBook}
    fun setTimeBook(s: String) {timeBook = s}
    fun getListFood(): ArrayList<ItemFood>{return listFood}
    fun setListFood(data: ArrayList<ItemFood>) {
//        listFood.clear()
//        listFood.add(data)
    }
    fun getPositionFood(): Int {return positionFood}
    fun setPositionFood(data: Int) {positionFood = data}

    fun fetchListFood(){
        homeRepository.processListFood()
    }
    fun getFoodLiveDataObserver(): MutableLiveData<ItemFood>{
        return homeRepository.foodLiveDataObserver()
    }
    fun getStatusFoodLiveDataObserver(): MutableLiveData<String>{
        return homeRepository.statusFoodLiveDataObserver()
    }
    fun getKeysFoodSize(): Int{
        return homeRepository.keysFoodSize()
    }

}