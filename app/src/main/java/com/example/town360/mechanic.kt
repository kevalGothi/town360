package com.example.town360

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.town360.R

class mechanic : AppCompatActivity() {

    //  @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanic)

        val gridView: GridView = findViewById(R.id.gridView)

        val dashboardItems = listOf(
            DashboardItem(R.drawable.electronicshop, "ઈલેક્ટ્રિક સ્ટોર"),
            DashboardItem(R.drawable.anaj, "અનાજ કરિયાણું"),
            DashboardItem(R.drawable.clothshop, "કપડાં"),
            DashboardItem(R.drawable.toyshop, "રમકડાં"),
            DashboardItem(R.drawable.hardwareshop, "હાર્ડવેર"),
            DashboardItem(R.drawable.genralstore, "જનરલ સ્ટોર"),
            DashboardItem(R.drawable.footware, "જનરલ સ્ટોર"),
            // Add more items as needed
        )

        val adapter = khedutadapter(this, dashboardItems)
        gridView.adapter = adapter
    }
}
