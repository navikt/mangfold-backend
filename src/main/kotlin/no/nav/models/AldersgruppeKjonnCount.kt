package no.nav.models
import kotlinx.serialization.Serializable

@Serializable
data class AldersgruppeKjonnCount (
    val aldersgruppe: String,
    val kvinne: Long,
    val mann: Long
)