package com.example.town360

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class Home : Fragment() {

    private lateinit var gridView: GridView
    private lateinit var fabAddService: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        gridView = view.findViewById(R.id.gridView)
        fabAddService = view.findViewById(R.id.fabAddService)

        setupFab()

        // Register for context menu for long-press
        registerForContextMenu(gridView)

        return view
    }

    private fun setupFab() {
        fabAddService.setOnClickListener {
            // Handle FAB click
            navigateToAddService()
        }
    }

    private fun navigateToAddService() {
        val intent = Intent(requireContext(), AddServiceActivity::class.java)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch service data from Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("services")
        val services = mutableListOf<Service>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                services.clear()
                for (childSnapshot in snapshot.children) {
                    val service = childSnapshot.getValue(Service::class.java)
                    service?.let { services.add(it) }
                }

                try {
                    // Update the GridView with the new data
                    val adapter = ServiceAdapter(requireContext(), services)
                    gridView.adapter = adapter

                    // Set item click listener
                    gridView.setOnItemClickListener { _, _, position, _ ->
                        // Handle item click, if needed
                        val selectedService = services[position]
                        if (!selectedService.hasSubService) {
                            navigateToHealth(selectedService)
                        } else {
                            navigateToServiceDetails(selectedService)
                        }
                    }
                } catch (e: Exception) {
                    if (isAdded) { // Check if the fragment is added before showing the toast
                        showToast("Error updating UI: ${e.message}")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded) { // Check if the fragment is added before showing the toast
                    showToast("Database error: ${error.message}")
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHealth(selectedService: Service) {
        val intent = Intent(requireContext(), Health::class.java).apply {
            putExtra("ITEM_NAME", selectedService.name)
            // Add more data as needed
        }
        startActivity(intent)
    }

    private fun navigateToServiceDetails(selectedService: Service) {
        val intent = Intent(requireContext(), ServiceDetailsActivity::class.java).apply {
            putExtra("SERVICE_NAME", selectedService.name)
            putExtra("SERVICE_IMAGE", selectedService.image)
            // Add more data as needed
        }
        startActivity(intent)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == R.id.gridView) {
            requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        // Assuming you have a Service model in your adapter
        val selectedService = gridView.adapter.getItem(info.position) as Service

        when (item.itemId) {

            R.id.action_delete -> {
                showDeleteConfirmationDialog(selectedService)
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

   

    private fun showDeleteConfirmationDialog(selectedService: Service) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Service")
            .setMessage("Are you sure you want to delete ${selectedService.name}?")
            .setPositiveButton("Delete") { _, _ ->
                deleteService(selectedService)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteService(selectedService: Service) {
        // Implement the logic to delete the service from Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("services")
        val serviceReference = databaseReference.child(selectedService.name)

        serviceReference.removeValue()

            .addOnSuccessListener {
                showToast("Service deleted successfully")
            }
            .addOnFailureListener {
                showToast("Error deleting service")
            }
    }
}
