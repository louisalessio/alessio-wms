# ADR 0002: concurrency control strategy

## Status
ACCEPTED

## Context
If two workers read the same row and both attempt to write an update, the second write may silently overwrite the first (Lost Update problem)

## Decision
The pessimistic locking is excluded because it locks the row for the entire duration of the transaction, reducing throughput unnecessarily for a low-conflict workload.
The optimistic locking will be used with @Version in the warehouse entity as column

## Consequences
Pro:
-The optimistic locking is better for a read-heavy workload and low conflict probability (since the expected business is medium-small)

Cons:
-If two workers try to update the same row of the warehouse simultaneously the output will be an error (OptimisticLockingFailureException) requiring proper UX handling ("Error updating the row, retry")