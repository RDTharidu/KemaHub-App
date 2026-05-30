package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
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
import java.util.Locale

class ActiveDonationActivity : AppCompatActivity() {

    private lateinit var rvActiveDonations: RecyclerView
    private lateinit var foodListAll: ArrayList<FoodItem> // මුල්ම දත්ත ලැයිස්තුව සේව් කරගන්න
    private lateinit var foodListDisplay: ArrayList<FoodItem> // ස්ක්‍රීන් එකේ පේන ලැයිස්තුව
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var dbRef: DatabaseReference

    private var searchText = ""
    private var selectedCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_donation)

        // Navigation
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }
        findViewById<ImageView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, DonorDashboardActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.btnAddDonation)?.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
        }

        // Setup RecyclerView
        rvActiveDonations = findViewById(R.id.rvActiveDonations)
        rvActiveDonations.layoutManager = GridLayoutManager(this, 2)

        foodListAll = ArrayList()
        foodListDisplay = ArrayList()
        foodAdapter = FoodAdapter(foodListDisplay)
        rvActiveDonations.adapter = foodAdapter

        // Search Logic
        val etSearchFood = findViewById<EditText>(R.id.etSearchFood)
        etSearchFood.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString().trim()
                filterData()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Category Buttons Logic
        val btnCooked = findViewById<TextView>(R.id.btnCookedFood)
        val btnDry = findViewById<TextView>(R.id.btnDryFood)
        val btnFruit = findViewById<TextView>(R.id.btnFruitBakery)

        btnCooked.setOnClickListener { toggleCategory("Cooked Food") }
        btnDry.setOnClickListener { toggleCategory("Dry Food") }
        btnFruit.setOnClickListener { toggleCategory("Fruit / Bakery") }

        fetchDonationsFromFirebase()
    }

    private fun toggleCategory(category: String) {

        selectedCategory = if (selectedCategory == category) "" else category
        filterData()
    }

    private fun fetchDonationsFromFirebase() {
        dbRef = FirebaseDatabase.getInstance().getReference("Donations")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodListAll.clear()
                if (snapshot.exists()) {
                    for (donationSnap in snapshot.children) {
                        val foodName = donationSnap.child("foodName").getValue(String::class.java) ?: ""
                        val location = donationSnap.child("pickupLocation").getValue(String::class.java) ?: ""
                        val status = donationSnap.child("status").getValue(String::class.java) ?: ""
                        val qty = donationSnap.child("quantity").getValue(String::class.java) ?: "1"
                        val cat = donationSnap.child("category").getValue(String::class.java) ?: ""

                        if (status == "Available") {
                            val item = FoodItem(
                                id = donationSnap.key ?: "",
                                name = foodName,
                                description = "Loc: $location",
                                quantity = "$qty Portions",
                                category = cat
                            )
                            foodListAll.add(item)
                        }
                    }
                    filterData()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActiveDonationActivity, "Error loading data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun filterData() {
        foodListDisplay.clear()
        for (item in foodListAll) {
            val matchesSearch = item.name.toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))
            val matchesCategory = selectedCategory.isEmpty() || item.category == selectedCategory

            if (matchesSearch && matchesCategory) {
                foodListDisplay.add(item)
            }
        }
        foodAdapter.notifyDataSetChanged()
    }
}