package com.example.kycbank.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kycbank.domain.model.AccountType
import com.example.kycbank.domain.model.KycStatus
import com.example.kycbank.domain.usecase.GetCustomersUseCase
import com.example.kycbank.domain.usecase.SearchCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGE_SIZE = 10

@OptIn(FlowPreview::class)
@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getCustomersUseCase: GetCustomersUseCase,
    private val searchCustomersUseCase: SearchCustomersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Loading)
    val uiState: StateFlow<AccountsUiState> = _uiState

    private val _selectedTab = MutableStateFlow(KycStatus.PENDING)
    val selectedTab: StateFlow<KycStatus> = _selectedTab

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val debouncedSearchQuery = _searchQuery
        .debounce(300)
        .distinctUntilChanged()

    private val _selectedAccountType = MutableStateFlow<AccountType?>(null)
    val selectedAccountType: StateFlow<AccountType?> = _selectedAccountType

    private val _visibleCount = MutableStateFlow(PAGE_SIZE)

    fun onTabSelected(status: KycStatus) {
        _selectedTab.value = status
        _visibleCount.value = PAGE_SIZE // reset window when switching tabs
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _visibleCount.value = PAGE_SIZE // reset window on new search
    }

    fun onAccountTypeSelected(type: AccountType?) {
        _selectedAccountType.value = type
        _visibleCount.value = PAGE_SIZE // reset window when filter changes
    }

    fun loadMore() {
        _visibleCount.value += PAGE_SIZE
    }

    val filteredUiState: StateFlow<AccountsUiState> = combine(
        _uiState,
        _selectedTab,
        debouncedSearchQuery,
        _selectedAccountType,
        _visibleCount
    ) { values ->
        val state = values[0] as AccountsUiState
        val tab = values[1] as KycStatus
        val query = values[2] as String
        val accountType = values[3] as AccountType?
        val visibleCount = values[4] as Int

        if (state is AccountsUiState.Success) {
            val filtered = searchCustomersUseCase(state.customers, tab, query, accountType)
            val windowed = filtered.take(visibleCount)
            AccountsUiState.Success(
                customers = windowed,
                hasMore = filtered.size > visibleCount
            )
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
            getCustomersUseCase()
                .catch { e ->
                    _uiState.value = AccountsUiState.Error(e.message ?: "Unknown Error")
                }
                .collect { customers ->
                    _uiState.value = AccountsUiState.Success(customers, hasMore = false)
                }
        }
    }
}