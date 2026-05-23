package com.example.kemahub

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class BeneDonationDetailsActivity : AppCompatActivity() {

    private var donationId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bene_donation_details)

        // Navigation
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }
        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, BeneficiaryDashboardActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navHistory)?.setOnClickListener {
            startActivity(Intent(this, BeneMyClaimedDonationActivity::class.java))
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

        // View IDs අඳුරගැනීම
        val ivFoodImg = findViewById<ImageView>(R.id.ivDetailFoodImg)
        val tvFoodName = findViewById<TextView>(R.id.tvDetailFoodName)
        val tvFoodDesc = findViewById<TextView>(R.id.tvDetailFoodDesc)
        val etRequestQty = findViewById<EditText>(R.id.etRequestQty)
        val btnConfirmRequest = findViewById<TextView>(R.id.btnConfirmRequest)

        // අර කලින් පිටුවෙන් (Adapter එකෙන්) එවන දත්ත ගන්නවා
        donationId = intent.getStringExtra("id") ?: ""
        val foodName = intent.getStringExtra("name") ?: "Food Name"
        val qty = intent.getStringExtra("qty") ?: "0"
        val location = intent.getStringExtra("location") ?: "Unknown"
        val contact = intent.getStringExtra("contact") ?: "Unknown"
        val imageBase64 = intent.getStringExtra("image") ?: ""

        // දත්ත ටික ස්ක්‍රීන් එකේ පෙන්වනවා
        tvFoodName.text = foodName
        tvFoodDesc.text = "$qty Portions Available\nPickup Location - $location\nContact - $contact"

        // Base64 ෆොටෝ එක පෙන්වනවා
        if (imageBase64.isNotEmpty()) {
            try {
                val decodedString = Base64.decode(imageBase64, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                ivFoodImg.setImageBitmap(decodedByte)
            } catch (e: Exception) {
                ivFoodImg.setImageResource(R.drawable.logo)
            }
        }

        // Confirm Request බොත්තම එබුවම
        btnConfirmRequest.setOnClickListener {
            val requestedQty = etRequestQty.text.toString().trim()

            if (requestedQty.isEmpty()) {
                Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (donationId.isNotEmpty()) {
                val dbRef = FirebaseDatabase.getInstance().getReference("Donations").child(donationId)

                // ඩේටාබේස් එකේ Status එක "Requested" කියලා අප්ඩේට් කරනවා
                dbRef.child("status").setValue("Requested")
                    .addOnCompleteListener {
                        Toast.makeText(this, "Donation Requested Successfully!", Toast.LENGTH_LONG).show()

                        // කෑම එක ඉල්ලුවට පස්සේ කෙලින්ම එයාලගේ "My Claimed Donations" පිටුවට යවනවා
                        startActivity(Intent(this, BeneMyClaimedDonationActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Request failed. Try again.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}