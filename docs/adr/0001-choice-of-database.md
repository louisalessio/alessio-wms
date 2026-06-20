#ADR 0001: choice of database

##Status
ACCEPTED

##Context
The choice of database is between PostgreSQL (relational DB), MongoDB (NoSQL) and H2 (In-Memory)

##Decision
PostgreSQL will be used

##Consequences
Since the wms manages bar codes, warehouse movements and construction sites (projects) we need to save datas in the disk, so in-memory databases like H2 and NoSQL like MongoDB for the rigidity of the accepted data and ACID system are not the best for our application

-ACID transitions and data integrity: to guarantee that a movement will update also the warehouse
-Relationships: the rigidity of the scheme guarantees that only clean rows will be added
-Performance: Since the objects in the warehouse will be thousands and identified by the barcode, the b-tree index will be the best to search between them