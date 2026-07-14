# ADR 0006: Hybrid Inventory Ledger

## Status
ACCEPTED

## Context
The system must provide current stock availability and a history of all operations. Relying purely on Event Sourcing (calculating current stock dynamically by summing all historical movements) becomes computationally expensive over time; furthermore, a single corrupted or missing record would invalidate the entire system's state. Conversely, simply overwriting the current quantity in place destroys the historical audit trail, making it impossible to track where materials were used

## Decision
We will adopt a hybrid approach:
1. The `inventory` table acts as the Single Source of Truth (SSOT) for the current available quantity, utilizing optimistic locking (`@Version`) to safely handle concurrent updates.
2. The `movement` table acts as an append-only ledger to record the history of operations.
3. Both tables must be updated together as an atomic operation. This will be enforced using Spring's `@Transactional` annotation at the Service layer.

## Consequences
Pro: 
-Optimized read performance for current stock with zero computational overhead on the server.
-Maintains an immutable audit trail of all warehouse operations.

Cons: 
-Introduces strict architectural rigidity; developers must ensure that the `inventory` table is never updated without simultaneously inserting a corresponding `movement` record in the same transaction.