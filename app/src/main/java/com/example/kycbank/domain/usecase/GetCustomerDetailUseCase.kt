package com.example.kycbank.domain.usecase

import com.example.kycbank.domain.model.Customer
import com.example.kycbank.domain.repository.CustomerRepository
import javax.inject.Inject

class GetCustomerDetailUseCase @Inject constructor(
    private val repository: CustomerRepository
) {
    suspend operator fun invoke(customerId: String): Customer? = repository.getCustomerById(customerId)
}