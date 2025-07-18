package com.nightlife.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.nightlife.data.ClubRepositoryImpl
import com.nightlife.domain.GetClubs
import kotlinx.coroutines.*

class ClubListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var clubAdapter: ClubAdapter
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val firestore = FirebaseFirestore.getInstance()
        val clubRepository = ClubRepositoryImpl(firestore)
        val getClubs = GetClubs(clubRepository)

        coroutineScope.launch {
            val clubs = withContext(Dispatchers.IO) {
                getClubs()
            }
            clubAdapter = ClubAdapter(clubs)
            recyclerView.adapter = clubAdapter
        }
    }
}
