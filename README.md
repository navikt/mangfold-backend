# Mangfold Backend API

## Om applikasjonen
Backend-API for å hente mangfoldsdata fra NAV-direktoratet. Applikasjonen gir tilgang til statistikk om kjønnsfordeling på tvers av organisasjonen, inkludert avdelinger, seksjoner, roller og aldersgrupper.

## Teknisk oversikt
- Kotlin/Ktor backend
- Google BigQuery integrasjon
- Deployet på NAIS-plattformen

## Oppsett for utvikling

### Forutsetninger
- Java 21
- Gradle
- Tilgang til NAV's Google Cloud Platform (prosjekt: heda-prod-2664)
- Tilgang til NAIS

### Lokal utvikling

1. **Klon repoet**
```bash
git clone https://github.com/navikt/mangfold-backend.git
cd mangfold-backend
```

2. **Autentiser mot Google Cloud**
```bash
# For BigQuery-tilgang
gcloud auth application-default login
```

2. **Bygg og start applikasjonen:**
```bash
# Bygg prosjektet først
./gradlew build

# Start applikasjonen
./gradlew run
```

Applikasjonen vil være tilgjengelig på `http://localhost:8080`

### Verifiser oppsett
```bash
curl http://localhost:8080/kjonn-statistikk
```

## API-endepunkter

### Total statistikk
```http
GET /kjonn-statistikk
Response:
[
  {
    "kjonn": "kvinne",
    "antall": 150
  }
]
```

### Avdelingsstatistikk
```http
GET /kjonn-per-avdeling
Response:
[
  {
    "gruppe": "IT",
    "kjonnAntall": {
      "kvinne": 100,
      "mann": 200
    }
  }
]
```

### Avdeling med seksjoner
```http
GET /avdelinger-med-seksjoner
Response:
[
  {
    "avdeling": "IT-avdelingen",
    "seksjoner": [
      {
        "gruppe": "Utviklingsseksjonen",
        "kjonnAntall": {
          "kvinne": 40,
          "mann": 80
        }
      },
      {
        "gruppe": "Driftsseksjonen",
        "kjonnAntall": {
          "kvinne": 60,
          "mann": 120
        }
      }
    ]
  }
]
```

### Alle tilgjengelige endepunkter
- `/kjonn-statistikk` - Total kjønnsfordeling
- `/kjonn-per-avdeling` - Kjønnsfordeling per avdeling
- `/alder-kjonn-per-avdeling` - Kjønn og alder per avdeling
- `/ansiennitet-kjonn-per-avdeling` - Kjønn og ansiennitet per avdeling
- `/kjonn-per-seksjon` - Kjønnsfordeling per seksjon
- `/alder-kjonn-per-seksjon` - Kjønn og alder per seksjon
- `/ansiennitet-kjonn-per-seksjon` - Kjønn og ansiennitet per seksjon
- `/lederniva-kjonn-per-seksjon` - Kjønn og ledernivå per seksjon
- `/kjonn-per-rolle` - Kjønnsfordeling per rolle
- `/aldersgruppe-per-rolle` - Kjønn og alder per rolle
- `/kjonn-per-lederniva` - Kjønnsfordeling per ledernivå
- `/kjonn-per-aldersgruppe` - Kjønnsfordeling per aldersgruppe
- `/kjonn-per-ansiennitetsgruppe` - Kjønnsfordeling per ansiennitetsgruppe
- `/nyansatte-per-aar` - Statistikk over nyansettelser per år
- `/avdelinger-med-seksjoner` - Kjønnsfordeling per seksjon gruppert under avdelinger

## Deployment

### NAIS
Applikasjonen er deployet på NAIS og tilgjengelig på:
- Prod: `https://mangfold-backend.intern.nav.no`

**NB:** Krever tilkobling til NAV-nettverk (VPN)

### Miljøvariabler
- `BIGQUERY_PROJECT_ID`: Satt automatisk via NAIS
- BigQuery-autentisering: Håndteres av Workload Identity i NAIS

## Kontakt
Team Heda - [#team-heda](https://nav-it.slack.com/archives/team-heda) på Slack


