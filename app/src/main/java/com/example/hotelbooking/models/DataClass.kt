package com.example.hotelbooking.models

class DataClass {
    var hotelID: String = ""
    var hotelName: String = ""
    var hotelAddress: String = ""
    var hotelPrice: String = ""
    var hotelRating: Float = 4f
    var hotelImage: String = ""
    var description: String = ""
    var available: Boolean = false

    constructor()

    constructor(
        hotelID: String,
        hotelName: String,
        hotelAddress: String,
        hotelPrice: String,
        hotelRating: Float,
        hotelImage: String,
        description: String,
        available: Boolean
    ) {
        this.hotelID = hotelID
        this.hotelName = hotelName
        this.hotelAddress = hotelAddress
        this.hotelPrice = hotelPrice
        this.hotelRating = hotelRating
        this.hotelImage = hotelImage
        this.description = description
        this.available = available
    }
}
