# Java repository layer frameworks comparison
Comparison of usage of non-JPA SQL mapping frameworks for Java (Jooq, Spring JDBCTemplate etc.).

We are not comparing performance (that'll be maybe added later), but rather how are these frameworks used for everyday tasks.

We prepared some common scenarios, which you typically need to implement data-centric application, and then we implemented these scenarios using various non-JPA frameworks.

## Why only non-JPA?
Well, we were trying to "stick with standard" in our projects in the past, but after many years of JPA usage
(Hibernate mostly), we realized it's counterproductive. In most of our projects it caused more problems then 
it helped to solve - especially in big projects (with lots of tables and relations).
There are many reasons of those failures - but the biggest issues are that JPA implementations simply turned into bloatware,
lot of strange magic is happening inside and the complexity is so high, that you need a high-class Hibernate uberexpert
in every team so the app actually shows some performance and the code is managable...

So we dropped JPA completely, started using JDBCTemplate and discovered that we can deliver apps sooner
(which was kind of surprising), they are god damn faster (thanks to effective use of DB) and much more robust... 

This project aims to explore other options in the SQL mapping area than just JDBCTemplate. It should serve us 
- as point of reference when deciding for SQL mapping framework 
- and as a template of common DB usage scenarios


Use code in the repository as you like (MIT License)
