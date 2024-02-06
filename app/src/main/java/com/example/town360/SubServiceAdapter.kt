package com.example.town360
// SubServiceAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.town360.R

class SubServiceAdapter(private val context: Context, private val subServices: List<SubService>) : BaseAdapter() {

    override fun getCount(): Int {
        return subServices.size
    }

    override fun getItem(position: Int): Any {
        return subServices[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        // Set data to views
        val subService = getItem(position) as SubService
        viewHolder.bind(subService)

        return view!!
    }

    private class ViewHolder(view: View) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val textView: TextView = view.findViewById(R.id.textView)

        fun bind(subService: SubService) {
            // Load image using Glide or your preferred image loading library
            Glide.with(imageView.context)
                .load(subService.image)
                .into(imageView)

            textView.text = subService.name
        }
    }
}
