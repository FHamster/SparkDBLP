package execTest

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.toSparkSessionFunctions
import org.apache.spark.graphx.{Edge, Graph, VertexRDD}
import org.apache.spark.sql.functions.{explode, monotonically_increasing_id}
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.scalatest.funsuite.AnyFunSuite
import property._
import script.GraphScript
import script.GraphScript.graph

import scala.language.postfixOps

class WriteGraph extends AnyFunSuite {

  val AuthorTemp = "AuthorTemp"
  val onlyDoc = "onlyDoc"
  val graphDB = "graphDB"
  val indexPattern = "[\\s(),.]"
  val dataBaseName = PropertiesObj.dataBaseName


  test("write edge") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("write graph")
      .master("local[*]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/$dataBaseName.$onlyDoc")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/$dataBaseName.edges")
      //      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/$dataBaseName.$graphDB")
      .getOrCreate()

    import sparkSession.implicits._
    val onlyDocRDD: DataFrame = MongoSpark
      .load(sparkSession)
      .filter($"author" isNotNull)
      .select($"author", $"title")
    //      .cache()
    val vertexes = onlyDocRDD
      .select(explode($"author._VALUE") as "AuthorName")
      .distinct()
      .withColumn("id", monotonically_increasing_id())


    val nodeAndEdges = onlyDocRDD.select(explode($"author._VALUE") as "author", $"title")
      .join(vertexes, $"AuthorName" === $"author", "leftouter")
      .select($"id", $"AuthorName", $"title")

    //          nodeAndEdges.show(100)
    val from = nodeAndEdges
      .withColumnRenamed("id", "fromId")
      .withColumnRenamed("AuthorName", "fromAuthorName")


    val to = nodeAndEdges
      .withColumnRenamed("id", "toId")
      .withColumnRenamed("AuthorName", "toAuthorName")
    val edges = to
      .join(
        from.withColumnRenamed("title", "titleTemp"),
        $"title" === $"titleTemp",
        "leftouter"
      )
      .select($"fromId", $"toId", $"fromAuthorName", $"toAuthorName", $"title")
      .filter($"fromId" =!= $"toId")



    /*    val graph = Graph(
          vertices = v,
          edges = e
        )

        graph.edges
          .takeSample(withReplacement = true, 10, 123)
          .foreach(println(_))*/

    //194289476
//    println(edges.count())

    MongoSpark.save(edges.write.mode(SaveMode.Overwrite))
  }
  test("write vertex") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("write graph")
      .master("local[*]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/$dataBaseName.$onlyDoc")
      //      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/$dataBaseName.vertex")
      //      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/$dataBaseName.$graphDB")
      .getOrCreate()

    import sparkSession.implicits._
    val onlyDocRDD: DataFrame = MongoSpark
      .load(sparkSession)
      .filter($"author" isNotNull)
      .select($"author", $"title")
    //      .cache()
    val vertexes = onlyDocRDD
      .select(explode($"author._VALUE") as "AuthorName")
      .distinct()
      .withColumn("id", monotonically_increasing_id())

    //194289476
    println(vertexes.count())

    MongoSpark.save(vertexes.write
        .option("uri", s"mongodb://127.0.0.1/$dataBaseName.vertex")
        .mode(SaveMode.Overwrite)
    )
  }

  test("write graph") {
    val sparkSession: SparkSession = SparkSession
      .builder
      .appName("write graph")
      .config("spark.executor.memory", "4g")
      .master("local[*]")
      .getOrCreate()

    import sparkSession.implicits._

    val vertices = sparkSession.loadFromMongoDB(ReadConfig(
      Map("uri" -> s"mongodb://localhost/$dataBaseName.vertex")
    )).select($"id", $"AuthorName")
      .rdd
      .map(it => (it.getLong(0), it.getString(1)))

    val edges = sparkSession.loadFromMongoDB(ReadConfig(
      Map("uri" -> s"mongodb://localhost/$dataBaseName.edges")
    )).select($"fromId", $"toId", $"title")
      .rdd
      .map(it => Edge(it.getLong(0), it.getLong(1), it.getString(2)))

    val graph = Graph(vertices = vertices, edges = edges)
    //    graph.
  }

  test("1") {
    val ranks: VertexRDD[Double] = GraphScript.graph
      .pageRank(0.0001)
      .vertices

    val spark: SparkSession = SparkSession
      .builder
      .appName("write graph")
      .config("spark.executor.memory", "8g")
      .master("local[*]")
      .getOrCreate()
    //    ranks.sample(withReplacement = true, 0.1, 123)
    //      .foreach(it => println((it._1, it._2)))
    val dfRDD = spark.createDataFrame(ranks).toDF("id", "rank")

    //      .map(it => Row(it._1:Long, it._2:Double))
    //      .toDF()
    //      .
    //    spark.mongodb.output.uri
    MongoSpark.save(
      dfRDD.write
        .option("uri", s"mongodb://127.0.0.1/$dataBaseName.pageRankVertex")
        .mode(SaveMode.Overwrite)
    )

    //    MongoSpark.save(dfRDD.write.mode(SaveMode.Overwrite))
    //    println("Reading from the 'hundredClub' collection:")
    //    MongoSpark.load[Character](sparkSession, ReadConfig(Map("collection" -> "hundredClub"), Some(ReadConfig(sparkSession)))).show()
  }
}