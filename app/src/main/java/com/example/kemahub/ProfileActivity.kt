package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        // මැද තියෙන Donation History බොත්තම
        findViewById<TextView>(R.id.btnDonationHistory)?.setOnClickListener {
            startActivity(Intent(this, DonationHistoryActivity::class.java))
        }

        // Bottom Navigation බොත්තම්
        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, DonorDashboardActivity::class.java))
            finish()
        }

        // අලුතින් දැම්මේ - History එකට යන්න
        findViewById<ImageView>(R.id.navHistory)?.setOnClickListener {
            startActivity(Intent(this, DonationHistoryActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.navNotifications)?.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.navSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        // අලුතින් දැම්මේ - මැද තියෙන (+) බොත්තමෙන් Add Donation එකට යන්න
        findViewById<ImageView>(R.id.fabAddDonation)?.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
        }
    }
}