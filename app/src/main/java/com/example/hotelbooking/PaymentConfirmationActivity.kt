package com.example.hotelbooking

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PaymentConfirmationActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_confirmation)

        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        // Xử lý sự kiện nút quay lại
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Đóng trang xác nhận thanh toán và quay lại trang trước đó
        }

        // Lấy thông tin thanh toán từ intent
        val paymentAmount = intent.getDoubleExtra("paymentAmount", 0.0)
        val paymentMethod = intent.getStringExtra("paymentMethod")

        // Hiển thị thông tin thanh toán lên layout
        val paymentAmountTextView = findViewById<TextView>(R.id.paymentAmountTextView)
        paymentAmountTextView.text = getString(R.string.payment_amount_text, paymentAmount)

        val paymentMethodTextView = findViewById<TextView>(R.id.paymentMethodTextView)
        paymentMethodTextView.text = getString(R.string.payment_method_text, paymentMethod)

        // Lưu thông tin thanh toán vào cơ sở dữ liệu
        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            val paymentInfo = hashMapOf(
                "amount" to paymentAmount,
                "method" to paymentMethod
            )
            mFirestore.collection("users").document(userId).collection("payments")
                .add(paymentInfo)
                .addOnSuccessListener { documentReference ->
                    // Thành công, không cần làm gì cả
                }
                .addOnFailureListener { e ->
                    // Xảy ra lỗi, thông báo lỗi lên màn hình
                    val errorTextView = findViewById<TextView>(R.id.errorTextView)
                    errorTextView.text = getString(R.string.payment_confirmation_error_message, e.message)
                }
        }
    }
}

