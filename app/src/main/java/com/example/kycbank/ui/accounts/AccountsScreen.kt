package com.example.kycbank.ui.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kycbank.domain.model.KycStatus
import com.example.kycbank.ui.accounts.components.AccountTypeChips
import com.example.kycbank.ui.accounts.components.CustomerCard
import com.example.kycbank.ui.accounts.components.SearchBar
import com.example.kycbank.ui.theme.ThemeMode
import com.example.kycbank.ui.theme.ThemeViewModel
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow

@Composable
fun AccountsScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel,
    onCustomerClick: (String) -> Unit = {}
) {
    val uiState by viewModel.filteredUiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedAccountType by viewModel.selectedAccountType.collectAsStateWithLifecycle()
    val themeMode by themeViewModel.themeMode.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Signzy - KYC",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (selectedTab == KycStatus.PENDING) "Pending KYC" else "Verified KYC",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = {
                    val next = if (themeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
                    themeViewModel.setThemeMode(next)
                }
            ) {
                Icon(
                    imageVector = if (themeMode == ThemeMode.DARK) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle theme"
                )
            }
        }

        TabRow(selectedTabIndex = if (selectedTab == KycStatus.PENDING) 0 else 1) {
            Tab(
                selected = selectedTab == KycStatus.PENDING,
                onClick = { viewModel.onTabSelected(KycStatus.PENDING) },
                text = { Text("Pending") }
            )
            Tab(
                selected = selectedTab == KycStatus.VERIFIED,
                onClick = { viewModel.onTabSelected(KycStatus.VERIFIED) },
                text = { Text("Verified") }
            )
        }

        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged
        )

        AccountTypeChips(
            selectedType = selectedAccountType,
            onTypeSelected = viewModel::onAccountTypeSelected
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (uiState) {
                is AccountsUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is AccountsUiState.Success -> {
                    val state = uiState as AccountsUiState.Success
                    if (state.customers.isEmpty()) {
                        Text("No customers match your search.")
                    } else {
                        val gridState = rememberLazyGridState()

                        LaunchedEffect(gridState, state.customers.size, state.hasMore) {
                            snapshotFlow {
                                val layoutInfo = gridState.layoutInfo
                                val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                lastVisible >= layoutInfo.totalItemsCount - 4
                            }.collect { nearBottom ->
                                if (nearBottom && state.hasMore) {
                                    viewModel.loadMore()
                                }
                            }
                        }

                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.customers) { customer ->
                                CustomerCard(
                                    customer = customer,
                                    onClick = { onCustomerClick(customer.id) }
                                )
                            }
                            if (state.hasMore) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        }
                    }
                }
                is AccountsUiState.Error -> {
                    val state = uiState as AccountsUiState.Error
                    Text(text = state.message)
                }
            }
        }
    }
}