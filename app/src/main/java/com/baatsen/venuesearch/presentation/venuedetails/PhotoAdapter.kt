package com.baatsen.venuesearch.presentation.venuedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.baatsen.venuesearch.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.photo_item.view.*

class PhotoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var items: List<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> PhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> initPhoto(position, holder)
        }
    }

    private fun initPhoto(position: Int, holder: PhotoViewHolder) {
        Glide.with(holder.photo.context).load(items[position]).into(holder.photo)
    }

    override fun getItemCount(): Int {
        return if (::items.isInitialized) items.size else 0
    }

    fun updatePhotos(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo: ImageView = view.photo
    }
}