package com.anderson.restaapp.model

data class Review(
    val name: String = "",
    val star: Int = 0,
    val time: String = "",
    val content: String = ""
)

data class Rating(
    val number: Int = 0,
    val rating: Double = 0.0
)
