package com.nightlife.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.nightlife.domain.Reservation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class ClubDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reservationAdapter: ReservationAdapter
    private val firestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_dashboard)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val clubId = "REPLACE_WITH_ACTUAL_CLUB_ID" // Hardcoded for now

        GlobalScope.launch(Dispatchers.IO) {
            val reservations = firestore.collection("reservations")
                .whereEqualTo("clubId", clubId)
                .get()
                .await()
                .toObjects(Reservation::class.java)

            withContext(Dispatchers.Main) {
                reservationAdapter = ReservationAdapter(reservations)
                recyclerView.adapter = reservationAdapter
            }
        }

        val scanQrButton = findViewById<Button>(R.id.scanQrButton)
        scanQrButton.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt("Scan a QR code")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(true)
            integrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
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
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
