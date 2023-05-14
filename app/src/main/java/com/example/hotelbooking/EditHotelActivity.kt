package com.example.hotelbooking

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.bumptech.glide.Glide
import com.example.hotelbooking.databinding.ActivityDetailBinding
import com.example.hotelbooking.databinding.ActivityEditHotelBinding
import com.example.hotelbooking.databinding.UpdateDialogBinding
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
        val hotelId = bundle!!.getString("hotel_id") ?: ""
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
//        binding.update.setOnClickListener {
//            val hotelName = binding.detailTitle.text.toString()
//            openUpdateDialog(hotelName, hotelId)
//        }
    }

//    private fun openUpdateDialog(hotelName: String, hotelId: String) {
//        // Khởi tạo AlertDialog
//        val builder = AlertDialog.Builder(this)
//        val dialogBinding = UpdateDialogBinding.inflate(LayoutInflater.from(this))
//        builder.setView(dialogBinding.root)
//
//        // Ánh xạ các thành phần giao diện trong layout
//        val nameEditText = dialogBinding.hotelNameEditText
//        val priceEditText = dialogBinding.hotelPriceEditText
//        val addressEditText = dialogBinding.hotelAddressEditText
//        val descriptionEditText = dialogBinding.hotelDescriptionEditText
//        val statusSwitch = dialogBinding.hotelStatusSwitch
//        val cancelBtn = dialogBinding.btnCancel
//        val saveBtn = dialogBinding.btnUpdateData
//
//        // Set giá trị cho các EditText và SwitchCompat
//        nameEditText.setText(hotelName)
//
//        // Thiết lập nút "Lưu" trong hộp thoại
//        builder.setPositiveButton("Lưu") { dialog, which ->
//            // Lấy giá trị từ các EditText và SwitchCompat
//            val name = nameEditText.text.toString()
//            val price = priceEditText.text.toString()
//            val address = addressEditText.text.toString()
//            val description = descriptionEditText.text.toString()
//            val available = statusSwitch.isChecked
//
//            // Cập nhật thông tin khách sạn trong database
//            val ref = FirebaseDatabase.getInstance().getReference("hotels").child(hotelId)
//            val map = mutableMapOf<String, Any>()
//            map["Hotel Name"] = name
//            map["Hotel Price"] = price
//            map["Address"] = address
//            map["description"] = description
//            map["available"] = available
//            ref.updateChildren(map)
//
//            // Cập nhật lại giá trị trong TextView của Activity
//            binding.detailTitle.text = name
//            binding.detailPriority.text = "Giá tiền: $price VND"
//            binding.detailDesc.text = "Địa chỉ : $address"
//            binding.motaHotel.text = description
//            if (available) {
//                binding.hotelStatus.text = "Trạng thái: available"
//            } else {
//                binding.hotelStatus.text = "Trạng thái: not available"
//            }
//        }
//
//        // Thiết lập nút "Hủy" trong hộp thoại
//        builder.setNegativeButton("Hủy") { dialog, which ->
//            dialog.dismiss()
//        }
//
//        // Hiển thị hộp thoại
//        val dialog = builder.create()
//        dialog.show()
//    }

}

