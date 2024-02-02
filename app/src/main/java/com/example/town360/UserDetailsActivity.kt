package com.example.town360

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var userImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userNumberTextView: TextView
    private lateinit var userJobTextView: TextView
    private lateinit var userDetailsTextView: TextView
    private lateinit var userReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        // Initialize UI components
        userImageView = findViewById(R.id.userImageView)
        userNameTextView = findViewById(R.id.userNameTextView)
        userNumberTextView = findViewById(R.id.userNumberTextView)
        userJobTextView = findViewById(R.id.userJobTextView)
        userDetailsTextView = findViewById(R.id.userDetailsTextView)

        // Retrieve user information from intent
        val collection = intent.getStringExtra("ITEM_NAME")
        val userId = intent.getStringExtra("USER_ID").toString()

        // Initialize Firebase Database reference
        userReference = FirebaseDatabase.getInstance().getReference(collection.toString()).child(userId)

        // Add a ValueEventListener to listen for changes in user data
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User data found, update UI
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        // Load image using Picasso library
                        if (!user.image.isNullOrBlank()) {
                            Picasso.get().load(user.image).into(userImageView)
                        } else {
                            // If the image URL is empty or null, set a default image resource
                            userImageView.setImageResource(R.drawable.wp)
                        }

                        // Display user details
                        userNameTextView.text = user.name
                        userNumberTextView.text = user.number
                        userJobTextView.text = user.job
                        userDetailsTextView.text = user.details

                        // Set click listener for the call button
                        val callButton = findViewById<Button>(R.id.callButton)
                        callButton.setOnClickListener {
                            // Call the user's phone number
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:${user.number}")
                            startActivity(dialIntent)
                        }

                        // Set click listener for the update button
                        val updateButton = findViewById<Button>(R.id.btnUpdate)
                        updateButton.setOnClickListener {
                            // Navigate to the UpdateUserActivity with necessary information
                            val intent = Intent(this@UserDetailsActivity, UserUpdateActivity::class.java)
                            intent.putExtra("ITEM_NAME", collection)
                            intent.putExtra("USER_ID", userId)
                            intent.putExtra("USER_NAME", user.name)
                            intent.putExtra("USER_JOB", user.job)
                            intent.putExtra("USER_NUMBER", user.number)
                            intent.putExtra("USER_IMG_LINK", user.image)
                            intent.putExtra("USER_DETAILS", user.details)
                            startActivity(intent)
                        }

                        // Set click listener for the delete button
                        val deleteButton = findViewById<Button>(R.id.btnDelete)
                        deleteButton.setOnClickListener {
                            // Show a confirmation dialog before deleting the user
                            showConfirmationDialog(userId, collection.toString())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun showConfirmationDialog(userId: String, collection: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this user?")

        builder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes, delete the user
            deleteUser(userId, collection)
        }

        builder.setNegativeButton("No") { _, _ ->
            // User clicked No, do nothing or provide feedback
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun deleteUser(userId: String, collection: String) {
        // Implement the deleteUser function to delete the user from Firebase
        userReference.removeValue()
        // Optionally, you can add a completion listener to handle the result
        // Provide appropriate feedback to the user
        // For example, display a Toast or navigate back to the previous screen
        finish()
    }
}
