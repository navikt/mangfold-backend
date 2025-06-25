package no.nav.models

import kotlinx.serialization.Serializable

@Serializable
data class RolleKjonnCount(
    val rolle: String,
    val kvinne: Long,
    val mann: Long
)

