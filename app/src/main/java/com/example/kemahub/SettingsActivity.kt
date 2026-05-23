package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Back බොත්තම
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        // Navigation බොත්තම්
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

        // අලුතින් දැම්මේ - (+) බොත්තමෙන් Add Donation එකට යන්න
        findViewById<ImageView>(R.id.fabAddDonation)?.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
            // මෙතන finish() දාන්නේ නෑ, මොකද කෑමක් දාලා ආපහු Back වුණොත් Settings එකටම එන්න පුළුවන් වෙන්න.
        }
    }
}