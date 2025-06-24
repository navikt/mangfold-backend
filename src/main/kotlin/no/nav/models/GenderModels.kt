package no.nav.models

import kotlinx.serialization.Serializable

@Serializable
data class GenderStatsResponse(
    val totalKjonnsfordeling: List<GenderCount>
)

@Serializable
data class GenderCount(
    val kjonn: String,
    val antall: Long
)
