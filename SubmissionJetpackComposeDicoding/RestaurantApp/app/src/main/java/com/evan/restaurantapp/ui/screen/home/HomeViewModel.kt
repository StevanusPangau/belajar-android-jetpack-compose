package com.evan.restaurantapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evan.restaurantapp.data.MenuRepository
import com.evan.restaurantapp.model.OrderMenu
import com.evan.restaurantapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MenuRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _uiState: MutableStateFlow<UiState<List<OrderMenu>>> = MutableStateFlow(UiState.Loading)
//    val uiState: StateFlow<UiState<List<OrderMenu>>>
//        get() = _uiState

    val uiState: StateFlow<UiState<List<OrderMenu>>> = repository.getAllMenus()
        .combine(_searchQuery) { orderMenus, query ->
            if (query.isEmpty()) {
                orderMenus
            } else {
                orderMenus.filter {
                    it.menu.name.contains(query, ignoreCase = true)
                }
            }
        }
        .map<List<OrderMenu>, UiState<List<OrderMenu>>> {
            UiState.Success(it)
        }
        .catch { exception ->
            emit(UiState.Error(exception.message.toString()))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun getAllMenus() {
        viewModelScope.launch {
            repository.getAllMenus()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { orderMenus ->
                    _uiState.value = UiState.Success(orderMenus)
                }
        }
    }

    // Fitur Search
    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }
}