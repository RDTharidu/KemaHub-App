package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BeneAvailableDonationActivity : AppCompatActivity() {

    private lateinit var rvAvailableDonations: RecyclerView
    private lateinit var donationList: ArrayList<DonationModel>
    private lateinit var beneFoodAdapter: BeneFoodAdapter
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bene_available_donation)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        // Navigation
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

        // RecyclerView Setup (ඔයාගේ ඩිසයින් එකේ වගේම තීරු 2කට පෙන්නනවා)
        rvAvailableDonations = findViewById(R.id.rvAvailableDonations)
        rvAvailableDonations.layoutManager = GridLayoutManager(this, 2)

        donationList = ArrayList()
        beneFoodAdapter = BeneFoodAdapter(donationList)
        rvAvailableDonations.adapter = beneFoodAdapter

        fetchAvailableDonations()
    }

    private fun fetchAvailableDonations() {
        dbRef = FirebaseDatabase.getInstance().getReference("Donations")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                donationList.clear()
                if (snapshot.exists()) {
                    for (donationSnap in snapshot.children) {
                        val donation = donationSnap.getValue(DonationModel::class.java)

                        // "Available" ඒවා විතරක් පෙන්නනවා!
                        if (donation != null && donation.status == "Available") {
                            donationList.add(donation)
                        }
                    }
                    beneFoodAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BeneAvailableDonationActivity, "Error loading donations", Toast.LENGTH_SHORT).show()
            }
        })
    }
}