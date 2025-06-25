package no.nav.models

import kotlinx.serialization.Serializable

@Serializable
data class LedernivaKjonnCount(
    val lederNiva: String,
    val kvinne: Long,
    val mann: Long
)