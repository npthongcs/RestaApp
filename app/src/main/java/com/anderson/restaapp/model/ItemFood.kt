package com.anderson.restaapp.model

import java.io.Serializable

data class ItemFood(
    val name: String = "",
    val price: Double = 0.0,
    val url: String = "",
    val description: String = "",
    val discount: Double = 0.0
): Serializable
