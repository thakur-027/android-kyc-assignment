package com.example.kycbank.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CustomerEntity::class, IfscCacheEntity::class],
    version = 2,
    exportSchema = false
)
abstract class KycBankDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun ifscCacheDao(): IfscCacheDao
}