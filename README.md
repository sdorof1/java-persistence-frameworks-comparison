# Java repository layer frameworks comparison

Comparison of usage of non-JPA SQL mapping (persistence) frameworks for Java (Jooq, Spring JDBCTemplate, MyBatis etc.).

We are not comparing performance, but rather how are these frameworks used for everyday tasks.

We prepared some common scenarios, which you typically need to implement data-centric application, and then we implemented these scenarios using various non-JPA DB layer frameworks. This project should serve
- as point of reference when deciding for SQL mapping framework 
- as a template of common framework usage scenarios (see scenarios below)
- to document best practices of such common usages (**comments are welcomed!**)

## Frameworks compared

* Spring JDBCTemplate (see [implementation](src/main/java/com/clevergang/dbtests/repository/impl/jdbctemplate/JDBCDataRepositoryImpl.java))
* jOOQ (see [implementation](src/main/java/com/clevergang/dbtests/repository/impl/jooq/JooqDataRepositoryImpl.java))

We tried to find optimal (== most readable) implementation in every framework, but comments are welcomed! There are lot of comments explaining why we chose to such implementation and some FIXMEs on places which we do not like, but which cannot be implemented differently or which we have troubles to improve...

## Scenarios implemented

These are the scenarios:

1. Fetch single entity based on primary key
2. Fetch list of entities based on condition
3. Save new single entity and return primary key
4. Batch insert multiple entities of same type and return generated keys
5. Update single existing entity - update all fields of entity at once
6. Fetch many-to-one relation (Company for Department)
7. Fetch one-to-many relation (Departments for Company)
8. Update entities one-to-many relation (Departments in Company) - add two items, update two items and delete one item - all at once
9. Complex select - construct select where conditions based on some boolean conditions + throw in some joins
10. Call stored procedure/function and process results
11. Execute query using JDBC simple Statement (not PreparedStatement)

Each scenario has it's implementation in the Scenarios class. See javadoc of [Scenarios](src/main/java/com/clevergang/dbtests/Scenarios.java) methods for more detailed description of each scenario.

## Model used

![Simple company database model](/SimpleCompanyModel.png?raw=true "Simple company database model")

## How-to

1. Clone the repository
2. Configure PostgreSQL connection details in [application.properties](src/main/resources/application.properties)
3. Create tables and data by running [create-script.sql](sql-updates/create-script.sql)
4. Create one stored procedure by running [register_employee.sql](sql-updates/sql_functions/register_employee.sql)
5. Give the scenarios a test run by running one of the test classes and enjoy :)

## Why only non-JPA?

Well, we were trying to "stick with standard" in our projects so we used JPA in the past, but after many years of JPA usage (Hibernate mostly), we realized it's counterproductive. In most of our projects it caused more problems than it helped to solve - especially in big projects (with lots of tables and relations). There are many reasons of those failures - but the biggest issue is that JPA implementations simply turned into bloatware. Lot of strange magic is happening inside and the complexity is so high, that you need a high-class Hibernate uberexpert in every team so the app actually shows some performance and the code is manageable...

So we dropped JPA completely, started using JDBCTemplate and discovered that we can deliver apps sooner (which was kind of surprising), they are a lot faster (thanks to effective use of DB) and much more robust... This was really relaxing and we do not plan to return to JPA at all... (yes, even for CRUD applications!) 

This project aims to explore other options in the SQL mapping area than just JDBCTemplate. 

**Use code in the repository as you like (MIT License)**
