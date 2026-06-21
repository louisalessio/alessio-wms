# ADR 0003: Database Migration Strategy

## Status
Accepted

## Context
The application needs to track the evolution of the DB schema like indexes, tables etc. as it does with the code with Git.

## Decision
Letting Hibernate update tables automatically is dangerous in production (when ddl-auto = update).
Flyway will be added as dependency in the pom.xml file so the updates to the DB tables will be sequential and can be found in src/main/resources/db/migration/

## Consequences 
Pro:
-Explicit schema control: when a name is changed it, Hibernate doesn't find the column so creates a new column with the new name leaving the data in the old column since they are now separated, this doesn't happen since the update of the schema comes from the written SQL only
-Version Control: since the versioning of the DB by Flyway is done by a sql file inside the project, is tracked as normal file tracked in Git like any other source file.
Also need to be careful with the versioning that might cause crash (2 files ex. "V05", coming from the merge of 2 branches): to fix this the versioning on this project will be using the timestamp, ex. V202606211653__creation_user_table.sql (2026-06-21 16:53)

Cons:
-The Cost: using Flyway means that the DB will need hand-written SQL code and the java equivalent the files so it will be a slower but much safer process than using Hibernate.
-A failed migration in production blocks application startup entirely. Requires manual intervention: fix the SQL, then run flyway repair before restarting.

