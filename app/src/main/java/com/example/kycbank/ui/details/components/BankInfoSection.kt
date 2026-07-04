package com.example.kycbank.ui.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kycbank.ui.details.BranchInfoState

@Composable
fun BankInfoSection(
    branchInfoState: BranchInfoState,
    ifsc: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (branchInfoState) {
            is BranchInfoState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 8.dp))
            }
            is BranchInfoState.Loaded -> {
                InfoRow("Bank / Branch", "${branchInfoState.branchInfo.bankName}, ${branchInfoState.branchInfo.branchName}")
                InfoRow("IFSC", ifsc)
            }
            is BranchInfoState.Failed -> {
                Text(
                    text = "Failed to load bank information.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
    }
}