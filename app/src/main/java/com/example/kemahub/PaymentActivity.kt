package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)


        val btnPay = findViewById<Button>(R.id.btnPay)


        btnPay.setOnClickListener {
            val intent = Intent(this, DonorDashboardActivity::class.java)
            startActivity(intent)
        }
    }
}