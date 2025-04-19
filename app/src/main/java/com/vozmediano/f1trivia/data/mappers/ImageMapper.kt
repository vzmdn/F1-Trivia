package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.ImageEntity
import com.vozmediano.f1trivia.data.network.model.WikiResponse
import com.vozmediano.f1trivia.domain.model.wiki.Image

fun WikiResponse.toDomain(title:String) = Image(
    title = title,
    imageUrl = this.items.first().srcset.first().src.substring(2)
)

fun Image.toDatabase() = ImageEntity(
    title = title,
    imageUrl = imageUrl
)

fun ImageEntity.toDomain() = Image(
    title = title,
    imageUrl = imageUrl
)