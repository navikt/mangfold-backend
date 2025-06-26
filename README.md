# NAV Mangfolds-API

Backend for levering av endepunkter for å hente og aggregere mangfoldsdata for ansatte i NAV (direktoratet/etaten), med fokus på kjønnsfordeling, aldersgrupper, ansiennitet og tilhørighet i organisasjonen. Data hentes fra BigQuery og eksponeres via REST-API-endepunkter i Ktor.

## Funksjonalitet
- Henter aggregerte data om kjønnsfordeling, aldersgrupper og ansiennitet per avdeling, seksjon, rolle, ledernivå og andre grupperinger.
- Tilrettelegger for visualisering av mangfold i NAV på gruppenivå – ingen identifiserende persondata eksponeres.

## Teknologi
- [Ktor](https://ktor.io/) – Kotlin-basert webserver
- [Google BigQuery](https://cloud.google.com/bigquery) – Datavarehus for HR-data
- [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) – JSON-serialisering
- [Gradle](https://gradle.org/) – Byggeverktøy

## Kjøring lokalt

1. **Forutsetninger:**
    - Java 17+
    - Tilgang til Google Cloud-prosjektet og BigQuery-tabellen

2. **Autentisering mot Google Cloud:**

   Du kan autentisere på to måter:

   - **Med service account-fil (anbefalt for produksjon):**
     ```bash
     export GOOGLE_APPLICATION_CREDENTIALS=/sti/til/service-account.json
     ```
   - **Med din egen Google-bruker (for lokal testing):**
     ```bash
     gcloud auth application-default login
     unset GOOGLE_APPLICATION_CREDENTIALS
     ```

3. **Sett prosjekt-ID for BigQuery:**
   ```bash
   export BIGQUERY_PROJECT_ID=nav-prosjekt-id
   ```

4. **Bygg og start server:**
   ```bash
   ./gradlew build
   ./gradlew run
   ```

Serveren vil som standard lytte på port 8080.

## API-endepunkter

Her er en oversikt over tilgjengelige endepunkter (alle returnerer aggregerte tall per kjønn):

### Total statistikk
- `GET /kjonn-statistikk`  
  Total kjønnsfordeling (hele NAV).

### Avdelingsrelaterte endepunkter
- `GET /kjonn-per-avdeling`  
  Kjønnsfordeling per avdeling.
- `GET /alder-kjonn-per-avdeling`  
  Kjønnsfordeling per avdeling og aldersgruppe.
- `GET /ansiennitet-kjonn-per-avdeling`  
  Kjønnsfordeling per avdeling og ansiennitetsgruppe.

### Seksjonsrelaterte endepunkter
- `GET /kjonn-per-seksjon`  
  Kjønnsfordeling per seksjon.
- `GET /alder-kjonn-per-seksjon`  
  Kjønnsfordeling per seksjon og aldersgruppe.
- `GET /ansiennitet-kjonn-per-seksjon`  
  Kjønnsfordeling per seksjon og ansiennitetsgruppe.
- `GET /lederniva-kjonn-per-seksjon`  
  Kjønnsfordeling per seksjon og ledernivå.

### Rolle og ledernivå
- `GET /kjonn-per-rolle`  
  Kjønnsfordeling per rolle/stillingsnavn.
- `GET /aldersgruppe-per-rolle`  
  Kjønnsfordeling per rolle og aldersgruppe.
- `GET /kjonn-per-lederniva`  
  Kjønnsfordeling per ledernivå.

### Alders- og ansiennitetsgrupper
- `GET /kjonn-per-aldersgruppe`  
  Kjønnsfordeling per aldersgruppe (på tvers av organisasjonen).
- `GET /kjonn-per-ansiennitetsgruppe`  
  Kjønnsfordeling per ansiennitetsgruppe (på tvers av organisasjonen).

## Videre arbeid
- Flere variabler: lønn, etnisk bakgrunn, funksjonsvariasjoner m.m.

**Kontakt:**  
Team Heda


