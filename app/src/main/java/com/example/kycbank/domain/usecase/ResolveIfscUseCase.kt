package com.example.kycbank.domain.usecase

import com.example.kycbank.domain.model.BankBranchInfo
import com.example.kycbank.domain.repository.IfscRepository
import javax.inject.Inject

class ResolveIfscUseCase @Inject constructor(
    private val repository: IfscRepository
) {
    suspend operator fun invoke(ifsc: String): BankBranchInfo? = repository.getBranchInfo(ifsc)
}