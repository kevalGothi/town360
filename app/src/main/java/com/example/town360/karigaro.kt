package com.example.town360

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.town360.R

class karigaro : AppCompatActivity() {

    //  @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_karigaro)

        val gridView: GridView = findViewById(R.id.gridView)

        val dashboardItems = listOf(
            DashboardItem(R.drawable.electrician, "એલેક્ટ્રીસેયન"),
            DashboardItem(R.drawable.laborman, "મજૂર"),
            DashboardItem(R.drawable.painter, "પેંટર"),
            DashboardItem(R.drawable.glassfitting, "ગ્લાસ ફિટટીનગ"),
            DashboardItem(R.drawable.tv, "tv રેપઈરિનગ"),
            DashboardItem(R.drawable.mobilerepairing, "મોબાઈલ રેપઈરિનગ"),
            DashboardItem(R.drawable.airconditioner, "ac રેપઈરિનગ"),
            // Add more items as needed
        )

        val adapter = khedutadapter(this, dashboardItems)
        gridView.adapter = adapter
    }
}
