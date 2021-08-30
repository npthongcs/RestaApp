package com.anderson.restaapp.model

data class DetailBooking(
    var userID: String = "",
    var amountPeople: Int = 0,
    var date: String,
    var time: String,
    var totalPayment: Double,
    var listBook: ArrayList<FoodSelected> = ArrayList()
)

data class FoodSelected(var name: String, var amountFood: Int = 0, var payment: Double = 0.0)
