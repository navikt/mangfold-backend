package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class AvdelingAldersgrupperSeksjoner(
    val avdeling: String,
    val seksjoner: List<SeksjonAldersgrupper>
)

@Serializable
data class SeksjonAldersgrupper(
    val seksjon: String,
    val aldersgrupper: Map<String, KjonnAntallData>,
    val erMaskert: Boolean

)

@Serializable
data class KjonnAntallData(
    val kvinne: Long = 0,
    val mann: Long = 0,
)