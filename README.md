# NAV Mangfolds-API

Backend for levering av endepunkter for å hente og aggregere på mangfoldsdata for ansatte i NAV (direktoratet/etaten), med fokus på kjønnsfordeling, aldersgrupper, ansiennitet og tilhørighet i organisasjonen. Data hentes fra BigQuery og eksponeres via REST-API-endepunkter i Ktor.

## Funksjonalitet
- Henter aggregerte data om kjønnsfordeling, aldersgrupper og ansiennitet per avdeling, seksjon og andre grupperinger.
- Tilrettelegger for visualisering av mangfold i NAV på gruppenivå - ingen identifiserende persondata eksponeres.

## Teknologi
- [Ktor](https://ktor.io/) - Kotlin-basert webserver
- [Google BigQuery](https://cloud.google.com/bigquery) - Datavarehus for HR-data
- [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - JSON-serialisering
- [Gradle](https://gradle.org/) - Byggeverktøy

## Kjøring lokalt

1. **Forutsetninger:**
    - Java 17+
    - Google Cloud Service Account med tilgang til aktuell BigQuery-tabell
    - (Valgfritt) Docker, hvis du vil kjøre lokalt i container

<!-- TODO: Oppdatere dette -->
### Autentisering mot Google Cloud
Per nå må du logge inn med din egen Google-bruker for å kjøre applikasjonen lokalt:

```bash
gcloud auth application-default login
unset GOOGLE_APPLICATION_CREDENTIALS
```

Oppdatering om annen autentisering (for produksjon/lansering) kommer senere.




3. **Bygg og start server:**
   ```bash
   ./gradlew build
   ./gradlew run
   ```

4. **Serveren vil som standard lytte på port 8080.**

<!-- TODO: ## Kjøring med Docker -->

## API-endepunkter

### Hent total kjønnsfordeling
```
GET /gender-stats
```

### Kjønnsfordeling per avdeling
```
GET /gender-per-department
```

### Kjønnsfordeling per avdeling og aldersgruppe
```
GET /gender-age-per-department
```

## Videre arbeid
- Flere variabler: lønn, etnisk bakgrunn, funksjonsvariasjoner m.m.

**Kontakt**  
Team Heda


