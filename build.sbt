name := """crawler-twitter"""

version := "1.0-SNAPSHOT"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  // Select Play modules
  //jdbc,      // The JDBC connection pool and the play.api.db API
  //anorm,     // Scala RDBMS Library
  //javaJdbc,  // Java database API
  //javaEbean, // Java Ebean plugin
  //javaJpa,   // Java JPA plugin
  //filters,   // A set of built-in filters
  javaCore,  // The core Java API
  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1",
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
  "oauth.signpost" % "signpost-core" % "1.2",
  "oauth.signpost" % "signpost-commonshttp4" % "1.2", 
  "org.apache.httpcomponents" % "httpclient" % "4.2",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "net.liftweb" % "lift-json_2.9.1" % "2.6-M1"
)

play.Project.playScalaSettings

libraryDependencies += "com.typesafe.slick" %% "slick" % "1.0.1"

libraryDependencies += "com.typesafe.play" %% "play-slick" % "0.5.0.2-SNAPSHOT"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.18"
