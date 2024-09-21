package com.evan.restaurantapp.di

import com.evan.restaurantapp.data.MenuRepository

object Injection {
    fun provideRepository(): MenuRepository {
        return MenuRepository.getInstance()
    }
}