# ADR 0005: Postpone users to v2

## Status
ACCEPTED

## Context
Implementing a full Role-Based Access Control (RBAC) system with user tables and login logic introduces significant overhead. The initial target environment (a small warehouse) prioritizes speed of adoption and execution over strict security restrictions. Delivering the core value (inventory tracking) is the primary goal for v1

## Decision
We will postpone formal user authentication and authorization to v2. In v1, we will track the operator performing the action using a simple free-text `String` column named `operator` within the `movement` table. The frontend application will pass the operator's name directly during the API call

## Consequences
Pro: 
-Significantly faster time-to-market for the core features
-Zero friction for warehouse workers to adopt the application (no forgotten passwords or login blocks)

Cons: 
-Lack of strict auditability; the operator name relies on client-side input and cannot be strictly verified
-Will require a data migration strategy for the `operator` column when formal relational users are introduced in v2