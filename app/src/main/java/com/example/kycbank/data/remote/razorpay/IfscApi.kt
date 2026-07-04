package com.example.kycbank.data.remote.razorpay

import com.example.kycbank.data.remote.razorpay.dto.IfscDto
import retrofit2.http.GET
import retrofit2.http.Path

interface IfscApi {
    @GET("{ifsc}")
    suspend fun getBankDetails(
        @Path("ifsc") ifsc: String
    ): IfscDto
}