package com.evan.restaurantapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evan.restaurantapp.data.MenuRepository
import com.evan.restaurantapp.model.Menu
import com.evan.restaurantapp.model.OrderMenu
import com.evan.restaurantapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailMenuViewModel(private val repository: MenuRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<OrderMenu>> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<OrderMenu>>
        get() = _uiState

    fun getMenuById(menuId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getOrderMenuById(menuId))
        }
    }

    fun addToCart(menu: Menu, count: Int) {
        viewModelScope.launch {
            repository.updateOrderMenu(menu.id, count)
        }
    }
}