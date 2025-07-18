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

### Ledernivå per avdeling
```http
GET /lederniva-kjonn-per-avdeling
Response:
[
  {
    "gruppe1": "Teknologi",
    "gruppe2": "6",
    "kjonnAntall": {
      "kvinne": 15,
      "mann": 22
    }
  },
  {
    "gruppe1": "HR",
    "gruppe2": "5",
    "kjonnAntall": {
      "kvinne": 1
    }
  },
  {
    "gruppe1": "Økonomi",
    "gruppe2": "6",
    "kjonnAntall": {
      "kvinne": 4,
      "mann": 3
    }
  }
]
```

Hvor:
- `gruppe1`: Avdelingsnavn
- `gruppe2`: Ledernivå (3-6, hvor 6 er ikke-leder)
- `kjonnAntall`: Antall kvinner og menn på dette nivået i avdelingen

### Stillingsnavn statistikk
```http
GET /kjonn-per-stilling
Response:
[
  {
    "gruppe": "Seniorrådgiver",
    "kjonnAntall": {
      "kvinne": 150,
      "mann": 200
    }
  }
]
```

### Aldersgrupper per stillingsnavn
```http
GET /aldersgruppe-per-stilling
Response:
[
  {
    "gruppe1": "Seniorrådgiver",
    "gruppe2": "30-39",
    "kjonnAntall": {
      "kvinne": 25,
      "mann": 35
    }
  }
]
```




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


