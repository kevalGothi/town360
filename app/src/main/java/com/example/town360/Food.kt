package com.example.town360

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.town360.R

class food : AppCompatActivity() {

    //  @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_karigaro)

        val gridView: GridView = findViewById(R.id.gridView)

        val dashboardItems = listOf(
            DashboardItem(R.drawable.rest, "હોટલ"),
            DashboardItem(R.drawable.pizza, "પિઝા"),
            DashboardItem(R.drawable.sifood, "સાઉથ ઇંડિયન ફૂડ"),
            DashboardItem(R.drawable.homedelivery, "હોમ ડિલિવરી"),
            // Add more items as needed
        )

        val adapter = khedutadapter(this, dashboardItems)
        gridView.adapter = adapter
    }
}
