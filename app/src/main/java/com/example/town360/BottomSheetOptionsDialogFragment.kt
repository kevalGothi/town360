// BottomSheetOptionsDialogFragment.kt
package com.example.town360

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment

class BottomSheetOptionsDialogFragment : DialogFragment() {

    private var onDeleteClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_options, container, false)

        val btnDelete = view.findViewById<LinearLayout>(R.id.btnDelete)



        btnDelete.setOnClickListener {
            onDeleteClickListener?.invoke()
            dismiss()
        }

        return view
    }



    fun setOnDeleteClickListener(listener: () -> Unit) {
        onDeleteClickListener = listener
    }
}
