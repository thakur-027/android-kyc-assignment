package com.example.kycbank.domain.repository

import com.example.kycbank.domain.model.BankBranchInfo

interface IfscRepository {
    suspend fun getBranchInfo(ifsc: String): BankBranchInfo?
    fun isValidIfsc(ifsc: String): Boolean
}