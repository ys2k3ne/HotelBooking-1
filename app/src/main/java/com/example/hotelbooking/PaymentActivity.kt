package com.example.hotelbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Lấy dữ liệu về ngày nhận và trả phòng từ intent trước đó
        val checkInDate = intent.getStringExtra("checkInDate")
        val checkOutDate = intent.getStringExtra("checkOutDate")

        // Hiển thị thông tin ngày nhận và trả phòng lên layout
        val checkInTextView = findViewById<TextView>(R.id.checkInTextView)
        checkInTextView.text = checkInDate

        val checkOutTextView = findViewById<TextView>(R.id.checkOutTextView)
        checkOutTextView.text = checkOutDate

        // Xử lý sự kiện nút thanh toán
        val payButton = findViewById<Button>(R.id.payButton)
        payButton.setOnClickListener {
            // Thực hiện thanh toán và chuyển đến trang xác nhận thanh toán
            // (ví dụ: gửi yêu cầu thanh toán đến máy chủ, hiển thị thông báo thanh toán thành công, ...)
            val intent = Intent(this, PaymentConfirmationActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện nút quay lại
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Đóng trang thanh toán và quay lại trang trước đó
        }
    }
}
