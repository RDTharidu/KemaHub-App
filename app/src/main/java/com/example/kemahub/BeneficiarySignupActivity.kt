package com.example.kemahub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BeneficiarySignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) // ඩිසයින් එකට Signup එකම පාවිච්චි කරනවා

        auth = FirebaseAuth.getInstance()

        val etName = findViewById<EditText>(R.id.etSignupName)
        val etEmail = findViewById<EditText>(R.id.etSignupEmail)
        val etPassword = findViewById<EditText>(R.id.etSignupPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etSignupConfirmPassword)

        val btnSignupMain = findViewById<Button>(R.id.btnSignupMain)
        btnSignupMain.text = "Beneficiary Sign Up" // බොත්තමේ නම වෙනස් කළා

        btnSignupMain.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // 🔴 මැජික් එක: ඩේටාබේස් එකේ "Beneficiary" කියලා සේව් කරනවා
                            val userMap = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "role" to "Beneficiary"
                            )
                            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                                .setValue(userMap).addOnCompleteListener {
                                    Toast.makeText(this, "Beneficiary Account Created!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, BeneficiaryDashboardActivity::class.java))
                                    finish()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}