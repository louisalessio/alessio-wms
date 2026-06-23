# ADR 0004: soft delete

## Status
ACCEPTED

## Context
Some entities like materials and job sites can become inactive through the years, if hard-deleted, movements referencing them would become orphaned, breaking historical traceability 

## Decision
These tables will have a soft delete with "active" column boolean, not null, default true.
ON DELETE RESTRICT on DB level to be sure.
movements table is excluded

## Consequences
Pro: 
-history of movements will always work 
-can reactivate the entities that are deactivated by mistake
-can easily find active entities

Cons: 
-Every read-query has to filter by active
-it will never shrink but based on the expected numbers this won't be an issue
