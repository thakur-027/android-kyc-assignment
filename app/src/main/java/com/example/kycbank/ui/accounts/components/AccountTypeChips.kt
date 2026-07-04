package com.example.kycbank.ui.accounts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kycbank.domain.model.AccountType

private val chipLabels = listOf("All", "Savings", "Current", "NRI")

private fun labelToType(label: String): AccountType? = when (label) {
    "Savings" -> AccountType.SAVINGS
    "Current" -> AccountType.CURRENT
    "NRI" -> AccountType.NRI
    else -> null // "All"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountTypeChips(
    selectedType: AccountType?,
    onTypeSelected: (AccountType?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chipLabels) { label ->
            val type = labelToType(label)
            val isSelected = selectedType == type
            FilterChip(
                selected = isSelected,
                onClick = { onTypeSelected(type) },
                label = { Text(label) }
            )
        }
    }
}