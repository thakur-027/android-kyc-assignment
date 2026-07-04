package com.example.kycbank.ui.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kycbank.domain.model.Customer
import com.example.kycbank.domain.model.KycStatus
import androidx.compose.foundation.layout.size

@Composable
fun KycProfileSection(
    customer: Customer,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AsyncImage(
            model = customer.selfiePath ?: customer.avatarUrl,
            contentDescription = "Customer photo",
            modifier = Modifier
                .size(96.dp)
                .padding(bottom = 8.dp)
                .clip(CircleShape)
        )
        Text(text = customer.fullName)
        Text(text = "DOB: ${customer.dob}")
        Text(text = "Nationality: ${customer.nationality}")
        Text(text = "Address: ${customer.address}")
        Text(text = "Phone: ${customer.phoneNo}")
        Text(text = "Email: ${customer.email}")
        Text(text = "A/C: ${customer.maskedAccountNumber}")
        Text(text = "Balance: ${customer.formattedBalance}")
        Text(text = "Status: ${if (customer.kycStatus == KycStatus.VERIFIED) "Verified" else "Pending"}")
    }
}