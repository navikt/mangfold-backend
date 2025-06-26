package no.nav.models
import kotlinx.serialization.Serializable

@Serializable
data class AnsiennitetsgruppeKjonnCount(
    val ansiennitetsgruppe: String,
    val kvinne: Long,
    val mann: Long
)