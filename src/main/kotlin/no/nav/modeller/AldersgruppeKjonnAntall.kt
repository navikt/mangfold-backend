package no.nav.modeller
import kotlinx.serialization.Serializable

@Serializable
data class AldersgruppeKjonnAntall (
    val aldersgruppe: String,
    val kvinne: Long,
    val mann: Long
)