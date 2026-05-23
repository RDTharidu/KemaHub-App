package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class BeneNotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bene_notifications)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, BeneficiaryDashboardActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }
}