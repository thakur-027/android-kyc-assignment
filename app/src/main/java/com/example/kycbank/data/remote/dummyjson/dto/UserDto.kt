package com.example.kycbank.data.remote.dummyjson.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val image: String,
    val address: AddressDto,
    val bank: BankDto
)

data class AddressDto(
    val address: String,
    val city: String,
    val state: String,
    val country: String
)

data class BankDto(
    val iban: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String
)

data class UsersResponseDto(
    val users: List<UserDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)