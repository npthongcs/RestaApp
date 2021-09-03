package com.anderson.restaapp.model

import java.io.Serializable

data class DetailBooking(
    val amountPeople: Int = 0,
    val date: String,
    val time: String,
    val note: String = "",
    val count: Int = 0,
    val totalPayment: Double = 0.0,
    val discount: Double = 0.0,
    val listBook: ArrayList<FoodSelected> = ArrayList(),
    val dateTimePayment: String = "",
): Serializable

data class FoodSelected(var name: String, var amountFood: Int = 0, var payment: Double = 0.0): Serializable
