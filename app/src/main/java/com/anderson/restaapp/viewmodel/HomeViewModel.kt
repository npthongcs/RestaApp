package com.anderson.restaapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anderson.restaapp.model.FoodSelected
import com.anderson.restaapp.model.ItemFood
import com.anderson.restaapp.repository.HomeRepository

class HomeViewModel : ViewModel() {
    private var dateBook: String = ""
    private var timeBook: String = "17:00"
    private var positionFood = 0
    private var listFood =  ArrayList<ItemFood>()
    private var listDrink = ArrayList<ItemFood>()
    private var listDessert = ArrayList<ItemFood>()
    private var listBooking = ArrayList<FoodSelected>()
    private val homeRepository = HomeRepository()

    fun getDateBook(): String {return dateBook}
    fun setDateBook(s: String) {dateBook = s}
    fun getTimeBook(): String {return timeBook}
    fun setTimeBook(s: String) {timeBook = s}
    fun getListFood(): ArrayList<ItemFood>{return listFood}
    fun getListDrink(): ArrayList<ItemFood>{return listDrink}
    fun getListDessert(): ArrayList<ItemFood>{return listDessert}
    //    fun setListFood(data: ArrayList<ItemFood>) {
////        listFood.clear()
////        listFood.add(data)
//        listFood = data
//    }
    fun getPositionFood(): Int {return positionFood}
    fun setPositionFood(data: Int) {positionFood = data}

    fun getListBook(): ArrayList<FoodSelected> {return listBooking}

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

    fun fetchListDrink(){
        homeRepository.processListDrink()
    }
    fun getDrinkLiveDataObserver(): MutableLiveData<ItemFood>{
        return homeRepository.drinkLiveDataObserver()
    }
    fun getStatusDrinkLiveDataObserver(): MutableLiveData<String>{
        return homeRepository.statusDrinkLiveDataObserver()
    }
    fun getKeysDrinkSize(): Int{
        return homeRepository.keysDrinkSize()
    }

    fun fetchListDessert(){
        homeRepository.processListDessert()
    }
    fun getDessertLiveDataObserver(): MutableLiveData<ItemFood>{
        return homeRepository.dessertLiveDataObserver()
    }
    fun getStatusDessertLiveDataObserver(): MutableLiveData<String>{
        return homeRepository.statusDessertLiveDataObserver()
    }
    fun getKeysDessertSize(): Int{
        return homeRepository.keysDessertSize()
    }
}