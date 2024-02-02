package com.example.town360

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(private var userList: List<User>, private val onItemClick: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView2)
        val nameTextView: TextView = itemView.findViewById(R.id.textView3)
        val numberTextView: TextView = itemView.findViewById(R.id.textView4)
        val jobTextView: TextView = itemView.findViewById(R.id.textView5)

        init {
            // Set an OnClickListener for the entire itemView
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(userList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        // Load the image using Glide (replace "currentUser.image" with the actual image property)
        Glide.with(holder.imageView.context)
            .load(currentUser.image)
            .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
            .into(holder.imageView)

        holder.nameTextView.text = currentUser.name
        holder.numberTextView.text = currentUser.number
        holder.jobTextView.text = currentUser.job
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    // Update the user list and refresh the adapter
    fun updateUserList(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }
}
