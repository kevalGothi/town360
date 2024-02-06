// BottomSheetOptionsDialogFragment.kt
package com.example.town360

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment

class BottomSheetOptionsDialogFragment : DialogFragment() {

    private var onUpdateClickListener: (() -> Unit)? = null
    private var onDeleteClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_options, container, false)

        val btnUpdate = view.findViewById<LinearLayout>(R.id.btnUpdate)
        val btnDelete = view.findViewById<LinearLayout>(R.id.btnDelete)

        btnUpdate.setOnClickListener {
            onUpdateClickListener?.invoke()
            dismiss()
        }

        btnDelete.setOnClickListener {
            onDeleteClickListener?.invoke()
            dismiss()
        }

        return view
    }

    fun setOnUpdateClickListener(listener: () -> Unit) {
        onUpdateClickListener = listener
    }

    fun setOnDeleteClickListener(listener: () -> Unit) {
        onDeleteClickListener = listener
    }
}
