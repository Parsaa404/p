package com.nightlife.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nightlife.domain.Reservation
import kotlinx.coroutines.*
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class ClubDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reservationAdapter: ReservationAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_dashboard)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val clubId = "REPLACE_WITH_ACTUAL_CLUB_ID" // Hardcoded for now

        coroutineScope.launch {
            val reservations = withContext(Dispatchers.IO) {
                firestore.collection("reservations")
                    .whereEqualTo("clubId", clubId)
                    .get()
                    .await()
                    .toObjects(Reservation::class.java)
            }
            reservationAdapter = ReservationAdapter(reservations)
            recyclerView.adapter = reservationAdapter
        }

        val scanQrButton = findViewById<Button>(R.id.scanQrButton)
        scanQrButton.setOnClickListener {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setPrompt("Scan a QR code")
            options.setCameraId(0)
            options.setBeepEnabled(false)
            options.setBarcodeImageEnabled(true)
            barcodeLauncher.launch(options)
        }
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            val reservationId = result.contents
            firestore.collection("reservations").document(reservationId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val reservation = document.toObject(Reservation::class.java)
                        // TODO: Display reservation details
                        Toast.makeText(this, "Reservation found: ${reservation?.userId}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Reservation not found", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error getting reservation", Toast.LENGTH_LONG).show()
                }
        }
    }
}
