package com.example.kycbank.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "ifsc_cache")
data class IfscCacheEntity(
    @PrimaryKey val ifsc: String,
    val bankName: String,
    val branchName: String,
    val address: String,
    val fetchedAt: Long
)

@Dao
interface IfscCacheDao {
    @Query("SELECT * FROM ifsc_cache WHERE ifsc = :ifsc")
    suspend fun getCachedIfsc(ifsc: String): IfscCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIfsc(entity: IfscCacheEntity)
}