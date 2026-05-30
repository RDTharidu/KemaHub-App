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

class ExpiredDonationActivity : AppCompatActivity() {

    private lateinit var rvExpiredDonations: RecyclerView
    private lateinit var foodList: ArrayList<FoodItem>
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expired_donation)


        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }


        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, DonorDashboardActivity::class.java))
            finish()
        }
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
        findViewById<ImageView>(R.id.fabAddDonation)?.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
        }


        rvExpiredDonations = findViewById(R.id.rvExpiredDonations)
        rvExpiredDonations.layoutManager = GridLayoutManager(this, 2)

        foodList = ArrayList()

        historyAdapter = HistoryAdapter(foodList)
        rvExpiredDonations.adapter = historyAdapter

        fetchExpiredDonations()
    }

    private fun fetchExpiredDonations() {
        dbRef = FirebaseDatabase.getInstance().getReference("Donations")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodList.clear()
                if (snapshot.exists()) {
                    for (donationSnap in snapshot.children) {
                        val status = donationSnap.child("status").getValue(String::class.java) ?: ""


                        if (status == "Expired") {
                            val foodName = donationSnap.child("foodName").getValue(String::class.java) ?: "Unknown"
                            val location = donationSnap.child("pickupLocation").getValue(String::class.java) ?: ""
                            val qty = donationSnap.child("quantity").getValue(String::class.java) ?: "1"
                            val cat = donationSnap.child("category").getValue(String::class.java) ?: ""

                            val item = FoodItem(
                                id = donationSnap.key ?: "",
                                name = foodName,
                                description = "Loc: $location",
                                quantity = "$qty Portions",
                                category = cat
                            )
                            foodList.add(item)
                        }
                    }
                    historyAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExpiredDonationActivity, "Error loading expired data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}