package com.example.kycbank.ui.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kycbank.domain.model.Customer
import com.example.kycbank.domain.model.KycStatus

@Composable
fun KycProfileSection(
    customer: Customer,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = customer.selfiePath ?: customer.avatarUrl,
                    contentDescription = "Customer photo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = customer.fullName.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "A/C: ${customer.maskedAccountNumber}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (customer.kycStatus == KycStatus.VERIFIED) {
                        StatusPill()
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = customer.formattedBalance,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${customer.accountType.name.lowercase().replaceFirstChar { it.uppercase() }} A/C",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        InfoRow("Date of Birth", customer.dob)
        InfoRow("Nationality", customer.nationality)
        InfoRow("Phone", customer.phoneNo)
        InfoRow("Email", customer.email)
        InfoRow("Address", customer.address)
    }
}

@Composable
private fun StatusPill() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .padding(top = 4.dp)
            .background(
                androidx.compose.ui.graphics.Color(0xFFDFF5E1),
                androidx.compose.foundation.shape.RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            "KYC VERIFIED",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color(0xFF1E7A32)
        )
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
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}