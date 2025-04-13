package com.vozmediano.f1trivia.domain.model.usecase

import android.util.Log
import com.vozmediano.f1trivia.domain.F1CircuitRepository
import com.vozmediano.f1trivia.domain.F1DriverRepository
import com.vozmediano.f1trivia.domain.model.quiz.Option
import com.vozmediano.f1trivia.domain.model.quiz.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DriverBySeasonAndCircuitAndPositionUseCase(
    private val f1DriverRepository: F1DriverRepository,
    private val f1CircuitRepository: F1CircuitRepository
) {
    suspend operator fun invoke(): Question? = withContext(Dispatchers.IO) {
        val correctSeason = (1960..2024).random().toString()

        val circuits = try {
            Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Fetching circuits")
            f1CircuitRepository.getCircuitsBySeason(correctSeason)
        } catch (e: Exception) {
            Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Error fetching circuits: ${e.message}")
            return@withContext null
        }

        if (circuits.isEmpty()) {
            Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "No circuits found for season $correctSeason")
            return@withContext null
        }

        val correctCircuit = circuits.random()
        val correctPosition = "1"

        val correctDriver = try {
            Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Fetching driver")
            f1DriverRepository.getDriverBySeasonAndCircuitAndPosition(
                correctSeason,
                correctCircuit.circuitId,
                correctPosition
            )
        } catch (e: Exception) {
            Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Error fetching driver: ${e.message}")
            return@withContext null
        }

        val question = Question(
            title = "Who won at ${correctCircuit.circuitName} in $correctSeason?",
            options = mutableListOf()
        )

        val driverSet = mutableSetOf<String>()

        question.options.add(
            Option(
                id = 0,
                shortText = "${correctDriver.givenName} ${correctDriver.familyName}",
                longText = "${correctDriver.givenName} ${correctDriver.familyName} won at ${correctCircuit.circuitName} in $correctSeason",
                isCorrect = true
            )
        )

        driverSet.add(correctDriver.driverId)

        while (driverSet.size < 4) {
            var season = ""
            var circuitId = ""
            var position = ""
            when ((1..5).random()) {
                //Same circuit, different season
                1 -> {
                    Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Same circuit, different season")
                    season = correctSeason + (-2..2).random()
                    circuitId = correctCircuit.circuitId
                    position = "1"
                }
                //Different circuit, same season
                2 -> {
                    Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Different circuit, same season")
                    season = correctSeason
                    circuitId = circuits.filter { it.circuitId != correctCircuit.circuitId }
                        .random().circuitId
                    position = "1"
                }
                //Same circuit, same season, 2nd position
                3 -> {
                    Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Same circuit, same season, 2nd position")
                    season = correctSeason
                    circuitId = correctCircuit.circuitId
                    position = "2"
                }
                //Same circuit, same season, 3rd position
                4 -> {
                    Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Same circuit, same season, 3rd position")
                    season = correctSeason
                    circuitId = correctCircuit.circuitId
                    position = "3"
                }
                //Different race, same year, podium position
                5 -> {
                    Log.i("DriverBySeasonAndCircuitAndPositionUseCase", "Different race, same year, podium position")
                    season = correctSeason
                    circuitId = circuits.filter { it.circuitId != correctCircuit.circuitId }
                        .random().circuitId
                    position = (2..3).random().toString()
                }
            }

            try {
                val distractor =
                    f1DriverRepository.getDriverBySeasonAndCircuitAndPosition(season, circuitId, position)
                if (driverSet.contains(distractor.driverId)) continue

                driverSet.add(distractor.driverId)
                question.options.add(
                    Option(
                        id = driverSet.size,
                        shortText = "${distractor.givenName} ${distractor.familyName}",
                        longText = "",
                        isCorrect = false
                    )
                )
            } catch (e: Exception) {
                continue
            }
        }

        question.options.shuffle()

        return@withContext question
    }
}
