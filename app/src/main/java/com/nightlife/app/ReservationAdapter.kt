package com.nightlife.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nightlife.domain.Reservation
import java.text.SimpleDateFormat
import java.util.*

class ReservationAdapter(private val reservations: List<Reservation>) : RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userIdTextView: TextView = view.findViewById(R.id.userIdTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reservation_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val reservation = reservations[position]
        viewHolder.userIdTextView.text = reservation.userId
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        viewHolder.timeTextView.text = sdf.format(reservation.time)
    }

    override fun getItemCount() = reservations.size
}
