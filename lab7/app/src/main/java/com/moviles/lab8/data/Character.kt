package com.moviles.lab8.data

import com.moviles.lab8.data.local.CharacterEntity
import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val isFavorite: Boolean = false
)

fun CharacterEntity.toModel(): Character {
    return Character(id, name, status, species, gender, image, isFavorite)
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(id, name, status, species, gender, image, isFavorite)
}