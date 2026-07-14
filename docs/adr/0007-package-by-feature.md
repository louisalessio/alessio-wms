# ADR 0007: Package by Feature

## Status
ACCEPTED

## Context
In traditional Spring Boot applications organized by technical layers (e.g., all controllers in one folder, all services in another), domain logic becomes scattered. This reduces cohesion, makes code navigation difficult as the project grows, and fails to communicate the business intent of the application at a glance (violating the "Screaming Architecture" principle)

## Decision
We will organize the codebase using the "Package by Feature" (or Package by Domain) architectural style. Classes are grouped by their business capability (e.g., `material`, `site`, `inventory`, `movement`). Each package is self-contained and holds everything needed for that specific feature: Entity, Repository, Service, and future Controllers.

## Consequences
Pro: 
-High cohesion and improved readability; developers can find all related components in a single directory
-Microservices readiness; bounded contexts are already isolated, making it significantly easier to extract a feature into a standalone service in the future

Cons: 
-Requires strict architectural discipline; developers must avoid cross-package database access (e.g., `InventoryService` calling `MaterialRepository` directly). Domains must communicate exclusively through Service public APIs to prevent tight coupling