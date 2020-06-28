name := "SparkExp"

version := "0.1"

scalaVersion := "2.12.11"

//useCoursier := false

//logLevel := Level.Debug

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "org.scalatest" %% "scalatest" % "3.1.1" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-core" % "2.4.5" withSources(),
  "org.apache.spark" %% "spark-sql" % "2.4.5" withSources(),
  "com.databricks" %% "spark-xml" % "0.8.0",
  // https://mvnrepository.com/artifact/mysql/mysql-connector-java
  "mysql" % "mysql-connector-java" % "5.1.48",
  "org.mongodb.spark" %% "mongo-spark-connector" % "2.4.1",
)
