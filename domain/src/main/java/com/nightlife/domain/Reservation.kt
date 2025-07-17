package com.nightlife.domain

import java.util.*

data class Reservation(
    val id: String = "",
    val userId: String = "",
    val clubId: String = "",
    val time: Date = Date(),
    val tableType: String = "",
    val qrCode: String = "",
    val status: String = "pending"
)
