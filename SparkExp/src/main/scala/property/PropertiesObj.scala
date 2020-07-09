package property

import org.apache.spark.sql.types._

object PropertiesObj {
  val pwd: String = System.getProperty("user.dir")
  val wholeDBLP = "/whole/dblp.xml"
  val wholeDBLP_cvt = "/whole/dblp_cvt.xml"
  lazy val wholeDBLP_cvtSparkPath: String = s"file://${pwd + wholeDBLP_cvt}"
  lazy val wholeDBLP_SparkPath: String = s"file://${pwd + wholeDBLP}"

  lazy val subNode = Seq(
    "article",
    "inproceedings",
    "proceedings",
    "book",
    "incollection",
    "phdthesis",
    "mastersthesis",
    "www",
    //    "person",
    //    "data"
  )

  //手工设定的article schema
  //虽然nullable参数默认是true，但是还是写着
  //我只是喜欢下面这种方式构造schema。其实也可以使用StructType.add的方式添加StructField
  /*
完整的article schema
root
 |-- _cdate: string (nullable = true)
 |-- _key: string (nullable = true)
 |-- _mdate: string (nullable = true)
 |-- _publtype: string (nullable = true)
 |-- author: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _aux: string (nullable = true)
 |    |    |-- _orcid: string (nullable = true)
 |-- booktitle: string (nullable = true)
 |-- cdrom: string (nullable = true)
 |-- cite: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _label: string (nullable = true)
 |-- crossref: string (nullable = true)
 |-- editor: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _orcid: string (nullable = true)
 |-- ee: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- journal: string (nullable = true)
 |-- month: string (nullable = true)
 |-- note: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- number: string (nullable = true)
 |-- pages: string (nullable = true)
 |-- publisher: string (nullable = true)
 |-- title: struct (nullable = true)
 |    |-- _VALUE: string (nullable = true)
 |    |-- _bibtex: string (nullable = true)
 ====================================================这部分要想办法忽略掉
 |    |-- i: array (nullable = true)
 |    |    |-- element: string (containsNull = true)
 |    |-- sub: array (nullable = true)
 |    |    |-- element: string (containsNull = true)
 |    |-- sup: array (nullable = true)
 |    |    |-- element: string (containsNull = true)
 ====================================================
 |-- url: string (nullable = true)
 |-- volume: string (nullable = true)
 |-- year: long (nullable = true)
 */
  val articleSchema: StructType = new StructType(Array(
    StructField("_cdate", StringType, nullable = true),
    StructField("_key", StringType, nullable = true),
    StructField("_mdate", StringType, nullable = true),
    StructField("_publtype", StringType, nullable = true),
    StructField("author", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true),
        StructField("_aux", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("booktitle", StringType, nullable = true),
    StructField("cdrom", StringType, nullable = true),
    StructField("cite", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_label", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("crossref", StringType, nullable = true),
    StructField("editor", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("ee", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("journal", StringType, nullable = true),
    StructField("month", StringType, nullable = true),
    StructField("note", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("number", StringType, nullable = true),
    StructField("pages", StringType, nullable = true),
    StructField("publisher", StringType, nullable = true),
    //人工设定表
    StructField("title", StringType, nullable = true),
    /*    StructField("title", StructType(Array(
          StructField("_VALUE", StringType, nullable = true),
          StructField("_bibtex", StringType, nullable = true),
          /*
          StructField("i", ArrayType(
            StructType(Array(
              StructField("element", StringType, nullable = true),
            )), containsNull = true)
          ),
          StructField("sub", ArrayType(
            StructType(Array(
              StructField("element", StringType, nullable = true),
            )), containsNull = true)
          ),
          StructField("sup", ArrayType(
            StructType(Array(
              StructField("element", StringType, nullable = true),
            )), containsNull = true)
          ),
          */
        )), nullable = true),*/
    StructField("url", StringType, nullable = true),
    StructField("volume", StringType, nullable = true),
    StructField("year", LongType, nullable = true) //long 或 string
  ))

  val inproceedingsSchema = new StructType(Array(
    StructField("_key", StringType, nullable = true),
    StructField("_mdate", StringType, nullable = true),
    StructField("_publtype", StringType, nullable = true),
    StructField("author", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true),
        StructField("_aux", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("booktitle", StringType, nullable = true),
    StructField("cdrom", ArrayType(
      StringType, containsNull = true
    )),
    StructField("cite", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_label", StringType, nullable = true)
      )), containsNull = true)
    ),
    //更改，只有一个记录有两个crossref
    /*StructField("crossref", ArrayType(
      StringType, containsNull = true
    )),*/
    StructField("crossref", StringType, nullable = true),
    StructField("editor", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("ee", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("month", StringType, nullable = true),
    StructField("note", StructType(Array(
      StructField("_VALUE", StringType, nullable = true),
      StructField("_type", StringType, nullable = true)
    )), nullable = true),
    StructField("number", StringType, nullable = true),
    StructField("pages", StringType, nullable = true),
    StructField("title", StringType, nullable = true),
    /* StructField("title", StructType(Array(
      StructField("_VALUE", StringType, nullable = true),
      StructField("_bibtex", StringType, nullable = true),
    )), nullable = true),*/
    StructField("url", StringType, nullable = true),
    StructField("year", LongType, nullable = true)
  ))

  val proceedingsSchema = new StructType(Array(
    StructField("_key", StringType, nullable = true),
    StructField("_mdate", StringType, nullable = true),
    StructField("_publtype", StringType, nullable = true),
    StructField("address", StringType, nullable = true),
    StructField("author", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true),
        StructField("_aux", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("booktitle", StringType, nullable = true),
    StructField("cite", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_label", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("editor", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("ee", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("isbn", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("journal", StringType, nullable = true),
    StructField("note", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("number", StringType, nullable = true),
    StructField("pages", StringType, nullable = true),
    StructField("publisher",
      ArrayType(StringType, containsNull = true)
    ),
    //修改简化
    /*StructField("publisher", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_href", StringType, nullable = true)
      )), containsNull = true)
    ),*/
    StructField("series", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_href", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("title", StringType, nullable = true),
    StructField("url", StringType, nullable = true),
    StructField("volume", StringType, nullable = true),
    StructField("year", LongType, nullable = true)
  ))

  val bookSchema = new StructType(Array(
    StructField("_key", StringType, nullable = true),
    StructField("_mdate", StringType, nullable = true),
    StructField("_publtype", StringType, nullable = true),
    StructField("author", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true),
        StructField("_aux", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("booktitle", StringType, nullable = true),
    StructField("cdrom", StringType, nullable = true),
    StructField("cite", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_label", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("crossref", StringType, nullable = true),
    StructField("editor", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("ee", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("isbn", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("month", StringType, nullable = true),
    StructField("note", StructType(Array(
      StructField("_VALUE", StringType, nullable = true),
      StructField("_type", StringType, nullable = true)
    )), nullable = true),
    StructField("pages", ArrayType(
      StringType, containsNull = true
    )),
    StructField("publisher", StructType(Array(
      StructField("_VALUE", StringType, nullable = true),
      StructField("_href", StringType, nullable = true)
    )), nullable = true),
    StructField("school", ArrayType(
      StringType, containsNull = true
    )),
    StructField("series", StructType(Array(
      StructField("_VALUE", StringType, nullable = true),
      StructField("_href", StringType, nullable = true)
    )), nullable = true),
    StructField("title", StringType, nullable = true),
    StructField("url", StringType, nullable = true),
    StructField("volume", StringType, nullable = true),
    StructField("year", LongType, nullable = true)
  ))

  val incollectionSchema = new StructType(Array(
    StructField("_key", StringType, nullable = true),
    StructField("_mdate", StringType, nullable = true),
    StructField("_publtype", StringType, nullable = true),
    StructField("author", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true),
        StructField("_aux", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("booktitle", StringType, nullable = true),
    StructField("cdrom", StringType, nullable = true),
    StructField("chapter", StringType, nullable = true),
    StructField("cite", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_label", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("crossref", StringType, nullable = true),
    StructField("ee", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("note", StringType, nullable = true),
    StructField("pages", StringType, nullable = true),
    StructField("publisher", StructType(Array(
      StructField("_VALUE", StringType, nullable = true),
      StructField("_href", StringType, nullable = true)
    )), nullable = true),
    StructField("title", StringType, nullable = true),
    StructField("url", StringType, nullable = true),
    StructField("year", LongType, nullable = true)
  ))

  val phdthesisSchema = new StructType(Array(
    StructField("_key", StringType, nullable = true),
    StructField("_mdate", StringType, nullable = true),
    StructField("_publtype", StringType, nullable = true),
    StructField("author", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true),
        StructField("_aux", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("ee", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("isbn", ArrayType(
      StringType, containsNull = true
    )),
    StructField("month", StringType, nullable = true),
    StructField("note", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("pages", ArrayType(
      StringType, containsNull = true
    )),
    StructField("publisher", StringType, nullable = true),
    StructField("school", ArrayType(
      StringType, containsNull = true
    )),
    StructField("series", StructType(Array(
      StructField("_VALUE", StringType, nullable = true),
      StructField("_href", StringType, nullable = true)
    )), nullable = true),
    StructField("title", StringType, nullable = true),
    StructField("volume", StringType, nullable = true),
    StructField("year", LongType, nullable = true)
  ))

  val mastersthesisSchema = new StructType(Array(
    StructField("_key", StringType, nullable = true),
    StructField("_mdate", StringType, nullable = true),
    StructField("author", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true),
        StructField("_aux", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("ee", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_type", StringType, nullable = true)
      )), containsNull = true)
    ),
    StructField("note", StructType(Array(
      StructField("_VALUE", StringType, nullable = true),
      StructField("_type", StringType, nullable = true)
    )), nullable = true),
    StructField("school", StringType, nullable = true),
    StructField("title", StringType, nullable = true),
    StructField("year", LongType, nullable = true),
  ))

  // 我猜这是www的schema，记得改改。——改好了
  val wwwSchema = new StructType(Array(
    StructField("_key", StringType, nullable = true),
    StructField("_mdate", StringType, nullable = true),
    StructField("_publtype", StringType, nullable = true),
    StructField("author", ArrayType(
      StructType(Array(
        StructField("_VALUE", StringType, nullable = true),
        StructField("_orcid", StringType, nullable = true),
        StructField("_aux", StringType, nullable = true)
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


  def main(args: Array[String]): Unit = {
    //    PropertiesObj.InproceedingsSchema.printTreeString()
  }
}

