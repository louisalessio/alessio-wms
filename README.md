# Alessio WMS
Warehouse management system built for a small electrical contractor.
The goal is simple: replace "we track inventory in our heads" 
with barcode scanning, movement history, and job site assignment, in the future might be added new features.
Built with Spring Boot and PostgreSQL. Designed to run mainly on mobile

## Stack
- Java 21 / Spring Boot 4.1.0
- PostgreSQL 16 (LTS)
- Flyway (schema migrations)
- Testcontainers (integration tests against real PostgreSQL)
- Lombok

## Architecture decisions
Key design choices are documented in (/docs/adr).

## Prerequisites
- Java 21
- Docker (required for Testcontainers during tests)
- Maven (wrapper included)

## Run locally
Configure your PostgreSQL connection in 
`src/main/resources/application.properties`, then:

```bash
./mvnw spring-boot:run
```

Flyway will handle schema creation on first startup.

## Status
In active development. v1 target: barcode inventory, 
load/unload movements with job site tracking, picking lists.
