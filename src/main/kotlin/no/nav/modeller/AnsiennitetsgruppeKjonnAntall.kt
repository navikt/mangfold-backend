package no.nav.modeller
import kotlinx.serialization.Serializable

@Serializable
data class AnsiennitetsgruppeKjonnAntall(
    val ansiennitetsgruppe: String,
    val kvinne: Long,
    val mann: Long
)