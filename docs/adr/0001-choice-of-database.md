# ADR 0001: choice of database

## Status
ACCEPTED

## Context
The choice of database is between PostgreSQL (relational DB), MongoDB (NoSQL) and H2 (In-Memory)

## Decision
PostgreSQL will be used

## Consequences
Since the WMS manages bar codes, warehouse movements and construction sites (projects) we need to save data on disk, 
so non production-grade databases (like H2) and NoSQL (like MongoDB) are excluded, as they lack sufficient schema rigidity and ACID compliance which is the best for our application

Pro: 
-ACID transactions and data integrity: to guarantee that a movement will update also the warehouse
-Relationships: the rigidity of the schema guarantees that only clean rows will be added and the relationships with FK are enforced
-Performance: Since the objects in the warehouse will be thousands and identified by the barcode, the b-tree index will be the best to search through them

Cons: 
-Need a PostgreSQL instance running (via Docker or dedicated server) (H2 is embedded, will be used for tests)
-Greater operational overhead