package com.vozmediano.f1trivia.data

import com.vozmediano.f1trivia.data.local.entities.DriverEntity
import com.vozmediano.f1trivia.data.network.model.DriversResponse
import com.vozmediano.f1trivia.domain.model.Driver

fun DriversResponse.toDomain() = Driver(
    driverId = driverId,
    permanentNumber = permanentNumber ?: "",
    code = code ?: "",
    url = url ?: "",
    givenName = givenName ?: "",
    familyName = familyName ?: "",
    dateOfBirth = dateOfBirth ?: "",
    nationality = nationality ?: ""
)

fun Driver.toDatabase() = DriverEntity(
    driverId = driverId,
    permanentNumber = permanentNumber,
    code = code,
    url = url,
    givenName = givenName,
    familyName = familyName,
    dateOfBirth = dateOfBirth,
    nationality = nationality
)

fun DriverEntity.toDomain() = Driver(
    driverId = driverId,
    permanentNumber = permanentNumber,
    code = code,
    url = url,
    givenName = givenName,
    familyName = familyName,
    dateOfBirth = dateOfBirth,
    nationality = nationality
)
