/*
==============================onlyDoc索引建立==============================
 */

//建立onlyDoc.crossref的single索引
db.onlyDoc.createIndex({crossref: -1});
//测试查询crossref
db.onlyDoc.find({crossref: "conf/southchi/2013chikdd"})

//建立onlyDoc.year的single索引
db.onlyDoc.createIndex({year: -1});
//使用onlyDoc.year的索引辅助排序的测试
db.onlyDoc.find({crossref: "conf/southchi/2013chikdd"}).sort({year: -1})

//建立onlyDoc的text索引 使用title作为text索引的分词对象
-- db.onlyDoc.createIndex({title: "text", "author._VALUE": "text"});
db.onlyDoc.createIndex({title: "text"});
//测试onlyDoc的text索引
db.onlyDoc.find({$text: {$search: "hadoop"}});

//建立onlyDoc.author._VALUE的single索引
db.onlyDoc.createIndex({'author._VALUE': 1});
//测试onlyDoc.author._VALUE的single索引
db.onlyDoc.find({"author._VALUE": "Alexey A. Belov"})

//建立onlyDoc.{prefix2,volume}的组合索引
db.onlyDoc.createIndex({prefix2: 1, volume: 1});
//测试onlyDoc.{prefix2,volume}的组合索引
db.onlyDoc.find({"prefix2": "journals/access/", "volume": "7"});

/*
==============================DistinctAuthor索引建立==============================
 */

//建立DistinctAuthor_VALUE的single索引
db.DistinctAuthor.createIndex({_VALUE: 1});
//测试DistinctAuthor._VALUE的single索引
db.DistinctAuthor.find({_VALUE: "Danfeng Qin"});
//建立DistinctAuthor_VALUE的single索引
db.DistinctAuthor.createIndex({prefixIndex: 1});
//测试DistinctAuthor._VALUE的single索引
db.DistinctAuthor.find({prefixIndex: /^Dan/ });
/*
==============================venue索引建立==============================
 */

//建立venue.venue.{booktitle,title}的text索引
db.venue.createIndex({'venue.booktitle': "text", 'venue.title': "text",});
//测试venue.{booktitle,title}的text索引
db.venue.find({$text: {$search: "big"}});

//建立venue.booktitle的single索引
db.venue.createIndex({booktitle: 1});
//测试venue.booktitle的single索引
db.venue.find({booktitle: /^SPML/});

/*
==============================journalIndex索引建立==============================
 */


// db.onlyDoc.dropIndex('author._VALUE_1')
// db.onlyDoc.dropIndex('year_1')
// db.onlyDoc.dropIndex('title_text')
// db.onlyDoc.dropIndex('year_-1_title_1')

db.onlyDoc.getIndexes()
db.venue.getIndexes()
db.DistinctAuthor.getIndexes()

