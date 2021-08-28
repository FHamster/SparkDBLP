name := "SparkExp"

version := "0.1"

scalaVersion := "2.12.13"

//useCoursier := false

logLevel := Level.Debug

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "org.scalatest" %% "scalatest" % "3.1.1" withSources(),
  "org.apache.spark" %% "spark-core" % "3.1.1" withSources(),
  "org.apache.spark" %% "spark-sql" % "3.1.1" withSources(),
  "org.apache.spark" %% "spark-graphx" % "3.1.1" withSources(),
  "com.databricks" %% "spark-xml" % "0.8.0" withSources(),
  "org.mongodb.spark" %% "mongo-spark-connector" % "3.0.1" withSources(),
)
