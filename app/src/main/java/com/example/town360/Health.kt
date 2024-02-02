package com.example.town360

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.town360.databinding.ActivityHealthBinding
import com.google.firebase.database.*

class Health : AppCompatActivity() {

    private lateinit var binding: ActivityHealthBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()

        // Retrieve extra data from the Intent
        val itemName = intent.getStringExtra("ITEM_NAME")
        supportActionBar?.title = itemName

        // Initialize RecyclerView and UserAdapter
        recyclerView = findViewById(R.id.recycleview)
        userAdapter = UserAdapter(ArrayList()) { clickedUser ->
            // Handle item click here using 'clickedUser'
            // 'clickedUser' is the user that was clicked
            // For example, you can launch a new activity with the user details
            val intent = Intent(this@Health, UserDetailsActivity::class.java)
            intent.putExtra("USER_ID", clickedUser.id)
            intent.putExtra("ITEM_NAME", itemName)
            startActivity(intent)
        }
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set the TextView to display the retrieved name
        binding.textView2.text = itemName

        // Set up Firebase reference with the collection name as itemName
        usersReference = database.getReference(itemName.toString())

        val btn: Button = findViewById(R.id.button2)
        btn.setOnClickListener {
            val intent = Intent(this, AddUser::class.java)
            intent.putExtra("ITEM_NAME", itemName)
            startActivity(intent)
        }

        // Retrieve and display data from Firebase
        retrieveDataFromFirebase()
    }

    private fun retrieveDataFromFirebase() {
        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userList = mutableListOf<User>()

                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        userList.add(it)
                    }
                }

                userAdapter.updateUserList(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                val errorMessage = "Firebase Data Retrieval Error: ${error.message}"
                // Display a Toast message or log the error
            }
        })
    }
}
