package com.example.kycbank.di

import android.content.Context
import androidx.room.Room
import com.example.kycbank.data.local.CustomerDao
import com.example.kycbank.data.local.IfscCacheDao
import com.example.kycbank.data.local.KycBankDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KycBankDatabase {
        return Room.databaseBuilder(
            context,
            KycBankDatabase::class.java,
            "kycbank.db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideCustomerDao(database: KycBankDatabase): CustomerDao {
        return database.customerDao()
    }

    @Provides
    @Singleton
    fun provideIfscCacheDao(database: KycBankDatabase): IfscCacheDao {
        return database.ifscCacheDao()
    }
}