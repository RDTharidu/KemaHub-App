package com.example.kemahub

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DonationHistoryActivity : AppCompatActivity() {

    private lateinit var rvHistoryDonations: RecyclerView
    private lateinit var historyList: ArrayList<FoodItem>
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var dbRef: DatabaseReference

    // Selection UI අඳුරගැනීම
    private lateinit var llNormalHeader: LinearLayout
    private lateinit var llSelectionHeader: LinearLayout
    private lateinit var tvSelectionCount: TextView
    private lateinit var ivDeleteSelected: ImageView
    private lateinit var ivCloseSelection: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_history)

        // Navigation
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }
        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, DonorDashboardActivity::class.java))
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

        // Headers
        llNormalHeader = findViewById(R.id.llNormalHeader)
        llSelectionHeader = findViewById(R.id.llSelectionHeader)
        tvSelectionCount = findViewById(R.id.tvSelectionCount)
        ivDeleteSelected = findViewById(R.id.ivDeleteSelected)
        ivCloseSelection = findViewById(R.id.ivCloseSelection)

        // Setup RecyclerView
        rvHistoryDonations = findViewById(R.id.rvHistoryDonations)
        rvHistoryDonations.layoutManager = GridLayoutManager(this, 2)
        historyList = ArrayList()

        historyAdapter = HistoryAdapter(historyList, object : HistoryAdapter.SelectionCallbacks {
            override fun onSelectionModeChanged(isInSelectionMode: Boolean) {
                if (isInSelectionMode) {
                    llNormalHeader.visibility = View.GONE
                    llSelectionHeader.visibility = View.VISIBLE
                } else {
                    llNormalHeader.visibility = View.VISIBLE
                    llSelectionHeader.visibility = View.GONE
                }
            }

            override fun onSelectionCountChanged(count: Int) {
                tvSelectionCount.text = "$count Selected"
            }
        })

        rvHistoryDonations.adapter = historyAdapter

        // සිලෙක්ට් කරපුවා මකන බොත්තම
        ivDeleteSelected.setOnClickListener {
            val selectedCount = historyAdapter.getSelectedCount()
            if (selectedCount > 0) {
                showDeleteConfirmationDialog(selectedCount)
            }
        }

        // සිලෙක්ට් Mode එක වහන බොත්තම
        ivCloseSelection.setOnClickListener {
            historyAdapter.clearSelection()
        }

        fetchAllDonations()
    }

    private fun showDeleteConfirmationDialog(count: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Donations")
            .setMessage("Are you sure you want to delete these $count selected donations from your history?")
            .setPositiveButton("Yes, Delete") { _, _ ->
                historyAdapter.deleteSelectedItems()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun fetchAllDonations() {
        dbRef = FirebaseDatabase.getInstance().getReference("Donations")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyList.clear()
                if (snapshot.exists()) {
                    for (donationSnap in snapshot.children) {
                        val foodName = donationSnap.child("foodName").getValue(String::class.java) ?: "Unknown"
                        val location = donationSnap.child("pickupLocation").getValue(String::class.java) ?: ""
                        val status = donationSnap.child("status").getValue(String::class.java) ?: "Unknown"
                        val qty = donationSnap.child("quantity").getValue(String::class.java) ?: "1"
                        val cat = donationSnap.child("category").getValue(String::class.java) ?: ""

                        val item = FoodItem(
                            id = donationSnap.key ?: "",
                            name = foodName,
                            description = "Status: $status\nLoc: $location",
                            quantity = "$qty Portions",
                            category = cat
                        )
                        historyList.add(item)
                    }
                    historyAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DonationHistoryActivity, "Error loading history", Toast.LENGTH_SHORT).show()
            }
        })
    }
}