package com.evan.restaurantapp.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.evan.restaurantapp.di.Injection
import com.evan.restaurantapp.model.FakeMenuDataSource
import com.evan.restaurantapp.model.OrderMenu
import com.evan.restaurantapp.ui.ViewModelFactory
import com.evan.restaurantapp.ui.common.UiState
import com.evan.restaurantapp.ui.components.MenuItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllMenus()
            }
            is UiState.Success -> {
                HomeContent(
                    orderMenu = uiState.data,
                    searchQuery = viewModel.searchQuery.collectAsState().value,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    orderMenu: List<OrderMenu>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Cari Menu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(orderMenu) { data ->
                MenuItem(
                    image = data.menu.image,
                    title = data.menu.name,
                    price = data.menu.price,
                    modifier = Modifier.clickable {
                        navigateToDetail(data.menu.id)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeContent(
        orderMenu = FakeMenuDataSource.dummyMenus.map {
            OrderMenu(it, 0)
        },
        searchQuery = "",
        onSearchQueryChange = {},
        navigateToDetail = {}
    )
}