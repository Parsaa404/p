package com.nightlife.domain

interface ClubRepository {
    suspend fun getClubs(): List<Club>
}
