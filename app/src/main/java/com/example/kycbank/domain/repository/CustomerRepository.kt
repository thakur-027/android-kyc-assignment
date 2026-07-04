package com.example.kycbank.domain.repository

import com.example.kycbank.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun getCustomers(): Flow<List<Customer>>

    suspend fun getCustomerById(id: String): Customer?

    suspend fun verifyCustomer(id: String, selfiePath: String): Boolean
}