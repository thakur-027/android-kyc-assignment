package com.example.kycbank.ui.accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kycbank.domain.model.KycStatus

@Composable
fun AccountsScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = hiltViewModel(),
    onCustomerClick: (String) -> Unit = {}
) {
    val uiState by viewModel.filteredUiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
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

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (uiState) {
                is AccountsUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is AccountsUiState.Success -> {
                    val state = uiState as AccountsUiState.Success
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.customers) { customer ->
                            Text(
                                text = customer.fullName,
                                modifier = Modifier.clickable { onCustomerClick(customer.id) }
                            )
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