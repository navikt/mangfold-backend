package no.nav.models

import kotlinx.serialization.Serializable

@Serializable
data class AvdelingAnsiennitetKjonnCount(
    val avdeling: String,
    val ansiennitetsgruppe: String,
    val kvinne: Long,
    val mann: Long
)