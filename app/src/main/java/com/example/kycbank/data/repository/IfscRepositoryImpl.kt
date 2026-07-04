package com.example.kycbank.data.repository

import com.example.kycbank.data.remote.razorpay.IfscApi
import com.example.kycbank.domain.model.BankBranchInfo
import com.example.kycbank.domain.repository.IfscRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IfscRepositoryImpl @Inject constructor(
    private val api: IfscApi
) : IfscRepository {

    private val validIfscCodes = setOf(
        "HDFC0CAGSBK", "SBIN0000001", "ICIC0000001", "PUNB0244200", "UTIB0000001"
    )

    override fun isValidIfsc(ifsc: String): Boolean {
        return validIfscCodes.contains(ifsc)
    }

    override suspend fun getBranchInfo(ifsc: String): BankBranchInfo? {
        return try {
            val response = api.getBankDetails(ifsc)
            BankBranchInfo(
                bankName = response.bank,
                branchName = response.branch,
                address = response.address
            )
        } catch (e: Exception) {
            null
        }
    }
}