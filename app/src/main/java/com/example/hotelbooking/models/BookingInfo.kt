package com.example.hotelbooking.models

data class BookingInfo(
    val hotelName: String,
    val checkInDate: String,
    val checkOutDate: String,
    val numOfGuests: Int,
    val hotelPrice: String
)