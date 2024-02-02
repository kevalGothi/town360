package com.example.town360

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.town360.R

class bloodtype : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bloodtype)


        val gridView: GridView = findViewById(R.id.gridView)

        val dashboardItems = listOf(
            DashboardItem(R.drawable.bloodtypea, "A+"),
            DashboardItem(R.drawable.bloodtypeaneg, "A-"),
            DashboardItem(R.drawable.bloodtypebpos, "B+"),
            DashboardItem(R.drawable.bloodtypebneg, "B-"),
            DashboardItem(R.drawable.bloodtypeabpos, "AB+"),
            DashboardItem(R.drawable.bloodtypeabneg, "AB-"),
            DashboardItem(R.drawable.bloodtypeopos, "O+"),
            DashboardItem(R.drawable.bloodtypeoneg, "O-"),
            // Add more items as needed
        )

        val adapter = khedutadapter(this, dashboardItems)
        gridView.adapter = adapter
    }
}