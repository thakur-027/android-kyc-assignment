package com.example.kycbank.domain.usecase

import com.example.kycbank.domain.repository.CustomerRepository
import javax.inject.Inject

class CompleteKycUseCase @Inject constructor(
    private val repository: CustomerRepository
) {
    suspend operator fun invoke(customerId: String, selfiePath: String): Boolean =
        repository.verifyCustomer(customerId, selfiePath)
}