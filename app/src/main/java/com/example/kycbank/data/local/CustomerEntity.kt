package com.example.kycbank.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val kycStatus: String,
    val lastFetchedAt: Long,
    val avatarUrl: String,
    val selfiePath: String?,
    val dob: String,
    val phoneNo: String,
    val nationality: String,
    val balance: Double,
    val currency: String,
    val address: String,
    val ifsc: String,
    val accountNumber: String,
    val accountType: String
)