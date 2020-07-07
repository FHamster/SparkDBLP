package cn.jmu.spark_dblp.server.entity;

import cn.jmu.spark_dblp.server.entity.sub.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * WWW 在dblp是用来记录一些author的hompage地址的
 * 不是主要实现目标
 * 将该类标记为废弃作为表示
 * @deprecated 不是主要实现目标
 */
@Document(collection = "www")
@Data
public class Www {
}
/*
root
 |-- _key: string (nullable = true)
 |-- _mdate: string (nullable = true)
 |-- _publtype: string (nullable = true)
 |-- author: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _bibtex: string (nullable = true)
 |-- cite: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- crossref: string (nullable = true)
 |-- editor: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- ee: string (nullable = true)
 |-- note: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _label: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- title: string (nullable = true)
 |-- url: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- _VALUE: string (nullable = true)
 |    |    |-- _type: string (nullable = true)
 |-- year: long (nullable = true)
 */
