package com.nightlife.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val viewClubsButton = findViewById<Button>(R.id.viewClubsButton)
        viewClubsButton.setOnClickListener {
            val intent = Intent(this, ClubListActivity::class.java)
            startActivity(intent)
        }

        val viewDashboardButton = findViewById<Button>(R.id.viewDashboardButton)
        viewDashboardButton.setOnClickListener {
            val intent = Intent(this, ClubDashboardActivity::class.java)
            startActivity(intent)
        }
    }
}
