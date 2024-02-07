// ServiceDetailsActivity.kt
package com.example.town360

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.*

class ServiceDetailsActivity : AppCompatActivity() {

    private lateinit var serviceNameTextView: MaterialTextView
    private lateinit var gridView: GridView
    private lateinit var fabAddService: FloatingActionButton

    private lateinit var parentServiceName: String

    private lateinit var subServices: MutableList<SubService>
    private lateinit var subServiceAdapter: SubServiceAdapter
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)

        serviceNameTextView = findViewById(R.id.serviceNameTextView)
        gridView = findViewById(R.id.gridView1)
        fabAddService = findViewById(R.id.fabAddService1)

        parentServiceName = intent.getStringExtra("SERVICE_NAME").toString()
        serviceNameTextView.text = parentServiceName

        subServices = mutableListOf()
        subServiceAdapter = SubServiceAdapter(this, subServices)
        gridView.adapter = subServiceAdapter

        registerForContextMenu(gridView)

        retrieveSubServicesFromFirebase()

        fabAddService.setOnClickListener {
            val i = Intent(this, AddSubServiceActivity::class.java).apply {
                putExtra("SERVICE_NAME", parentServiceName)
            }
            startActivity(i)
        }

        gridView.setOnItemLongClickListener { _, view, position, _ ->
            showBottomSheetOptions(subServices[position])
            true
        }

        gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedSubService = subServices[position]
            val i = Intent(this, Health::class.java).apply {
                putExtra("ITEM_NAME", selectedSubService.name)
            }
            startActivity(i)
        }



    }

    private fun retrieveSubServicesFromFirebase() {
        val subServicesReference = databaseReference.child("sub_services").child(parentServiceName)
        subServicesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                subServices.clear()
                for (childSnapshot in snapshot.children) {
                    val subService = childSnapshot.getValue(SubService::class.java)
                    subService?.let { subServices.add(it) }
                }
                subServiceAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Database error: ${error.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showBottomSheetOptions(subService: SubService) {
        val bottomSheetFragment = BottomSheetOptionsDialogFragment()
        bottomSheetFragment.setOnDeleteClickListener {
            showDeleteConfirmationDialog(subService)
        }
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }


    private fun showDeleteConfirmationDialog(subService: SubService) {
        AlertDialog.Builder(this)
            .setTitle("Delete Sub-Service")
            .setMessage("Are you sure you want to delete ${subService.name}?")
            .setPositiveButton("Delete") { _, _ ->
                deleteSubService(subService)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteSubService(subService: SubService) {
        val subServiceReference =
            databaseReference.child("sub_services").child(parentServiceName)
                .child(subService.name)
        subServiceReference.removeValue()
            .addOnSuccessListener {
                showToast("Sub-Service deleted successfully")
            }
            .addOnFailureListener {
                showToast("Error deleting sub-service")
            }
    }
}
