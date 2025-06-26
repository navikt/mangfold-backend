package no.nav.modeller
import kotlinx.serialization.Serializable

@Serializable
data class AvdelingKjonnAntall(
    val avdeling: String,
    val kvinne: Long,
    val mann: Long
)