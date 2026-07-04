package com.example.kycbank.data.repository

import com.example.kycbank.data.local.CustomerDao
import com.example.kycbank.data.local.CustomerEntity
import com.example.kycbank.data.mapper.toCustomer
import com.example.kycbank.data.mapper.toDomain
import com.example.kycbank.data.mapper.toEntity
import com.example.kycbank.data.remote.dummyjson.DummyJsonApi
import com.example.kycbank.domain.model.Customer
import com.example.kycbank.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val CACHE_EXPIRY_MS = 10 * 60 * 1000L // 10 minutes

@Singleton
class CustomerRepositoryImpl @Inject constructor(
    private val api: DummyJsonApi,
    private val dao: CustomerDao
) : CustomerRepository {

    override suspend fun getCustomers(): Flow<List<Customer>> {
        refreshIfStale()
        return dao.getAllCustomers().map { entities -> entities.map { it.toDomain() } }
    }

    private suspend fun refreshIfStale() {
        val cached = dao.getAllCustomersOnce()
        val currentTime = System.currentTimeMillis()
        val lastFetched = cached.firstOrNull()?.lastFetchedAt ?: 0L

        if (cached.isEmpty() || currentTime - lastFetched > CACHE_EXPIRY_MS) {
            val existingById = cached.associateBy { it.id }
            val remoteCustomers = api.getUsers().users

            val mergedEntities = remoteCustomers.map { dto ->
                val freshEntity = dto.toCustomer().toEntity()
                val existing = existingById[freshEntity.id]

                if (existing != null) {
                    // Preserve locally-owned KYC state; take everything else fresh from network
                    freshEntity.copy(
                        kycStatus = existing.kycStatus,
                        selfiePath = existing.selfiePath
                    )
                } else {
                    freshEntity
                }
            }

            dao.insertCustomers(mergedEntities)
        }
    }

    override suspend fun getCustomerById(id: String): Customer? {
        refreshIfStale()
        return dao.getCustomerById(id)?.toDomain()
    }

    override suspend fun verifyCustomer(id: String, selfiePath: String): Boolean {
        dao.updateKycStatus(id, "VERIFIED", selfiePath)
        return true
    }
}