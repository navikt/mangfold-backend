package no.nav.modeller
import kotlinx.serialization.Serializable

@Serializable
data class AvdelingAnsiennitetKjonnAntall(
    val avdeling: String,
    val ansiennitetsgruppe: String,
    val kvinne: Long,
    val mann: Long
)