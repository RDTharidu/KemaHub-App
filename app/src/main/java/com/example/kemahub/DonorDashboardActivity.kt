package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class DonorDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_dashboard)

        findViewById<ImageView>(R.id.imgProfileTopRight)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnActiveDonation)?.setOnClickListener {
            startActivity(Intent(this, ActiveDonationActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnClaimedDonation)?.setOnClickListener {
            startActivity(Intent(this, ClaimedDonationActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnExpiredDonation)?.setOnClickListener {
            startActivity(Intent(this, ExpiredDonationActivity::class.java))
        }

        // Navigation Menu
        findViewById<ImageView>(R.id.navHistory)?.setOnClickListener {
            startActivity(Intent(this, DonationHistoryActivity::class.java))
        }

        findViewById<ImageView>(R.id.navNotifications)?.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        findViewById<ImageView>(R.id.navSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<ImageView>(R.id.fabAddDonation)?.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
        }
    }
}