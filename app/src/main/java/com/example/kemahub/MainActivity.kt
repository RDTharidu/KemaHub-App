package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDonate = findViewById<Button>(R.id.btnDonateFood)
        val btnFind = findViewById<Button>(R.id.btnFindFood)
        val btnLogout = findViewById<Button>(R.id.btnLogout)


        btnDonate.setOnClickListener {
            val intent = Intent(this, DonorDashboardActivity::class.java)
            startActivity(intent)
        }


        btnFind.setOnClickListener {
            val intent = Intent(this, BeneficiaryDashboardActivity::class.java)
            startActivity(intent)
        }


        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}