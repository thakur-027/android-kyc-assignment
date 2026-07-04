package com.example.kycbank.data.mapper

import com.example.kycbank.data.remote.dummyjson.dto.UserDto
import com.example.kycbank.domain.model.Customer
import com.example.kycbank.domain.model.KycStatus
import kotlin.random.Random

private val ifscCodes = listOf(
    "HDFC0CAGSBK", "SBIN0000001", "ICIC0000001", "PUNB0244200", "UTIB0000001"
)

fun UserDto.toCustomer(): Customer {
    return Customer(
        avatarUrl = image,
        selfiePath = null,
        id = id.toString(),
        firstName = firstName,
        lastName = lastName,
        dob = birthDate,
        phoneNo = phone,
        nationality = address.country,
        email = email,
        balance = Random.nextDouble(1000.0, 100000.0),
        currency = bank.currency,
        address = address.address,
        kycStatus = KycStatus.PENDING,
        branchInfo = null,
        ifsc = ifscCodes.random(),
        accountNumber = bank.iban
    )
}