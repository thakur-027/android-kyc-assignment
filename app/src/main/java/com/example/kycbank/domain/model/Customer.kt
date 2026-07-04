package com.example.kycbank.domain.model

fun maskAccountNumber(iban: String): String {
    val lastFour = iban.takeLast(4)
    return "****$lastFour"
}

data class Customer(
    val avatarUrl: String,
    val selfiePath: String?,
    val id: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val phoneNo: String,
    val nationality: String,
    val email: String,
    val balance: Double,
    val currency: String,
    val address: String,
    val kycStatus: KycStatus,
    val branchInfo: BankBranchInfo?,
    val ifsc: String,
    val accountNumber: String,
    val accountType: AccountType
) {
    val fullName: String
        get() = "$firstName $lastName"

    val maskedAccountNumber: String
        get() = maskAccountNumber(accountNumber)

    val formattedBalance: String
        get() = "$currency %.2f".format(balance)
}