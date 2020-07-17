package com.baatsen.venuesearch.presentation.venues

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baatsen.venuesearch.R
import com.baatsen.venuesearch.domain.model.Venue
import kotlinx.android.synthetic.main.venue_item.view.*

class VenueAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var items: List<Venue>
    var onVenueClicked: ((venueId: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> VenueViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.venue_item, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VenueViewHolder -> initVenue(position, holder)
        }
    }

    private fun initVenue(position: Int, holder: VenueViewHolder) {
        with(holder) {
            name.text = items[position].name
            address.text = items[position].location
            itemView.setOnClickListener { onVenueClicked?.let { it(items[position].id) } }
        }
    }

    override fun getItemCount(): Int {
        return if (::items.isInitialized) items.size else 0
    }

    fun updateVenues(items: List<Venue>) {
        this.items = items
        notifyDataSetChanged()
    }

    class VenueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.venue_name
        val address: TextView = view.venue_address
    }
}