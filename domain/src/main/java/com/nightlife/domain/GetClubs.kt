package com.nightlife.domain

class GetClubs(private val clubRepository: ClubRepository) {
    suspend operator fun invoke(): List<Club> {
        return clubRepository.getClubs()
    }
}
