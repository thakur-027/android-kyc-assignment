package com.example.kycbank.domain.usecase

import com.example.kycbank.domain.model.AccountType
import com.example.kycbank.domain.model.Customer
import com.example.kycbank.domain.model.KycStatus
import javax.inject.Inject

class SearchCustomersUseCase @Inject constructor() {
    operator fun invoke(
        customers: List<Customer>,
        tab: KycStatus,
        query: String,
        accountType: AccountType?
    ): List<Customer> {
        return customers.filter { customer ->
            val matchesTab = customer.kycStatus == tab
            val matchesSearch = query.isBlank() ||
                    customer.fullName.contains(query, ignoreCase = true) ||
                    customer.maskedAccountNumber.contains(query, ignoreCase = true)
            val matchesType = accountType == null || customer.accountType == accountType

            matchesTab && matchesSearch && matchesType
        }
    }
}