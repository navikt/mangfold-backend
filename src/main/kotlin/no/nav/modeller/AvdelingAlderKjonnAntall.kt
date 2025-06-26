package no.nav.modeller
import kotlinx.serialization.Serializable

@Serializable
data class AvdelingAlderKjonnAntall(
    val avdeling: String,
    val aldersgruppe: String,
    val kvinne: Long,
    val mann: Long
)