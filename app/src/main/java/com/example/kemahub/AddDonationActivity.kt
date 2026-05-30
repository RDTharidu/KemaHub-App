package com.example.kemahub

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream


data class DonationModel(
    var id: String? = null,
    var foodName: String? = null,
    var category: String? = null,
    var quantity: String? = null,
    var pickupLocation: String? = null,
    var note: String? = null,
    var contact: String? = null,
    var status: String? = null,
    var imageBase64: String? = null
)

class AddDonationActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private var currentQuantity = 1


    private var encodedImage: String? = null


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val ivFoodImage = findViewById<ImageView>(R.id.ivFoodImage)


            ivFoodImage.setImageURI(uri)
            ivFoodImage.scaleType = ImageView.ScaleType.CENTER_CROP
            ivFoodImage.setPadding(0, 0, 0, 0)
            ivFoodImage.clearColorFilter()


            encodedImage = encodeImageToBase64(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_donation)

        dbRef = FirebaseDatabase.getInstance().getReference("Donations")


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


        findViewById<ImageView>(R.id.ivFoodImage)?.setOnClickListener {
            pickImageLauncher.launch("image/*") // ෆොටෝ විතරක් පෙන්නන්න
        }

        val etFoodName = findViewById<EditText>(R.id.etFoodName)
        val etPickupLocation = findViewById<EditText>(R.id.etPickupLocation)
        val etNote = findViewById<EditText>(R.id.etNote)
        val etContact = findViewById<EditText>(R.id.etContact)
        val btnPostDonation = findViewById<Button>(R.id.btnPostDonation)

        val rgCategory = findViewById<RadioGroup>(R.id.rgCategory)
        val btnQtyMinus = findViewById<TextView>(R.id.btnQtyMinus)
        val btnQtyPlus = findViewById<TextView>(R.id.btnQtyPlus)
        val tvQuantityCount = findViewById<TextView>(R.id.tvQuantityCount)

        btnQtyPlus.setOnClickListener {
            currentQuantity++
            tvQuantityCount.text = currentQuantity.toString()
        }

        btnQtyMinus.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                tvQuantityCount.text = currentQuantity.toString()
            }
        }

        btnPostDonation.setOnClickListener {
            val foodName = etFoodName.text.toString().trim()
            val pickupLocation = etPickupLocation.text.toString().trim()
            val note = etNote.text.toString().trim()
            val contact = etContact.text.toString().trim()

            val selectedCategoryId = rgCategory.checkedRadioButtonId
            if (selectedCategoryId == -1) {
                Toast.makeText(this, "Please select a Food Category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedCategory = findViewById<RadioButton>(selectedCategoryId).text.toString()

            if (foodName.isEmpty() || pickupLocation.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill Food Name, Location & Contact", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val donationId = dbRef.push().key!!


            val donation = DonationModel(
                donationId,
                foodName,
                selectedCategory,
                currentQuantity.toString(),
                pickupLocation,
                note,
                contact,
                "Available",
                encodedImage
            )

            dbRef.child(donationId).setValue(donation)
                .addOnCompleteListener {
                    Toast.makeText(this, "Donation Added Successfully!", Toast.LENGTH_LONG).show()


                    etFoodName.text.clear()
                    etPickupLocation.text.clear()
                    etNote.text.clear()
                    etContact.text.clear()
                    rgCategory.clearCheck()
                    currentQuantity = 1
                    tvQuantityCount.text = "1"


                    val ivFoodImage = findViewById<ImageView>(R.id.ivFoodImage)
                    ivFoodImage.setImageResource(android.R.drawable.ic_menu_camera)
                    ivFoodImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    ivFoodImage.setPadding(20, 20, 20, 20)
                    ivFoodImage.setColorFilter(android.graphics.Color.parseColor("#666666"))
                    encodedImage = null
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    }


    private fun encodeImageToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()


            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            val byteArry = outputStream.toByteArray()

            Base64.encodeToString(byteArry, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}