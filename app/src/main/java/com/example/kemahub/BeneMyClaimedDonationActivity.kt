package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BeneMyClaimedDonationActivity : AppCompatActivity() {

    private lateinit var rvBeneClaimedDonations: RecyclerView
    private lateinit var donationList: ArrayList<DonationModel>
    private lateinit var beneClaimedAdapter: BeneClaimedAdapter
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bene_my_claimed_donation)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        // Navigation
        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, BeneficiaryDashboardActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navHistory)?.setOnClickListener {
            // දැනටමත් ඉන්නේ මේ පිටුවේ නිසා මුකුත් කරන්නේ නෑ, නැත්නම් Refresh කරන්න පුළුවන්
        }
        findViewById<ImageView>(R.id.navNotifications)?.setOnClickListener {
            startActivity(Intent(this, BeneNotificationsActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        // Setup RecyclerView (මේක තීරු 2ක් නෙමෙයි, සම්පූර්ණ පළලට පේන්න ඕන නිසා LinearLayoutManager පාවිච්චි කරන්නේ)
        rvBeneClaimedDonations = findViewById(R.id.rvBeneClaimedDonations)
        rvBeneClaimedDonations.layoutManager = LinearLayoutManager(this)

        donationList = ArrayList()
        beneClaimedAdapter = BeneClaimedAdapter(donationList)
        rvBeneClaimedDonations.adapter = beneClaimedAdapter

        fetchMyClaimedDonations()
    }

    private fun fetchMyClaimedDonations() {
        dbRef = FirebaseDatabase.getInstance().getReference("Donations")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                donationList.clear()
                if (snapshot.exists()) {
                    for (donationSnap in snapshot.children) {
                        val donation = donationSnap.getValue(DonationModel::class.java)

                        // කවුරුහරි Request කරපු ඒවා පෙන්වනවා (status == "Requested")
                        if (donation != null && donation.status == "Requested") {
                            donationList.add(donation)
                        }
                    }
                    beneClaimedAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BeneMyClaimedDonationActivity, "Error loading donations", Toast.LENGTH_SHORT).show()
            }
        })
    }
}