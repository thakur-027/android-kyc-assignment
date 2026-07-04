package com.example.kycbank.data.remote.dummyjson

import com.example.kycbank.data.remote.dummyjson.dto.UsersResponseDto
import retrofit2.http.GET

interface DummyJsonApi {
    @GET("users")
    suspend fun getUsers(): UsersResponseDto
}