package com.nightlife.app

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.nightlife.domain.Reservation
import java.util.*

class ClubDetailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_detail)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val clubId = intent.getStringExtra("clubId")
        val clubName = intent.getStringExtra("clubName")

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val reserveButton = findViewById<Button>(R.id.reserveButton)
        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)

        nameTextView.text = clubName

        reserveButton.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId != null && clubId != null) {
                val reservation = Reservation(
                    userId = userId,
                    clubId = clubId,
                    time = Date(),
                    tableType = "general"
                )
                db.collection("reservations")
                    .add(reservation)
                    .addOnSuccessListener { documentReference ->
                        val reservationId = documentReference.id
                        db.collection("reservations").document(reservationId)
                            .update("qrCode", reservationId)
                        try {
                            val barcodeEncoder = BarcodeEncoder()
                            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(reservationId, BarcodeFormat.QR_CODE, 400, 400)
                            qrCodeImageView.setImageBitmap(bitmap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Toast.makeText(baseContext, "Reservation successful.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(baseContext, "Reservation failed.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
