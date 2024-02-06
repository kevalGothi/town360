// AddSubServiceActivity.kt
package com.example.town360

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException

class AddSubServiceActivity : AppCompatActivity() {

    private lateinit var serviceImageView: ImageView
    private lateinit var chooseImageButton: Button
    private lateinit var serviceNameEditText: TextInputEditText
    private lateinit var addButton: Button

    // Service name passed from the previous activity
    private lateinit var serviceName: String

    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addsubservice)

        // Initialize UI components
        serviceImageView = findViewById(R.id.serviceImageView)
        chooseImageButton = findViewById(R.id.chooseImageButton)
        serviceNameEditText = findViewById(R.id.serviceNameEditText)
        addButton = findViewById(R.id.addsubButton)

        // Get service name from the intent
        serviceName = intent.getStringExtra("SERVICE_NAME") ?: ""

        // Set click listeners
        chooseImageButton.setOnClickListener {
            openGallery()
        }

        addButton.setOnClickListener {
            addSubServiceToRealtimeDatabase()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            selectedImageUri = data.data

            try {
                // Set the selected image to the ImageView
                serviceImageView.setImageURI(selectedImageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addSubServiceToRealtimeDatabase() {
        val subServiceName = serviceNameEditText.text.toString().trim()

        if (subServiceName.isNotEmpty() && selectedImageUri != null) {
            val storageReference = FirebaseStorage.getInstance().reference.child("sub_service_images")
            val imageFileName = "${System.currentTimeMillis()}.jpg"
            val imageReference = storageReference.child(imageFileName)

            // Upload image to Firebase Storage
            imageReference.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Get the download URL of the uploaded image
                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        // Save the sub-service data with the image URL to Realtime Database
                        val db = FirebaseDatabase.getInstance().reference.child("sub_services").child(serviceName).child(subServiceName)
                        val newSubService = hashMapOf(
                            "name" to subServiceName,
                            "image" to uri.toString(), // Store the download URL
                            "parent_service" to serviceName
                        )

                        db.setValue(newSubService)
                            .addOnSuccessListener {
                                // Handle success, e.g., show a toast
                                Toast.makeText(this, "Sub-Service added successfully", Toast.LENGTH_SHORT).show()

                                // Clear UI components
                                serviceNameEditText.text = null
                                serviceImageView.setImageURI(null)
                            }
                            .addOnFailureListener { e ->
                                // Handle errors, e.g., show an error message
                                Toast.makeText(this, "Error adding sub-service", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle errors during image upload
                    Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Show an error message if required fields are not filled
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
        }
    }
}
