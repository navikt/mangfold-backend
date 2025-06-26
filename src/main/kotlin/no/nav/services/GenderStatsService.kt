package no.nav.services

import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.JobId
import com.google.cloud.bigquery.JobInfo
import java.util.*
import no.nav.modeller.AvdelingKjonnAntall
import no.nav.modeller.AvdelingAlderKjonnAntall
import no.nav.modeller.AvdelingAnsiennitetKjonnAntall
import no.nav.modeller.SeksjonKjonnAntall
import no.nav.modeller.SeksjonAlderKjonnAntall
import no.nav.modeller.RolleKjonnAntall
import no.nav.modeller.LedernivaKjonnAntall
import no.nav.modeller.AldersgruppeKjonnAntall
import no.nav.modeller.AnsiennitetsgruppeKjonnAntall
import no.nav.modeller.KjonnAntall
import no.nav.Konfig

fun hentTotalKjonnStatistikk(prosjektId: String): List<KjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val spørring = """
        SELECT kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY kjonn
    """.trimIndent()

    val queryConfig = QueryJobConfiguration.newBuilder(spørring)
        .setUseLegacySql(false)
        .build()

    val jobId = JobId.of(prosjektId, UUID.randomUUID().toString())
    val queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()

    if (queryJob == null || queryJob.status.error != null) {
        throw RuntimeException("Spørring feilet: ${queryJob?.status?.error}")
    }

    val resultater = queryJob.getQueryResults()
    return resultater.iterateAll().map { rad ->
        KjonnAntall(
            kjonn = rad["kjonn"].stringValue.lowercase(),
            antall = rad["antall"].longValue
        )
    }
}

fun hentKjonnPerAvdeling(projectId: String): List<AvdelingKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_avdeling, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
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
            AvdelingKjonnAntall(
                avdeling = avdeling,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
    return grouped
}

fun hentAldersgruppeKjonnPerAvdeling(projectId: String): List<AvdelingAlderKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_avdeling, aldersgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
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
            AvdelingAlderKjonnAntall(
                avdeling = pair.first,
                aldersgruppe = pair.second,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0,
            )
        }
    return grouped
}

fun hentAnsiennnitetsgruppeKjonnPerAvdeling(projectId: String): List<AvdelingAnsiennitetKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_avdeling, ansiennitetsgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
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
            AvdelingAnsiennitetKjonnAntall(
                avdeling = avdeling,
                ansiennitetsgruppe = rows.first()["ansiennitetsgruppe"].stringValue,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
    return grouped
}

fun hentKjonnPerSeksjon(projectId: String): List<SeksjonKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_seksjon, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
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
            SeksjonKjonnAntall(
                seksjon = seksjon,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}

fun hentAldersgruppeKjonnPerSeksjon(projectId: String): List<SeksjonAlderKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT organisasjon_seksjon, aldersgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
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
            SeksjonAlderKjonnAntall(
                seksjon = seksjon,
                aldersgruppe = rows.first()["aldersgruppe"].stringValue,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}

fun hentKjonnPerRolle(projectId: String): List<RolleKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT rolle, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY rolle, kjonn
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
        .groupBy { it["rolle"].stringValue }
        .map { (rolle, rows) ->
            val kjonnMap = rows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            RolleKjonnAntall(
                rolle = rolle,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}

fun hentKjonnPerLederniva(projectId: String): List<LedernivaKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT lederniva, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY lederniva, kjonn
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
        .groupBy { it["lederniva"].stringValue }
        .map { (nivaa, rows) ->
            val kjonnMap = rows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            LedernivaKjonnAntall(
                lederNiva = nivaa,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}

fun hentKjonnPerAldersgruppe(projectId: String): List<AldersgruppeKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT aldersgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY aldersgruppe, kjonn
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
        .groupBy { it["aldersgruppe"].stringValue }
        .map { (aldersgruppe, rows) ->
            val kjonnMap = rows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            AldersgruppeKjonnAntall(
                aldersgruppe = aldersgruppe,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}

fun hentKjonnPerAnsiennitetsgruppe(projectId: String): List<AnsiennitetsgruppeKjonnAntall> {
    val bigquery = BigQueryOptions.getDefaultInstance().service

    val query = """
        SELECT ansiennitetsgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY ansiennitetsgruppe, kjonn
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
        .groupBy { it["ansiennitetsgruppe"].stringValue }
        .map { (ansiennitetsgruppe, rows) ->
            val kjonnMap = rows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            AnsiennitetsgruppeKjonnAntall(
                ansiennitetsgruppe = ansiennitetsgruppe,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}