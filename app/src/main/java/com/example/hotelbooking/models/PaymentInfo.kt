package com.example.hotelbooking.models

data class PaymentInfo(
    val cardNumber: String,
    val cardExpirationDate: String,
    val cardCvv: String,
    val checkInDate: String,
    val checkOutDate: String,
    val totalAmount: Double
)
