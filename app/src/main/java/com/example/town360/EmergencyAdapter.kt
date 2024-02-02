package com.example.town360

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class EmergencyAdapter(private val context: Context, private val items: List<DashboardItem>) : BaseAdapter() {

    private val activityClasses = arrayOf(
        bloodtype::class.java,
        Health::class.java,
        Health::class.java,
        Health::class.java,
        // Add more activities as needed
    )

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

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

        val item = getItem(position) as DashboardItem
        viewHolder.imageView.setImageResource(item.imageResId)
        viewHolder.textView.text = item.text

        // Handle item click
        view.setOnClickListener {
            // Create an Intent to start the corresponding activity
            val intent = Intent(context, activityClasses[position])

            // Pass information to the next activity
            intent.putExtra("ITEM_NAME", item.text)
            intent.putExtra("ITEM_IMAGE_RES_ID", item.imageResId)

            context.startActivity(intent)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView: TextView = view.findViewById(R.id.textView)
    }
}
