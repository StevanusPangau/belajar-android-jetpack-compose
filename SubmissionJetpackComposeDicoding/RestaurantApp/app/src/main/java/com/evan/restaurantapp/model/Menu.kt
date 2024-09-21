package com.evan.restaurantapp.model

data class Menu(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val image: Int
)
