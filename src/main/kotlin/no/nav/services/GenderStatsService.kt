package no.nav.services

import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.JobId
import com.google.cloud.bigquery.JobInfo
import java.util.*
import no.nav.models.DepartmentGenderCount
import no.nav.models.AvdelingAlderKjonnCount
import no.nav.models.AvdelingAnsiennitetKjonnCount
import no.nav.models.SeksjonKjonnCount
import no.nav.models.SeksjonAlderKjonnCount
import no.nav.Config

fun hentTotalKjonnStatistikk(projectId: String): Map<String, Long> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT kjonn, COUNT(*) AS antall
        FROM `${Config.ANSATTE_TABELL}`
        GROUP BY kjonn
    """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query)
        .setUseLegacySql(false)
        .build()

    val jobId = JobId.of(projectId, UUID.randomUUID().toString())
    val queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()

    if (queryJob == null || queryJob.status.error != null) {
        throw RuntimeException("Query failed: ${queryJob?.status?.error}")
    }

    val results = queryJob.getQueryResults()
    val output = mutableMapOf<String, Long>()

    for (row in results.iterateAll()) {
        val kjonn = row["kjonn"].stringValue.lowercase()
        val antall = row["antall"].longValue
        output[kjonn] = antall
    }

    return output
}

fun hentKjonnPerAvdeling(projectId: String): List<DepartmentGenderCount> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_avdeling, kjonn, COUNT(*) AS antall
        FROM `${Config.ANSATTE_TABELL}`
        GROUP BY organisasjon_avdeling, kjonn
    """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query)
        .setUseLegacySql(false)
        .build()

    val jobId = JobId.of(projectId, UUID.randomUUID().toString())
    val queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()

    if (queryJob == null || queryJob.status.error != null) {
        throw RuntimeException("Query failed: ${queryJob?.status?.error}")
    }

    val results = queryJob.getQueryResults()

    val grouped = results.iterateAll()
        .groupBy { it["organisasjon_avdeling"].stringValue }
        .map { (avdeling, rows) ->
            val kjonnMap = rows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            DepartmentGenderCount(
                avdeling = avdeling,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
    return grouped
}

fun hentAldersgruppeKjonnPerAvdeling(projectId: String): List<AvdelingAlderKjonnCount> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_avdeling, aldersgruppe, kjonn, COUNT(*) AS antall
        FROM `${Config.ANSATTE_TABELL}`
        GROUP BY organisasjon_avdeling, aldersgruppe, kjonn
    """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query)
        .setUseLegacySql(false)
        .build()

    val jobId = JobId.of(projectId, UUID.randomUUID().toString())
    val queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()

    if (queryJob == null || queryJob.status.error != null) {
        throw RuntimeException("Query failed: ${queryJob?.status?.error}")
    }

    val results = queryJob.getQueryResults()

    val grouped = results.iterateAll()
        .groupBy { 
            val avd = it["organisasjon_avdeling"]?.stringValue ?: "Ukjent"
            val alder = it["aldersgruppe"]?.stringValue ?: "Ukjent"
            avd to alder
        }
        .map { (pair, rows) ->
            val kjonnMap = rows.associate { 
                (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue 
            }
            AvdelingAlderKjonnCount(
                avdeling = pair.first,
                aldersgruppe = pair.second,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0,
            )
        }
    return grouped
}

fun hentAnsiennnitetsgruppeKjonnPerAvdeling(projectId: String): List<AvdelingAnsiennitetKjonnCount> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_avdeling, ansiennitetsgruppe, kjonn, COUNT(*) AS antall
        FROM `${Config.ANSATTE_TABELL}`
        GROUP BY organisasjon_avdeling, ansiennitetsgruppe, kjonn
    """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query)
        .setUseLegacySql(false)
        .build()

    val jobId = JobId.of(projectId, UUID.randomUUID().toString())
    val queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()

    if (queryJob == null || queryJob.status.error != null) {
        throw RuntimeException("Query failed: ${queryJob?.status?.error}")
    }

    val results = queryJob.getQueryResults()

    val grouped = results.iterateAll()
        .groupBy { it["organisasjon_avdeling"].stringValue }
        .map { (avdeling, rows) ->
            val kjonnMap = rows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            AvdelingAnsiennitetKjonnCount(
                avdeling = avdeling,
                ansiennitetsgruppe = rows.first()["ansiennitetsgruppe"].stringValue,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
    return grouped
}

fun hentKjonnPerSeksjon(projectId: String): List<SeksjonKjonnCount> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_seksjon, kjonn, COUNT(*) AS antall
        FROM `${Config.ANSATTE_TABELL}`
        GROUP BY organisasjon_seksjon, kjonn
    """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query)
        .setUseLegacySql(false)
        .build()

    val jobId = JobId.of(projectId, UUID.randomUUID().toString())
    val queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()

    if (queryJob == null || queryJob.status.error != null) {
        throw RuntimeException("Query failed: ${queryJob?.status?.error}")
    }

    val results = queryJob.getQueryResults()

    return results.iterateAll()
        .groupBy { it["organisasjon_seksjon"].stringValue }
        .map { (seksjon, rows) ->
            val kjonnMap = rows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            SeksjonKjonnCount(
                seksjon = seksjon,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}

fun hentAldersgruppeKjonnPerSeksjon(projectId: String): List<SeksjonAlderKjonnCount> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_seksjon, aldersgruppe, kjonn, COUNT(*) AS antall
        FROM `${Config.ANSATTE_TABELL}`
        GROUP BY organisasjon_seksjon, aldersgruppe, kjonn
    """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(query)
        .setUseLegacySql(false)
        .build()

    val jobId = JobId.of(projectId, UUID.randomUUID().toString())
    val queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()

    if (queryJob == null || queryJob.status.error != null) {
        throw RuntimeException("Query failed: ${queryJob?.status?.error}")
    }

    val results = queryJob.getQueryResults()

    return results.iterateAll()
        .groupBy { it["organisasjon_seksjon"].stringValue }
        .map { (seksjon, rows) ->
            val kjonnMap = rows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            SeksjonAlderKjonnCount(
                seksjon = seksjon,
                aldersgruppe = rows.first()["aldersgruppe"].stringValue,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}