package com.example.town360

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Move GridView setup to onViewCreated
        return view
    }

    // Move GridView setup to onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridView: GridView = view.findViewById(R.id.gridView)

        val dashboardItems = listOf(
            DashboardItem(R.drawable.farmer, "ખેતી"),
            DashboardItem(R.drawable.shopping, "ખરીદી"),
            DashboardItem(R.drawable.gov, "સરકારી કાર્યાલય"),
            DashboardItem(R.drawable.rest,"હોટેલ"),
            DashboardItem(R.drawable.handyman, "કારીગરો"),
            DashboardItem(R.drawable.mechanic, "ગાડી રિપેરિંગ"),
            DashboardItem(R.drawable.repairservices, "ટ્રેક્ટર રિપેરિંગ"),
            DashboardItem(R.drawable.bike, "બાઇક રિપેરિંગ"),
            DashboardItem(R.drawable.prop, "સ્થાવર મિલકત"),
            DashboardItem(R.drawable.housebuy, "મકાન"),
            DashboardItem(R.drawable.homerent, "ભાડે મકાન"),
            DashboardItem(R.drawable.vegfruit, "શાક ભાજી"),
            DashboardItem(R.drawable.school, "સ્કૂલ"),
            DashboardItem(R.drawable.classes, "ક્લાસ્સ"),
            DashboardItem(R.drawable.courier, "કૂરિયાર"),
            // Add more items as needed
        )

        val adapter = DashboardAdapter(requireContext(), dashboardItems)
        gridView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
