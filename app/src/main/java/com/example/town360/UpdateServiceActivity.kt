// UpdateServiceActivity.kt
package com.example.town360

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class UpdateServiceActivity : AppCompatActivity() {

    private lateinit var serviceNameEditText: TextInputEditText
    private lateinit var updateButton: MaterialButton

    private lateinit var parentServiceName: String
    private lateinit var subServiceName: String

    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_service)

        serviceNameEditText = findViewById(R.id.serviceNameEditText)
        updateButton = findViewById(R.id.updateButton)

        // Retrieve data from intent
        parentServiceName = intent.getStringExtra("SERVICE_NAME").toString()
        subServiceName = intent.getStringExtra("SUB_SERVICE_NAME").toString()

        // Set the existing sub-service name in the edit text
        serviceNameEditText.setText(subServiceName)

        updateButton.setOnClickListener {
            updateSubService()
        }
    }

    private fun updateSubService() {
        val updatedSubServiceName = serviceNameEditText.text.toString().trim()

        if (updatedSubServiceName.isEmpty()) {
            // Handle empty input
            serviceNameEditText.error = "Sub-Service name cannot be empty"
            return
        }

        // Check if the sub-service name is unchanged
        if (subServiceName == updatedSubServiceName) {
            showToast("Sub-Service name unchanged")
            finish()
            return
        }

        // Update the sub-service name in Firebase
        val subServiceReference =
            databaseReference.child("sub_services").child(parentServiceName)
                .child(subServiceName)

        subServiceReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Check if the sub-service exists
                if (snapshot.exists()) {
                    // Remove the existing sub-service
                    subServiceReference.removeValue()

                    // Add the updated sub-service with the new name
                    val updatedSubServiceReference =
                        databaseReference.child("sub_services").child(parentServiceName)
                            .child(updatedSubServiceName)

                    updatedSubServiceReference.setValue(true)
                        .addOnSuccessListener {
                            showToast("Sub-Service updated successfully")
                            finish()
                        }
                        .addOnFailureListener {
                            showToast("Error updating sub-service")
                        }
                } else {
                    showToast("Sub-Service not found")
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun showToast(message: String) {
        // Implement showToast method based on your project's implementation
    }
}
