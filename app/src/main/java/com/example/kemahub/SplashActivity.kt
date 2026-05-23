package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser

            // 🔴 කලින් ලොග් වෙලා ඉන්නවනම් Auto Login වෙනවා
            if (currentUser != null) {
                FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)
                    .get().addOnSuccessListener { snapshot ->
                        val role = snapshot.child("role").getValue(String::class.java)
                        if (role == "Beneficiary") {
                            startActivity(Intent(this, BeneficiaryDashboardActivity::class.java))
                        } else {
                            startActivity(Intent(this, DonorDashboardActivity::class.java))
                        }
                        finish()
                    }.addOnFailureListener {
                        startActivity(Intent(this, DonorDashboardActivity::class.java))
                        finish()
                    }
            } else {
                // ලොග් වෙලා නැත්නම් Login එකට යනවා
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }
}