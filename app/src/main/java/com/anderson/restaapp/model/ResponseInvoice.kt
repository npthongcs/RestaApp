package com.anderson.restaapp.model

data class ResponseInvoice(
    val amountPeople: Int = 0,
    val count: Int = 0,
    val date: String = "",
    val dateTimePayment: String = "",
    val discount: Double = 0.0,
    val note: String = "",
    val time: String = "",
    val totalPayment: Double = 0.0,
    val urlQRCode: String = ""
)

data class  FoodBooking(
    val amountFood: Int = 0,
    val name: String = "",
    val payment: Double = 0.0
)
