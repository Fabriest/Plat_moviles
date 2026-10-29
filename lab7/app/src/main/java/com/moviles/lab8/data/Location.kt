package com.moviles.lab8.data

import com.moviles.lab8.data.local.LocationEntity
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

fun LocationEntity.toModel(): Location {
    return Location(id, name, type, dimension)
}

fun Location.toEntity(): LocationEntity {
    return LocationEntity(id, name, type, dimension)
}