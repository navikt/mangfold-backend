package no.nav.models

import kotlinx.serialization.Serializable

@Serializable
data class DepartmentGenderCount(
    val avdeling: String,
    val kvinne: Long,
    val mann: Long
)