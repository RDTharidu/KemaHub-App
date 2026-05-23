package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class BeneficiaryDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beneficiary_dashboard)


        findViewById<RelativeLayout>(R.id.btnAvailableDonation)?.setOnClickListener {
            startActivity(Intent(this, BeneAvailableDonationActivity::class.java))
        }
        findViewById<RelativeLayout>(R.id.btnMyClaimedDonation)?.setOnClickListener {
            startActivity(Intent(this, BeneMyClaimedDonationActivity::class.java))
        }
        findViewById<RelativeLayout>(R.id.btnUpcomingDetails)?.setOnClickListener {
            startActivity(Intent(this, BeneDonationDetailsActivity::class.java))
        }


        findViewById<ImageView>(R.id.imgBeneProfileTopRight)?.setOnClickListener {
            startActivity(Intent(this, BeneProfileActivity::class.java))
        }


        findViewById<ImageView>(R.id.navNotifications)?.setOnClickListener {
            startActivity(Intent(this, BeneNotificationsActivity::class.java))
        }
        findViewById<ImageView>(R.id.navSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}