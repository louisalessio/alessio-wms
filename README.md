# Alessio WMS
Warehouse management system built for a small electrical contractor.
The goal is simple: replace "we track inventory in our heads" 
with barcode scanning, movement history, and job site assignment, in the future might be added new features.
Built with Spring Boot and PostgreSQL. Designed to run mainly on mobile

## Live Demo
-List materials: `GET https://louisalessio.online/api/materials`
-Check inventory: `GET https://louisalessio.online/api/inventory`
-Load stock: `POST https://louisalessio.online/api/movements/load`
(search by code so the material has to exist)
```json
{
    "materialCode": "WIRE-001",
    "quantity": 10,
    "siteId": 1,
    "operator": "Louis"
}
```
-Unload stock: `POST https://louisalessio.online/api/movements/unload`
```json
{
    "materialCode": "WIRE-001",
    "quantity": 3,
    "siteId": 1,
    "operator": "Louis"
}
```

## Stack
- Java 21 / Spring Boot 4.1.0
- PostgreSQL 16 (LTS)
- Flyway (schema migrations)
- Testcontainers (integration tests against real PostgreSQL)
- Lombok
- Caddy (Reverse Proxy + SSL on AWS EC2)

## Architecture decisions
Key design choices are documented in (/docs/adr).

## Prerequisites
- Java 21
- Docker (required for Testcontainers during tests)
- Maven (wrapper included)

## Run locally
Clone the repository
```bash
git clone https://github.com/louisalessio/alessio-wms.git
cd alessio-wms
```
Create a .env file in the root directory (ex.)
```bash
DB_USER=louisalessio
DB_PASS=pass
DB_NAME=dbname
```

Start PostgreSQL (via Docker or local service):
ensure your local PostgreSQL instance is running and the db is created.
Then run the application
```bash
./mvnw spring-boot:run
```

Flyway will handle schema creation on first startup.

## Status
In active development. v1 target: barcode inventory, 
load/unload movements with job site tracking, picking lists.
