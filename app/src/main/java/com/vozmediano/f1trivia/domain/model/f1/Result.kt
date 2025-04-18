package com.vozmediano.f1trivia.domain.model.f1

import androidx.room.Embedded

data class Result(
    val number : String,
    val position : String,
    val positionText : String,
    val points : String,
    @Embedded val driver : Driver,
    @Embedded val constructor : Constructor,
    val grid : String,
    val laps : String,
    val status : String,
    @Embedded val fastestLap: FastestLap ? = null,

    val season: String?,
    val round: String?,
    @Embedded val circuit: Circuit,
    val raceName: String
)
