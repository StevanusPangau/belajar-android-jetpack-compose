package com.evan.restaurantapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evan.restaurantapp.data.MenuRepository
import com.evan.restaurantapp.ui.screen.cart.CartViewModel
import com.evan.restaurantapp.ui.screen.detail.DetailMenuViewModel
import com.evan.restaurantapp.ui.screen.home.HomeViewModel

class ViewModelFactory(private val repository: MenuRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailMenuViewModel::class.java)) {
            return DetailMenuViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}