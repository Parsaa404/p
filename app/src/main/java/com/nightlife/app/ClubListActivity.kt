package com.nightlife.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.nightlife.data.ClubRepositoryImpl
import com.nightlife.domain.GetClubs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClubListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var clubAdapter: ClubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val firestore = FirebaseFirestore.getInstance()
        val clubRepository = ClubRepositoryImpl(firestore)
        val getClubs = GetClubs(clubRepository)

        GlobalScope.launch(Dispatchers.IO) {
            val clubs = getClubs()
            withContext(Dispatchers.Main) {
                clubAdapter = ClubAdapter(clubs)
                recyclerView.adapter = clubAdapter
            }
        }
    }
}
