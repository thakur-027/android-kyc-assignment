package com.example.kycbank.ui.details

import com.example.kycbank.domain.model.BankBranchInfo
import com.example.kycbank.domain.model.Customer

sealed interface DetailsUiState {
    data object Loading : DetailsUiState

    data class Success(
        val customer: Customer,
        val branchInfoState: BranchInfoState
    ) : DetailsUiState

    data class Error(val message: String) : DetailsUiState
}

sealed interface BranchInfoState {
    data object Loading : BranchInfoState
    data class Loaded(val branchInfo: BankBranchInfo) : BranchInfoState
    data object Failed : BranchInfoState
}