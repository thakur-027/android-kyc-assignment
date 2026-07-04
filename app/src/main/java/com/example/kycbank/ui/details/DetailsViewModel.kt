package com.example.kycbank.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kycbank.domain.repository.CustomerRepository
import com.example.kycbank.domain.repository.IfscRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val ifscRepository: IfscRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val customerId: String = checkNotNull(savedStateHandle["customerId"])

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    init {
        loadCustomer()
    }

    private fun loadCustomer() {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            val customer = customerRepository.getCustomerById(customerId)
            if (customer == null) {
                _uiState.value = DetailsUiState.Error("Customer not found")
            } else {
                _uiState.value = DetailsUiState.Success(
                    customer = customer,
                    branchInfoState = BranchInfoState.Loading
                )
                resolveBranchInfo(customer.ifsc)
            }
        }
    }

    private fun resolveBranchInfo(ifsc: String) {
        viewModelScope.launch {
            val result = ifscRepository.getBranchInfo(ifsc)
            val currentState = _uiState.value
            if (currentState is DetailsUiState.Success) {
                _uiState.value = currentState.copy(
                    branchInfoState = if (result != null) {
                        BranchInfoState.Loaded(result)
                    } else {
                        BranchInfoState.Failed
                    }
                )
            }
        }
    }

    fun onKycCompleted(selfiePath: String) {
        viewModelScope.launch {
            val success = customerRepository.verifyCustomer(customerId, selfiePath)
            if (success) {
                loadCustomer()
            } else {
                val currentState = _uiState.value
                if (currentState is DetailsUiState.Success) {
                    _uiState.value = DetailsUiState.Error("Failed to update KYC status")
                }
            }
        }
    }
}