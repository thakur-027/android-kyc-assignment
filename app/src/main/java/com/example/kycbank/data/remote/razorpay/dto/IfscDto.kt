package com.example.kycbank.data.remote.razorpay.dto
import com.google.gson.annotations.SerializedName

data class IfscDto(
    @SerializedName("BANK")
    val bank: String,
    @SerializedName("BRANCH")
    val branch: String,
    @SerializedName("ADDRESS")
    val address: String,
)