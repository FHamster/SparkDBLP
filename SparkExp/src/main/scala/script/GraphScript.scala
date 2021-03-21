package script

import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.toSparkSessionFunctions
import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

object GraphScript {
  val spark: SparkSession = SparkSession
    .builder
    .appName("write graph")
    .config("spark.executor.memory", "8g")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  val vertices: RDD[(Long, String)] = spark.loadFromMongoDB(ReadConfig(
    Map("uri" -> s"mongodb://localhost/SparkDBLP.vertex")
  )).select($"id", $"AuthorName")
    .rdd
    .map(it => (it.getLong(0), it.getString(1)))

  val edges: RDD[Edge[String]] = spark.loadFromMongoDB(ReadConfig(
    Map("uri" -> s"mongodb://localhost/SparkDBLP.edges")
  )).select($"fromId", $"toId", $"title")
    .rdd
    .map(it => Edge(it.getLong(0), it.getLong(1), it.getString(2)))

  val graph: Graph[String, String] = Graph(
    vertices = vertices,
    edges = edges,
    edgeStorageLevel = StorageLevel.DISK_ONLY,
    vertexStorageLevel = StorageLevel.MEMORY_ONLY,
    defaultVertexAttr = "default"
  )


}
