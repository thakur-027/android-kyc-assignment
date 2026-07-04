package com.example.kycbank.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kycbank.domain.model.KycStatus
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.kycbank.ui.details.components.BankInfoSection
import com.example.kycbank.ui.details.components.KycProfileSection

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val selfiePath = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("selfiePath", null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(selfiePath?.value) {
        selfiePath?.value?.let { path ->
            viewModel.onKycCompleted(path)
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selfiePath")
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (uiState) {
            is DetailsUiState.Loading -> {
                CircularProgressIndicator()
            }
            is DetailsUiState.Success -> {
                val state = uiState as DetailsUiState.Success
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    KycProfileSection(customer = state.customer)
                    Spacer(modifier = Modifier.height(16.dp))
                    BankInfoSection(branchInfoState = state.branchInfoState, ifsc = state.customer.ifsc)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (state.customer.kycStatus == KycStatus.PENDING) {
                        Button(onClick = { navController.navigate("camera/${state.customer.id}") }) {
                            Text("Do KYC")
                        }
                    }
                }
            }
            is DetailsUiState.Error -> {
                val state = uiState as DetailsUiState.Error
                Text(text = state.message)
            }
        }
    }
}