package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Embedded


data class FastestLapEntity(
    val rank: String,
    val lap: String,
    @Embedded val fastestLapTime: TimeEntity,
    @Embedded val averageSpeed: AverageSpeedEntity ? = null
)
