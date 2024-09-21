package com.evan.restaurantapp.ui.screen.cart

import com.evan.restaurantapp.model.OrderMenu


data class CartState(
    val orderMenu: List<OrderMenu>,
    val totalPrice: Double
)