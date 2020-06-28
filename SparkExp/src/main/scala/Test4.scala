import org.apache.spark.sql.types._

object Test4 {

  def main(args: Array[String]): Unit = {
    val IntoSchema = new StructType(Array(
      StructField("_key", StringType, nullable = true),
      StructField("_mdate", StringType, nullable = true),
      StructField("_publtype", StringType, nullable = true),
      StructField("author", ArrayType(
        StructType(Array(
          StructField("_VALUE", StringType, nullable = true),
          StructField("_bibtex", StringType, nullable = true)
        )), containsNull = true)
      ),
      StructField("cite", ArrayType(
        StringType, containsNull = true
      )),
      StructField("crossref", StringType, nullable = true),
      StructField("editor", ArrayType(
        StringType, containsNull = true
      )),
      StructField("ee", StringType, nullable = true),
      StructField("note", ArrayType(
        StructType(Array(
          StructField("_VALUE", StringType, nullable = true),
          StructField("_label", StringType, nullable = true),
          StructField("_type", StringType, nullable = true)
        )), containsNull = true)
      ),
      StructField("title", StringType, nullable = true),
      StructField("url", ArrayType(
        StructType(Array(
          StructField("_VALUE", StringType, nullable = true),
          StructField("_type", StringType, nullable = true)
        )), containsNull = true)
      ),
      StructField("year", LongType, nullable = true)
    ))

    IntoSchema.printTreeString()
  }
}