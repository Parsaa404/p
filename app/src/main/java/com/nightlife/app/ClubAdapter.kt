package com.nightlife.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nightlife.domain.Club

import android.content.Intent

class ClubAdapter(private val clubs: List<Club>) : RecyclerView.Adapter<ClubAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val locationTextView: TextView = view.findViewById(R.id.locationTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.club_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val club = clubs[position]
        viewHolder.nameTextView.text = club.name
        viewHolder.locationTextView.text = club.location
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, ClubDetailActivity::class.java)
            intent.putExtra("clubId", club.id)
            intent.putExtra("clubName", club.name)
            viewHolder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = clubs.size
}
