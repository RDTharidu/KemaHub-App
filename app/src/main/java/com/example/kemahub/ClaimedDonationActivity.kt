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

class ClaimedDonationActivity : AppCompatActivity() {

    private lateinit var rvClaimedDonations: RecyclerView
    private lateinit var foodList: ArrayList<FoodItem>
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claimed_donation)

        // Navigation
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }
        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, DonorDashboardActivity::class.java))
            finish()
        }

        // Setup RecyclerView
        rvClaimedDonations = findViewById(R.id.rvClaimedDonations)
        rvClaimedDonations.layoutManager = GridLayoutManager(this, 2)

        foodList = ArrayList()
        foodAdapter = FoodAdapter(foodList)
        rvClaimedDonations.adapter = foodAdapter

        fetchClaimedDonations()
    }

    private fun fetchClaimedDonations() {
        dbRef = FirebaseDatabase.getInstance().getReference("Donations")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodList.clear()
                if (snapshot.exists()) {
                    for (donationSnap in snapshot.children) {
                        val status = donationSnap.child("status").getValue(String::class.java) ?: ""

                        if (status == "Requested") {
                            val foodName = donationSnap.child("foodName").getValue(String::class.java) ?: ""
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
                    foodAdapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ClaimedDonationActivity, "Error loading claimed data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}