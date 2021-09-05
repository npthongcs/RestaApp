package com.anderson.restaapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anderson.restaapp.model.*
import com.anderson.restaapp.repository.HomeRepository

class HomeViewModel : ViewModel() {
    private var dateBook: String = ""
    private var timeBook: String = "17:00"
    private var note: String = ""
    private var positionFood = 0
    private var quantityAvailable = Quantity()
    private var listFood =  ArrayList<ItemFood>()
    private var listDrink = ArrayList<ItemFood>()
    private var listDessert = ArrayList<ItemFood>()
    private var listBooking = ArrayList<FoodSelected>()
    private var listInvoice = ArrayList<DetailBooking>()
    private var listReview = ArrayList<Review>()
    private val homeRepository = HomeRepository()

    fun getDateBook(): String {return dateBook}
    fun setDateBook(s: String) {dateBook = s}
    fun getTimeBook(): String {return timeBook}
    fun setTimeBook(s: String) {timeBook = s}
    fun getListFood(): ArrayList<ItemFood>{return listFood}
    fun getListDrink(): ArrayList<ItemFood>{return listDrink}
    fun getListDessert(): ArrayList<ItemFood>{return listDessert}
    fun getNote(): String {return note}
    fun setNote(data: String) {note = data}
    fun getQuantityAvailable(): Quantity {return quantityAvailable}
    fun setQuantityAvailable(data: Quantity) {quantityAvailable = data}
    fun getListInvoice(): ArrayList<DetailBooking>{return listInvoice}
    fun getListReview(): ArrayList<Review>{return listReview}

    //    fun setListFood(data: ArrayList<ItemFood>) {
////        listFood.clear()
////        listFood.add(data)
//        listFood = data
//    }
    fun getPositionFood(): Int {return positionFood}
    fun setPositionFood(data: Int) {positionFood = data}

    fun getListBooking(): ArrayList<FoodSelected> {return listBooking}
    fun resetListBooking() {listBooking.clear()}

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

    fun getQuantity(data: String){
        homeRepository.processGetQuantityAvailable(data)
    }
    fun getQuantityLiveDataObserver(): MutableLiveData<Quantity>{
        return homeRepository.quantityAvailableLiveDataObserver()
    }

    fun getInvoice(){
        homeRepository.processListInvoice()
    }
    fun getInvoiceLiveDataObserver(): MutableLiveData<DetailBooking>{
        return homeRepository.invoiceLiveDataObserver()
    }
    fun getKeysInvoiceSize(): Int{
        return homeRepository.keysInvoiceSize()
    }

    fun getReview(){
        return homeRepository.processListReview()
    }
    fun getReviewLiveDataObserver(): MutableLiveData<Review>{
        return homeRepository.reviewLiveDataObserver()
    }
    fun getKeysReviewSize(): Int{
        return homeRepository.keysReviewSize()
    }
}