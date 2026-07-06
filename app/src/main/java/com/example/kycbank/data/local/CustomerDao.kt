package com.example.kycbank.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<CustomerEntity>)

    @Query("UPDATE customers SET kycStatus = :status, selfiePath = :path WHERE id = :id")
    suspend fun updateKycStatus(id: String, status: String, path: String)

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: String): CustomerEntity?



    @Query("SELECT * FROM customers")
    suspend fun getAllCustomersOnce(): List<CustomerEntity>
}