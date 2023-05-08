package com.example.hotelbooking

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hotelbooking.databinding.ActivityPaymentBinding
import com.example.hotelbooking.models.PaymentInfo
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var checkInDate: String? = null
    private var checkOutDate: String? = null
    private var hotelPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get booking information from intent extras
        val extras = intent.extras
        if (extras != null) {
            checkInDate = extras.getString("checkInDate")
            checkOutDate = extras.getString("checkOutDate")
            hotelPrice = extras.getString("hotelPrice")

            // Display booking information on the UI
            binding.checkInDate.text = checkInDate
            binding.checkOutDate.text = checkOutDate
            binding.hotelPrice.text = hotelPrice
            // Convert EditText to TextView
            val totalAmountTextView = binding.totalAmount as TextView

// Set the total amount to pay
            totalAmountTextView.text = calculateTotalAmount().toString()
        }

        // Set event listener for the Confirm Payment button
        binding.confirmPaymentButton.setOnClickListener { confirmPayment() }
    }

    // Calculate the total amount to pay
    private fun calculateTotalAmount(): Int {
        val numOfDays = getNumOfDays(checkInDate ?: "", checkOutDate ?: "")
        val price = hotelPrice?.replace("[^\\d]".toRegex(), "")?.toInt() ?: 0
        return numOfDays * price
    }

    // Save payment information to Firebase database
    private fun confirmPayment() {
        // Get payment information from UI
        val cardNumber = binding.cardNumber.text.toString()
        val cardHolderName = binding.cardHolderName.text.toString()
        val cardExpirationDate = binding.cardExpirationDate.text.toString()
        val cardCvv = binding.cardCvv.text.toString()

        // Validate payment information
        if (cardNumber.isEmpty() || cardHolderName.isEmpty() || cardExpirationDate.isEmpty() || cardCvv.isEmpty()) {
            Toast.makeText(this, "Please enter all payment information", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a PaymentInfo object to store payment information
        val paymentInfo = PaymentInfo(
            cardNumber = cardNumber,
            cardHolderName = cardHolderName,
            cardExpirationDate = cardExpirationDate,
            cardCvv = cardCvv,
            checkInDate = checkInDate!!,
            checkOutDate = checkOutDate!!,
            totalAmount = calculateTotalAmount()
        )

        // Save payment information to Firebase database
        val database = FirebaseDatabase.getInstance()
        val paymentRef = database.getReference("payments").push()
        paymentRef.setValue(paymentInfo)

        // Go to PaymentResultActivity
        val intent = Intent(this, PaymentResultActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Calculate the number of days between check-in and check-out dates
    private fun getNumOfDays(checkInDate: String, checkOutDate: String): Int {
        // Define the date format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Parse the check-in and check-out dates
        val checkIn = dateFormat.parse(checkInDate)
        val checkOut = dateFormat.parse(checkOutDate)

        // Calculate the number of days
        val diffInMs = checkOut.time - checkIn.time
        val diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS).toInt()

        return diffInDays
    }
}
