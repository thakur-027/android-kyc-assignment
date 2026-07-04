package com.example.kycbank.ui.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kycbank.ui.details.BranchInfoState

@Composable
fun BankInfoSection(
    branchInfoState: BranchInfoState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (branchInfoState) {
            is BranchInfoState.Loading -> {
                CircularProgressIndicator()
            }
            is BranchInfoState.Loaded -> {
                Text(text = "Bank: ${branchInfoState.branchInfo.bankName}")
                Text(text = "Branch: ${branchInfoState.branchInfo.branchName}")
            }
            is BranchInfoState.Failed -> {
                Text(text = "Failed to load bank information.")
            }
        }
    }
}