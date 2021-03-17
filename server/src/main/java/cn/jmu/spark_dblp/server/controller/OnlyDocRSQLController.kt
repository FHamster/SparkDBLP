package cn.jmu.spark_dblp.server.controller

import cn.jmu.spark_dblp.server.entity.OnlyDoc
import cn.jmu.spark_dblp.server.service.OnlyDocService
import cn.jmu.spark_dblp.server.util.InsensitivePredicateVisitor
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

@RestController
@RequestMapping(value = ["/onlyDocs/search"])
class OnlyDocRSQLController {
    @Autowired
    lateinit var service: OnlyDocService
    private fun parse2Predicate(filter: String?): Predicate<OnlyDoc> {
        return if (filter != null) {
            QueryConversionPipeline.defaultPipeline()
                .apply(filter, OnlyDoc::class.java)
//                .query(PredicateVisitor())
                .query(InsensitivePredicateVisitor())
        } else Predicate { true }
    }
    //=======================title===========================================
    @GetMapping(value = ["/findAllByRSQL"])
    fun findAllByRSQL(
        @RequestParam title: String?,
        @RequestParam(required = false) filter: String?,
        pageable: Pageable,
        assembler: PagedResourcesAssembler<OnlyDoc>
    ): ResponseEntity<*> {
//        println(filter)
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        val parallelStream: List<OnlyDoc> = service.findAllByTitleMatchesTextReturnList(title).parallelStream()
            .filter(p)
            .sorted { o1: OnlyDoc, o2: OnlyDoc -> Math.toIntExact(o2.yearOption.orElse(0L) - o1.yearOption.orElse(0L)) }
            .collect(Collectors.toList())

        //初始化聚合结果list
        val onlyDocPage: Page<OnlyDoc> = PageImpl(
            parallelStream.stream()
                .skip(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .collect(Collectors.toList()),
            pageable,
            parallelStream.size.toLong()
        )
//        val a:CollectionModel<*> = CollectionModel.of()
        val model: PagedModel<*> = assembler.toModel(onlyDocPage)
        return ResponseEntity.ok(model)
    }

    @GetMapping(value = ["/findAuthorRefineByRSQL"])
    fun findAuthorRefineByRSQL(
        @RequestParam title: String,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Int>> {
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        return service.findAllByTitleMatchesTextReturnList(title).parallelStream()
            .filter(p)
            .map { it.authorOption.orElse(ArrayList()) }
            .collect(Collectors.toList())
            .flatten()
            .groupingBy { it._VALUE }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }
    }

    @GetMapping(value = ["/findPrefix2RefineByRSQL"])
    fun findPrefix2RefineByRSQL(
        @RequestParam author: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Pair<String, Int>>> {
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        return service.findAllByTitleMatchesTextReturnList(author).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.prefix2 }
            .fold(Pair("", 0), { acc, t ->
                val a = when {
                    t.booktitle != null -> t.booktitle
                    t.journal != null -> t.journal
                    else -> acc.first
                }
                Pair(a, acc.second + 1)
            })
            .toList()
            .sortedWith { it1, it2 -> it2.second.second - it1.second.second }
    }

    @GetMapping(value = ["/findYearRefineByRSQL"])
    fun findYearRefineByRSQL(
        @RequestParam author: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<Long, Int>> {
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        return service.findAllByTitleMatchesTextReturnList(author).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.year }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }
    }

    @GetMapping(value = ["/findTypeRefineByRSQL"])
    fun findTypeRefineByRSQL(
        @RequestParam author: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Int>> {
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        return service.findAllByTitleMatchesTextReturnList(author).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.type }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }
    }


    //=======================author===========================================
    @GetMapping(value = ["/findAllByAuthorRSQL"])
    fun findAllByAuthorRSQL(
        @RequestParam author: String?,
        @RequestParam(required = false) filter: String?,
        pageable: Pageable,
        assembler: PagedResourcesAssembler<OnlyDoc>
    ): ResponseEntity<*> {
//        println(filter)
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        val parallelStream: List<OnlyDoc> = service.findAllByTitleMatchesTextReturnList(author).parallelStream()
            .filter(p)
            .sorted { o1: OnlyDoc, o2: OnlyDoc -> Math.toIntExact(o2.yearOption.orElse(0L) - o1.yearOption.orElse(0L)) }
            .collect(Collectors.toList())

        //初始化聚合结果list
        val onlyDocPage: Page<OnlyDoc> = PageImpl(
            parallelStream.stream()
                .skip(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .collect(Collectors.toList()),
            pageable,
            parallelStream.size.toLong()
        )
//        val a:CollectionModel<*> = CollectionModel.of()
        val model: PagedModel<*> = assembler.toModel(onlyDocPage)
        return ResponseEntity.ok(model)
    }

    @GetMapping(value = ["/findAuthorRefineByAuthorRSQL"])
    fun findAuthorRefineByAuthorRSQL(
        @RequestParam author: String,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Int>> {
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        return service.findAllByAuthor__VALUE(author).parallelStream()
            .filter(p)
            .map { it.authorOption.orElse(ArrayList()) }
            .collect(Collectors.toList())
            .flatten()
            .groupingBy { it._VALUE }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }
    }

    @GetMapping(value = ["/findPrefix2RefineByAuthorRSQL"])
    fun findPrefix2RefineByAuthorRSQL(
        @RequestParam author: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Pair<String, Int>>> {
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        return service.findAllByAuthor__VALUE(author).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.prefix2 }
            .fold(Pair("", 0), { acc, t ->
                val a = when {
                    t.booktitle != null -> t.booktitle
                    t.journal != null -> t.journal
                    else -> acc.first
                }
                Pair(a, acc.second + 1)
            })
            .toList()
            .sortedWith { it1, it2 -> it2.second.second - it1.second.second }
    }

    /**
     * @deprecated 没用的方法
     */
    @GetMapping(value = ["/findYearRefineByAuthorRSQL"])
    fun findYearRefineByAuthorRSQL(
        @RequestParam author: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<Long, Int>> {
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        return service.findAllByAuthor__VALUE(author).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.year }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }
    }

    @GetMapping(value = ["/findTypeRefineByAuthorRSQL"])
    fun findTypeRefineByAuthorRSQL(
        @RequestParam author: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Int>> {
        val p: Predicate<OnlyDoc> = parse2Predicate(filter)

        //对service的结果流化
        return service.findAllByAuthor__VALUE(author).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.type }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }
    }



}

