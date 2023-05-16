package com.example.hotelbooking

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hotelbooking.databinding.ActivityEditHotelBinding
import com.example.hotelbooking.models.DataClass
import com.google.firebase.database.FirebaseDatabase

class EditHotelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditHotelBinding
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val hotelId = bundle!!.getString("HotelId") ?: ""
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

        binding.update.setOnClickListener {
            openUpdateDialog(
                hotelId,
                DataClass(
                    bundle!!.getString("HotelId")!!,
                    bundle.getString("Hotel Name")!!,
                    bundle.getString("Address")!!,
                    bundle.getString("Hotel Price")!!,
                    bundle.getFloat("Hotel Rating"),
                    bundle.getString("Image")!!,
                    bundle.getString("description")!!,
                    bundle.getBoolean("available")
                )
            )
        }

        binding.delete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa khách sạn?")
                .setPositiveButton("Có") { _, _ ->
                    deleteHotelData(hotelId)
                }
                .setNegativeButton("Không", null)
                .create()
            alertDialog.show()
        }
    }

    private fun deleteHotelData(hotelId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("hotels").child(hotelId)
        ref.removeValue()
            .addOnSuccessListener {
                // Handle success here
                finish()
            }
            .addOnFailureListener {
                // Handle errors here
            }
    }



    private fun openUpdateDialog(hotelID: String, hotel: DataClass?) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)
        mDialog.setView(mDialogView)

        val etName = mDialogView.findViewById<EditText>(R.id.hotel_name_edit_text)
        val etAddress = mDialogView.findViewById<EditText>(R.id.hotel_address_edit_text)
        val etPrice = mDialogView.findViewById<EditText>(R.id.hotel_price_edit_text)
        val etDescription = mDialogView.findViewById<EditText>(R.id.hotel_description_edit_text)
        val swStatus = mDialogView.findViewById<Switch>(R.id.hotel_status_switch)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etName.setText(hotel?.hotelName)
        etAddress.setText(hotel?.hotelAddress)
        etPrice.setText(hotel?.hotelPrice)
        etDescription.setText(hotel?.description)
        swStatus.isChecked = hotel?.available ?: false

        mDialog.setTitle("Update...")
        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            val status = if (swStatus.isChecked) {
                "available"
            } else {
                "not available"
            }
            updateHotelData(
                hotelID,
                etName.text.toString(),
                etAddress.text.toString(),
                etPrice.text.toString(),
                etDescription.text.toString(),
                status
            )
            alertDialog.dismiss() // Đóng dialog
        }

    }

    private fun updateHotelData(
        hotelID: String,
        name: String,
        address: String,
        price: String,
        description: String,
        status: String
    ) {
        val ref = FirebaseDatabase.getInstance().getReference("hotels").child(hotelID)

        val newData = DataClass(
            hotelID,
            name,
            address,
            price,
            binding.hotelRatingBar.rating,
            imageUrl,
            description,
            status == "available"
        )

        ref.setValue(newData)
            .addOnSuccessListener {
                // Handle success here
            }
            .addOnFailureListener {
                // Handle errors here
            }
    }
}

