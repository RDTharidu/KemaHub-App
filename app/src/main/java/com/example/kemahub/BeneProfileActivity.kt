package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BeneProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bene_profile)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        findViewById<TextView>(R.id.btnBeneSetting)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, BeneficiaryDashboardActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navNotifications)?.setOnClickListener {
            startActivity(Intent(this, BeneNotificationsActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }
}