package com.example.town360

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.example.town360.databinding.ActivityHealthBinding
import com.example.town360.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        replaceFragment(Home())

        binding.bottomNavigationView.setOnItemSelectedListener {


            when(it.itemId){

                R.id.home -> replaceFragment(Home())
                R.id.emergency -> replaceFragment(emergency())
                R.id.aboutus -> replaceFragment(about_us())


                else ->{

                }

            }

            true

        }


//
//        val gridView: GridView = findViewById(R.id.gridView)
//
//        val dashboardItems = listOf(
//            DashboardItem(R.drawable.farmer, "ખેતી"),
//            DashboardItem(R.drawable.mechanic, "ગાડી રેપઈરિનગ"),
//            DashboardItem(R.drawable.handyman, "કારીગરો"),
//            DashboardItem(R.drawable.apartment, "પ્રોપરટી"),
//            DashboardItem(R.drawable.housebuy, "મકાન"),
//            DashboardItem(R.drawable.homerent, "ભાડે મકાન"),
//            DashboardItem(R.drawable.vegfruit, "શાક ભાજી"),
//            DashboardItem(R.drawable.mechanic, "Car Repair"),
//            DashboardItem(R.drawable.handyman, "Repair"),
//            // Add more items as needed
//        )
//
//        val adapter = DashboardAdapter(this, dashboardItems)
//        gridView.adapter = adapter
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManeger = supportFragmentManager
        val fragmentTrancection = fragmentManeger.beginTransaction()
        fragmentTrancection.replace(R.id.frame_layout,fragment)
        fragmentTrancection.commit()

    }
}
