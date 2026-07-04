package com.example.kycbank.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CustomerEntity::class], version = 1, exportSchema = false)
abstract class KycBankDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
}