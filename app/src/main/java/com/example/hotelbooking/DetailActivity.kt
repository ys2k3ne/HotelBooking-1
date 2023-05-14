package com.example.hotelbooking

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hotelbooking.databinding.ActivityDetailBinding
import com.example.hotelbooking.models.BookingInfo
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

private lateinit var binding: ActivityDetailBinding
class DetailActivity : AppCompatActivity() {
    var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
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
        // Đăng ký sự kiện cho nút Đặt phòng
        binding.bookButton.setOnClickListener { bookRoom() }
    }

    fun bookRoom() {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            // Lưu lại ngày nhận và trả phòng
            val checkInDate = LocalDate.of(year, month + 1, dayOfMonth)
            val checkOutDatePickerDialog = DatePickerDialog(this)
            checkOutDatePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                val checkOutDate = LocalDate.of(year, month + 1, dayOfMonth)
                // Tạo một đối tượng BookingInfo để lưu thông tin đặt phòng
                val bookingInfo = BookingInfo(
                    hotelName = binding.detailTitle.text.toString(),
                    checkInDate = checkInDate.toString(),
                    checkOutDate = checkOutDate.toString(),
                    numOfGuests = 1, // Số khách lưu cứng là 1 trong ví dụ này
                    hotelPrice = binding.detailPriority.text.toString() // Tính tổng giá tiền ở đây
                )

                // Lưu thông tin đặt phòng vào Firebase
                saveBookingInfo(bookingInfo)

                // Chuyển đến màn hình thanh toán
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("checkInDate", checkInDate.toString())
                intent.putExtra("checkOutDate", checkOutDate.toString())
                startActivity(intent)
            }
            checkOutDatePickerDialog.datePicker.minDate = checkInDate.toEpochDay() * 24 * 60 * 60 * 1000 // Set ngày tối thiểu là ngày nhận phòng
            checkOutDatePickerDialog.show()
        }
        datePickerDialog.show()
    }


    fun saveBookingInfo(bookingInfo: BookingInfo) {
        // Khởi tạo một instance của Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().reference

        // Tạo một key ngẫu nhiên để lưu dữ liệu vào Firebase
        val bookingId = database.child("bookings").push().key

        // Tạo một hashmap để lưu thông tin đặt phòng
        val bookingMap = HashMap<String, Any>()
        bookingMap["hotelName"] = bookingInfo.hotelName
        bookingMap["checkInDate"] = bookingInfo.checkInDate
        bookingMap["checkOutDate"] = bookingInfo.checkOutDate
        bookingMap["numOfGuests"] = bookingInfo.numOfGuests
        bookingMap["hotelPrice"] = bookingInfo.hotelPrice

        // Thêm thông tin đặt phòng vào cơ sở dữ liệu Firebase
        if (bookingId != null) {
            database.child("bookings").child(bookingId).setValue(bookingMap)
                .addOnSuccessListener {
                    // Xử lý sau khi lưu dữ liệu thành công
                }
                .addOnFailureListener {
                    // Xử lý sau khi lưu dữ liệu thất bại
                }
        }
    }

}