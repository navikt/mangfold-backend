package no.nav.services

import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.JobId
import com.google.cloud.bigquery.JobInfo
import java.util.*
import no.nav.models.DepartmentGenderCount
import no.nav.models.AvdelingAlderKjonnCount

fun hentTotalKjonnStatistikk(projectId: String): Map<String, Long> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT kjonn, COUNT(*) AS antall
        FROM `heda-prod-2664.hr_data.test_ansatte_direktoratet`
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
        FROM `heda-prod-2664.hr_data.test_ansatte_direktoratet`
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
        FROM `heda-prod-2664.hr_data.test_ansatte_direktoratet`
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