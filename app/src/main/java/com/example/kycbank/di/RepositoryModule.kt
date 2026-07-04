package com.example.kycbank.di

import com.example.kycbank.data.repository.CustomerRepositoryImpl
import com.example.kycbank.data.repository.IfscRepositoryImpl
import com.example.kycbank.domain.repository.CustomerRepository
import com.example.kycbank.domain.repository.IfscRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        impl: CustomerRepositoryImpl
    ): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindIfscRepository(
        impl: IfscRepositoryImpl
    ): IfscRepository
}