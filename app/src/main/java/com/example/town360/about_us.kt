package com.example.town360

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class about_us : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)

        val callButton = view.findViewById<Button>(R.id.callButton1)
        callButton?.setOnClickListener {
            // Call the user's phone number
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:+91 9265273298")
            startActivity(dialIntent)
        }
        val openLinkButton = view.findViewById<Button>(R.id.openInMapButton)
        openLinkButton?.setOnClickListener {
            // Open the specified link
            val linkIntent = Intent(Intent.ACTION_VIEW)
            linkIntent.data = Uri.parse("wa.link/h5339o8")
            startActivity(linkIntent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            about_us().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
