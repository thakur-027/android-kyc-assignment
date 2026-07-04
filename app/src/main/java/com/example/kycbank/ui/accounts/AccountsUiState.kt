package com.example.kycbank.ui.accounts
import com.example.kycbank.domain.model.Customer

sealed interface AccountsUiState {

    data object Loading : AccountsUiState

    data class Success(
        val customers: List<Customer>,
        val hasMore: Boolean
    ) : AccountsUiState

    data class Error(val message: String) : AccountsUiState
}