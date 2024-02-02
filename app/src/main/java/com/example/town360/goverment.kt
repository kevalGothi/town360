//
package com.example.town360

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.town360.R

class goverment : AppCompatActivity() {

    //  @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goverment)

        val gridView: GridView = findViewById(R.id.gridView)

        val dashboardItems = listOf(
            DashboardItem(R.drawable.municipal, "નગર પાલિકા"),
            DashboardItem(R.drawable.police, "પોલિસ સ્ટેશન"),
            DashboardItem(R.drawable.firestation, "ફાયર સ્ટેશન"),
            DashboardItem(R.drawable.court, "કોર્ટ"),
            DashboardItem(R.drawable.advocate, "વકીલ"),
            // Add more items as needed
        )

        val adapter = khedutadapter(this, dashboardItems)
        gridView.adapter = adapter
    }
}
