package com.example.hotelbooking

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hotelbooking.databinding.ActivityDetailBinding
import com.example.hotelbooking.databinding.ActivityEditHotelBinding
import com.example.hotelbooking.models.BookingInfo
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

class EditHotelActivity : AppCompatActivity() {
    var imageUrl = ""
    private lateinit var binding: ActivityEditHotelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            val hotelAdd = bundle.getString("Address")
            binding.detailDesc.text = "Địa chỉ : $hotelAdd"

            binding.detailTitle.text = bundle.getString("Hotel Name")

            val hotelPrice = bundle.getString("Hotel Price")
            binding.detailPriority.text = "Giá tiền: $hotelPrice VND"

            imageUrl = bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image"))
                .into(binding.detailImage)

            val hotelRating = bundle.getFloat("Hotel Rating")
            binding.hotelRatingBar.rating = hotelRating

            val hotelDescription = bundle.getString("description")
            binding.motaHotel.text = hotelDescription

            val hotelAvailable = bundle.getBoolean("available")
            if (hotelAvailable) {
                binding.hotelStatus.text = "Trạng thái: available"
            } else {
                binding.hotelStatus.text = "Trạng thái: not available"
            }
        }
    }
}
