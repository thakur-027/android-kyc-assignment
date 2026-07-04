package com.example.kycbank.data.repository

import com.example.kycbank.data.local.IfscCacheDao
import com.example.kycbank.data.local.IfscCacheEntity
import com.example.kycbank.data.remote.razorpay.IfscApi
import com.example.kycbank.domain.model.BankBranchInfo
import com.example.kycbank.domain.repository.IfscRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val IFSC_CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L

@Singleton
class IfscRepositoryImpl @Inject constructor(
    private val api: IfscApi,
    private val cacheDao: IfscCacheDao
) : IfscRepository {

    private val validIfscCodes = setOf(
        "HDFC0CAGSBK", "SBIN0000001", "ICIC0000001", "PUNB0244200", "UTIB0000001"
    )

    override fun isValidIfsc(ifsc: String): Boolean {
        return validIfscCodes.contains(ifsc)
    }

    override suspend fun getBranchInfo(ifsc: String): BankBranchInfo? {
        val cached = cacheDao.getCachedIfsc(ifsc)
        val isFresh = cached != null &&
                (System.currentTimeMillis() - cached.fetchedAt) < IFSC_CACHE_EXPIRY_MS

        if (isFresh && cached != null) {
            return BankBranchInfo(
                bankName = cached.bankName,
                branchName = cached.branchName,
                address = cached.address
            )
        }

        return try {
            val response = api.getBankDetails(ifsc)
            cacheDao.insertIfsc(
                IfscCacheEntity(
                    ifsc = ifsc,
                    bankName = response.bank,
                    branchName = response.branch,
                    address = response.address,
                    fetchedAt = System.currentTimeMillis()
                )
            )
            BankBranchInfo(
                bankName = response.bank,
                branchName = response.branch,
                address = response.address
            )
        } catch (e: Exception) {

            cached?.let {
                BankBranchInfo(
                    bankName = it.bankName,
                    branchName = it.branchName,
                    address = it.address
                )
            }
        }
    }
}