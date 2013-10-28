# crawler-twitter

This is a web application that is divided in three parts:

* **An API to comminicate to twitter REST API** 
* **A crawler that gets twitters and insert them in a MariaDB/Mysql Database**
* **A front end that shows the words with more frequency in the crawled twitters per day**

It is written in scala (using the typesafe activator) and uses slick for access to the database and Play for the front-end.

## How to install and run

Be sure that you have the latest play framework installed in your machine and execute the command play run in the checked out folder.