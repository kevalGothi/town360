package com.example.town360

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.town360.R

class khedut_mitra : AppCompatActivity() {

  //  @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_khedut_mitra)

        val gridView: GridView = findViewById(R.id.gridView)

        val dashboardItems = listOf(
            DashboardItem(R.drawable.gardening, "ખેત-ઓજાર"),
            DashboardItem(R.drawable.bugspray, "જંતુનાસક દાવાઓ"),
            DashboardItem(R.drawable.seedbag, "ખાતર-બિયારણ"),
            DashboardItem(R.drawable.tractors, "ટ્રેક્ટર પાર્ટ"),
            DashboardItem(R.drawable.borewell, "બોરેવેલ"),
            DashboardItem(R.drawable.pumprepairing, "પમ્પ રેપરિંગ"),
            // Add more items as needed
        )

        val adapter = khedutadapter(this, dashboardItems)
        gridView.adapter = adapter
    }
}
