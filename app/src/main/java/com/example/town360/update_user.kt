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
import com.example.town360.User
import com.example.town360.databinding.ActivityUpdateUserBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class UserUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserBinding
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
                binding.imgUserUpdate.setImageURI(selectedImageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnUpdateUser: Button = findViewById(R.id.btnUpdateUser)
        val etName: EditText = findViewById(R.id.etNameUpdate)
        val etNumber: EditText = findViewById(R.id.etNumberUpdate)
        val etDetails: EditText = findViewById(R.id.etDetailsUpdate)
        val tvJobDetails: TextView = findViewById(R.id.tvJobUpdate)
        val imgUser: ImageView = findViewById(R.id.imgUserUpdate)

        // Load user data from intent
        val userId = intent.getStringExtra("USER_ID")
        val userName = intent.getStringExtra("USER_NAME")
        val userNumber = intent.getStringExtra("USER_NUMBER")
        val userGoogleMapLink = intent.getStringExtra("USER_MAP_LINK")
        val userDetails = intent.getStringExtra("USER_DETAILS")
        val userJob = intent.getStringExtra("USER_JOB")
        val userProfileImage = intent.getStringExtra("USER_IMG_LINK")

        // Set user data to UI elements
        etName.setText(userName)
        etNumber.setText(userNumber)
        etDetails.setText(userDetails)
        tvJobDetails.text = userJob
        Picasso.get().load(userProfileImage).into(/* target = */ imgUser)

        binding.btnSelectImageUpdate.setOnClickListener {
            // Call the image picker
            pickImage.launch("image/*")
        }

        btnUpdateUser.setOnClickListener {
            val name = etName.text.toString()
            val number = etNumber.text.toString()
            val details = etDetails.text.toString()

            // Check if any of the fields are empty
            if (name.isEmpty() || number.isEmpty() || details.isEmpty()) {
                showToast("Please fill in all fields")
                return@setOnClickListener
            }

            // Create a User object
            val updatedUser = User(id = userId, name, number, details, userJob)

            // Upload the image and update user data
            uploadImageAndUpdateUser(userJob.toString(), updatedUser)

            // Show a toast
            showToast("User updated successfully!")
        }
    }

    private fun uploadImageAndUpdateUser(collectionName: String, user: User) {
        selectedImageUri?.let { uri ->
            // Create a reference to the image file
            val imageFileName = "${System.currentTimeMillis()}.jpg"
            val imageRef = imageRef.child(imageFileName)

            // Upload the image to Firebase Storage
            imageRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    // Image upload successful, get the download URL
                    imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                        // Save the user with the updated image URL to Firebase Realtime Database
                        user.image = imageUrl.toString()
                        updateUserInFirebase(collectionName, user)
                    }
                }
                .addOnFailureListener { exception ->
                    // Image upload failed
                    showToast("Failed to upload image. ${exception.message}")
                }
        }
    }

    private fun updateUserInFirebase(collectionName: String, user: User) {
        val usersReference = database.getReference(collectionName)
        val userReference = usersReference.child(user.id!!)
        userReference.setValue(user)
            .addOnSuccessListener {
                showToast("User updated successfully!")
                finish()  // Close the activity after successful update
            }
            .addOnFailureListener { exception ->
                showToast("Failed to update user. ${exception.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
