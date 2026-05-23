package com.example.kemahub

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val etName = findViewById<EditText>(R.id.etSignupName)
        val etEmail = findViewById<EditText>(R.id.etSignupEmail)
        val etPassword = findViewById<EditText>(R.id.etSignupPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etSignupConfirmPassword)

        val btnSignupMain = findViewById<Button>(R.id.btnSignupMain)
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

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // අලුත් Account එක හදනවා
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // 🔴 මැජික් එක: ඩේටාබේස් එකේ "Donor" කියලා සේව් කරනවා
                            val userMap = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "role" to "Donor"
                            )
                            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                                .setValue(userMap).addOnCompleteListener {
                                    Toast.makeText(this, "Donor Account Created!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, DonorDashboardActivity::class.java))
                                    finish()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        val tvGoToLogin = findViewById<TextView>(R.id.tvGoToLogin)
        tvGoToLogin?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val ivTogglePassword = findViewById<ImageView>(R.id.ivTogglePasswordSignup)
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

        val ivToggleConfirmPassword = findViewById<ImageView>(R.id.ivToggleConfirmPasswordSignup)
        ivToggleConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivToggleConfirmPassword.setColorFilter(Color.parseColor("#FFB771"))
            } else {
                etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivToggleConfirmPassword.setColorFilter(Color.parseColor("#888888"))
            }
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }
    }
}