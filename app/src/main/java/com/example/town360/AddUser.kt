package com.example.town360

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.town360.databinding.ActivityAddUserBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddUser : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val imageRef: StorageReference by lazy {
        storage.reference.child("images") // You can customize the storage path
    }
    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Image selected from the gallery
                selectedImageUri = it

                // Display the selected image in your ImageView
                binding.imgUser.setImageURI(selectedImageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnAddUser: Button = findViewById(R.id.btnAddUser)
        val etName: EditText = findViewById(R.id.etName)
        val etNumber: EditText = findViewById(R.id.etNumber)
        val etDetails: EditText = findViewById(R.id.etDetails)
        val tvJobDetails: TextView = findViewById(R.id.tvJobDetails)
        binding.tvJob.text =  intent.getStringExtra("ITEM_NAME").toString()
        val imgUser: ImageView = findViewById(R.id.imgUser)

        binding.btnSelectImage.setOnClickListener {
            // Call the image picker
            pickImage.launch("image/*")
        }

        btnAddUser.setOnClickListener {
            val name = etName.text.toString()
            val number = etNumber.text.toString()
            val details = etDetails.text.toString()
            val job = intent.getStringExtra("ITEM_NAME").toString()

            // Check if any of the fields are empty
            if (name.isEmpty() || number.isEmpty() || details.isEmpty()) {
                showToast("Please fill in all fields")
                return@setOnClickListener
            }

            // Create a User object
            val user = User(id=null,name, number, details, job)

            // Upload the image and save user data
            uploadImageAndSaveUser(job, user)

            // Clear all EditText fields
            etName.text.clear()
            etNumber.text.clear()
            etDetails.text.clear()

            // Show a toast
            showToast("User added successfully!")
        }
    }

    private fun uploadImageAndSaveUser(collectionName: String, user: User) {
        selectedImageUri?.let { uri ->
            // Create a reference to the image file
            val imageFileName = "${System.currentTimeMillis()}.jpg"
            val imageRef = imageRef.child(imageFileName)

            // Upload the image to Firebase Storage
            imageRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    // Image upload successful, get the download URL
                    imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                        // Save the user with the image URL to Firebase Realtime Database
                        user.image = imageUrl.toString()
                        saveUserToFirebase(collectionName, user)
                    }
                }
                .addOnFailureListener { exception ->
                    // Image upload failed
                    showToast("Failed to upload image. ${exception.message}")
                }
        }
    }

    private fun saveUserToFirebase(collectionName: String, user: User) {
        val usersReference = database.getReference(collectionName)
        val userId = usersReference.push().key // Generate a unique key for the user
        userId?.let {
            val userReference = usersReference.child(it)
            user.id = it  // Set the ID
            userReference.setValue(user)
                .addOnSuccessListener {
                    showToast("User added successfully!")
                    finish()  // Close the activity after successful addition
                }
                .addOnFailureListener { exception ->
                    showToast("Failed to add user. ${exception.message}")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
