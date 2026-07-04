package com.example.kycbank.ui.accounts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.kycbank.domain.model.Customer
import com.example.kycbank.domain.model.KycStatus
import com.example.kycbank.ui.theme.SuccessGreenSoft
import com.example.kycbank.ui.theme.SuccessGreen
import com.example.kycbank.ui.theme.WarningAmber
import com.example.kycbank.ui.theme.WarningAmberSoft

@Composable
fun CustomerCard(
    customer: Customer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                CustomerAvatar(customer = customer, size = 36.dp)
                KycStatusBadge(status = customer.kycStatus)
            }

            Text(
                text = customer.fullName,
                modifier = Modifier.padding(top = 10.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1
            )
            Text(
                text = customer.maskedAccountNumber,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = customer.formattedBalance,
                modifier = Modifier.padding(top = 6.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            if (customer.kycStatus == KycStatus.PENDING) {
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Do KYC", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun CustomerAvatar(customer: Customer, size: androidx.compose.ui.unit.Dp) {
    val palette = listOf(
        Color(0xFFE3EDFB), Color(0xFFE8F6EC), Color(0xFFFFF3E0), Color(0xFFF3E8FD)
    )
    val bgColor = palette[customer.id.hashCode().mod(palette.size)]

    SubcomposeAsyncImage(
        model = customer.displayPhoto,
        contentDescription = "${customer.fullName} photo",
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        loading = { InitialsFallback(customer, size, bgColor) },
        error = { InitialsFallback(customer, size, bgColor) }
    )
}

@Composable
private fun InitialsFallback(customer: Customer, size: androidx.compose.ui.unit.Dp, bgColor: Color) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = customer.initials,
            fontSize = (size.value / 2.4).sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun KycStatusBadge(status: KycStatus) {
    val (bg, fg, label) = when (status) {
        KycStatus.VERIFIED -> Triple(SuccessGreenSoft, SuccessGreen, "VERIFIED")
        KycStatus.PENDING -> Triple(WarningAmberSoft, WarningAmber, "PENDING")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(bg, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        if (status == KycStatus.VERIFIED) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(10.dp)
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(end = 2.dp))
        }
        Text(
            text = label,
            color = fg,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}