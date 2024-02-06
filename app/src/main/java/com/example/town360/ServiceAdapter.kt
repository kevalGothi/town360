// ServiceAdapter.kt
package com.example.town360

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class ServiceAdapter(private val context: Context, private val services: List<Service>) : BaseAdapter() {

    override fun getCount(): Int = services.size

    override fun getItem(position: Int): Any = services[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            // Inflate the view for each item if it's not recycled
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)

            // Create a ViewHolder and store references to the children views
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            // Reuse the recycled view
            view = convertView
            // Retrieve the ViewHolder from the recycled view
            viewHolder = view.tag as ViewHolder
        }

        val service = getItem(position) as Service
        // Load image using Glide or your preferred image loading library
        Glide.with(context)
            .load(service.image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_info_24) // optional placeholder image
                    .error(R.drawable.w) // optional error image
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // optional caching strategy
            )
            .into(viewHolder.imageView)

        viewHolder.textView.text = service.name

        return view
    }

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView: TextView = view.findViewById(R.id.textView)
    }
}
