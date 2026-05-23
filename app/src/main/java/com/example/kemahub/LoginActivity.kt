package com.example.kemahub

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)

        val tabSignup = findViewById<LinearLayout>(R.id.tabSignup)
        tabSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        val btnGoToDonors = findViewById<Button>(R.id.btnGoToDonors)
        btnGoToDonors.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        val btnGoToBeneficiaries = findViewById<Button>(R.id.btnGoToBeneficiaries)
        btnGoToBeneficiaries.setOnClickListener {
            startActivity(Intent(this, BeneficiarySignupActivity::class.java))
        }

        val btnLoginMain = findViewById<Button>(R.id.btnLoginMain)
        btnLoginMain.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // 🔴 ලොග් වුණාම ඩේටාබේස් එකෙන් Role එක අරන් බලනවා
                            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                                .get().addOnSuccessListener { snapshot ->
                                    val role = snapshot.child("role").getValue(String::class.java)

                                    if (role == "Beneficiary") {
                                        Toast.makeText(this, "Welcome Beneficiary!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, BeneficiaryDashboardActivity::class.java))
                                    } else {
                                        Toast.makeText(this, "Welcome Donor!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, DonorDashboardActivity::class.java))
                                    }
                                    finish()
                                }.addOnFailureListener {
                                    // අවුලක් ගියොත් සාමාන්‍ය විදිහට Donor කෙනෙක් වගේ යවනවා
                                    startActivity(Intent(this, DonorDashboardActivity::class.java))
                                    finish()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        val ivTogglePassword = findViewById<ImageView>(R.id.ivTogglePassword)
        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivTogglePassword.setColorFilter(Color.parseColor("#FFB771"))
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivTogglePassword.setColorFilter(Color.parseColor("#888888"))
            }
            etPassword.setSelection(etPassword.text.length)
        }
    }
}