package com.example.kycbank.di

import com.example.kycbank.data.remote.dummyjson.DummyJsonApi
import com.example.kycbank.data.remote.razorpay.IfscApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DummyJsonRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RazorpayRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @DummyJsonRetrofit
    fun provideDummyJsonRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @RazorpayRetrofit
    fun provideRazorpayRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ifsc.razorpay.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDummyJsonApi(@DummyJsonRetrofit retrofit: Retrofit): DummyJsonApi {
        return retrofit.create(DummyJsonApi::class.java)
    }

    @Provides
    @Singleton
    fun provideIfscApi(@RazorpayRetrofit retrofit: Retrofit): IfscApi {
        return retrofit.create(IfscApi::class.java)
    }
}