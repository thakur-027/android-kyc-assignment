package com.example.kycbank.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kycbank.domain.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.kycbank.domain.model.KycStatus
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val repository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Loading)
    val uiState: StateFlow<AccountsUiState> = _uiState

    private val _selectedTab = MutableStateFlow(KycStatus.PENDING)
    val selectedTab: StateFlow<KycStatus> = _selectedTab

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun onTabSelected(status: KycStatus) {
        _selectedTab.value = status
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    val filteredUiState: StateFlow<AccountsUiState> = combine(
        _uiState,
        _selectedTab,
        _searchQuery
    ) { state, tab, query ->
        if (state is AccountsUiState.Success) {
            val filtered = state.customers.filter { customer ->
                val matchesTab = customer.kycStatus == tab
                val matchesSearch = query.isBlank() ||
                        customer.fullName.contains(query, ignoreCase = true) ||
                        customer.maskedAccountNumber.contains(query, ignoreCase = true)

                matchesTab && matchesSearch
            }
            AccountsUiState.Success(filtered)
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccountsUiState.Loading
    )

    init {
        loadCustomers()
    }

    private fun loadCustomers() {
        viewModelScope.launch {
            _uiState.value = AccountsUiState.Loading
            repository.getCustomers()
                .catch { e ->
                    _uiState.value = AccountsUiState.Error(e.message ?: "Unknown Error")
                }
                .collect { customers ->
                    _uiState.value = AccountsUiState.Success(customers)
                }
        }
    }
}