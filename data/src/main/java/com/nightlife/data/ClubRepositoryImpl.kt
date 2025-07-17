package com.nightlife.data

import com.google.firebase.firestore.FirebaseFirestore
import com.nightlife.domain.Club
import com.nightlife.domain.ClubRepository
import kotlinx.coroutines.tasks.await

class ClubRepositoryImpl(private val firestore: FirebaseFirestore) : ClubRepository {

    override suspend fun getClubs(): List<Club> {
        // For testing, I'll add some dummy data to Firestore if it's empty
        val clubsCollection = firestore.collection("clubs")
        val snapshot = clubsCollection.get().await()
        if (snapshot.isEmpty) {
            clubsCollection.add(Club(name = "Club A", location = "New York", capacity = 100))
            clubsCollection.add(Club(name = "Club B", location = "Los Angeles", capacity = 150))
            clubsCollection.add(Club(name = "Club C", location = "Miami", capacity = 200))
        }

        val result = firestore.collection("clubs")
            .get()
            .await()
        return result.documents.map { document ->
            val club = document.toObject(Club::class.java)!!
            club.copy(id = document.id)
        }
    }
}
