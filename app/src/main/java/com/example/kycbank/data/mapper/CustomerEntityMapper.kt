package com.example.kycbank.data.mapper

import com.example.kycbank.data.local.CustomerEntity
import com.example.kycbank.domain.model.AccountType
import com.example.kycbank.domain.model.Customer
import com.example.kycbank.domain.model.KycStatus

fun Customer.toEntity(): CustomerEntity {
    return CustomerEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        kycStatus = kycStatus.name,
        lastFetchedAt = System.currentTimeMillis(),
        avatarUrl = avatarUrl,
        selfiePath = selfiePath,
        dob = dob,
        phoneNo = phoneNo,
        nationality = nationality,
        balance = balance,
        currency = currency,
        address = address,
        ifsc = ifsc,
        accountNumber = accountNumber,
        accountType = accountType.name
    )
}

fun CustomerEntity.toDomain(): Customer {
    return Customer(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        kycStatus = KycStatus.valueOf(kycStatus),
        avatarUrl = avatarUrl,
        selfiePath = selfiePath,
        dob = dob,
        phoneNo = phoneNo,
        nationality = nationality,
        balance = balance,
        currency = currency,
        address = address,
        ifsc = ifsc,
        branchInfo = null,
        accountNumber = accountNumber,
        accountType = AccountType.valueOf(accountType)
    )
}