package cn.jmu.spark_dblp.server.controller

import cn.jmu.spark_dblp.server.entity.AggClass
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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

@RestController
//@RequestMapping(value = ["/onlyDocs/search"])
//@RequestMapping(value = "/onlyDoc")
@RequestMapping(value = ["/onlyDocs/search"])
class OnlyDocRSQLController {
    @Autowired
    lateinit var service: OnlyDocService

    @GetMapping(value = ["/findAllByRSQL"])
    fun findAllByRSQL(
        @RequestParam title: String?,
        @RequestParam(required = false) filter: String?,
        pageable: Pageable,
        assembler: PagedResourcesAssembler<OnlyDoc>
    ): ResponseEntity<*> {
        val p: Predicate<OnlyDoc> = if (filter != null) {
            QueryConversionPipeline.defaultPipeline().apply(filter, OnlyDoc::class.java)
                .query(InsensitivePredicateVisitor())
        } else Predicate { true }

        //对service的结果流化
        val parallelStream: List<OnlyDoc> = service.findAllByTitleMatchesTextReturnList(title).parallelStream()
            .filter(p)
            .collect(Collectors.toList())


        //初始化聚合结果list
        val onlyDocList = parallelStream
            .sortedWith { o1: OnlyDoc, o2: OnlyDoc -> Math.toIntExact(o2.yearOption.orElse(0L) - o1.yearOption.orElse(0L)) }
        val onlyDocPage: Page<OnlyDoc> = PageImpl(
            onlyDocList.stream()
                .skip(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .collect(Collectors.toList()),
            pageable,
            onlyDocList.size.toLong()
        )
        val model: PagedModel<*> = assembler.toModel(onlyDocPage)
        return ResponseEntity.ok(model)
    }

    @GetMapping(value = ["/findAuthorRefineByRSQL"])
    fun findAuthorRefineByRSQL(
        @RequestParam title: String,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Int>> {
        val p: Predicate<OnlyDoc> = if (filter != null) {
            QueryConversionPipeline.defaultPipeline().apply(filter, OnlyDoc::class.java)
                .query(InsensitivePredicateVisitor())
        } else Predicate { true }

        //对service的结果流化
        val parallelStream = service.findAllByTitleMatchesTextReturnList(title).parallelStream()
            .filter(p)
            .collect(Collectors.toList())

        //聚合处理
        val res = parallelStream.map { it.authorOption.orElse(ArrayList()) }
            .flatten()
            .groupingBy { it._VALUE }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }

        return res
    }

    @GetMapping(value = ["/findPrefix2RefineByRSQL"])
    fun findPrefix2RefineByRSQL(
        @RequestParam title: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Int>> {
        //初始化聚合结果list
        val aggClassList: MutableList<AggClass> = LinkedList()
        //对service的结果流化
        val p: Predicate<OnlyDoc> = if (filter != null) {
            QueryConversionPipeline.defaultPipeline().apply(filter, OnlyDoc::class.java)
                .query(InsensitivePredicateVisitor())
        } else Predicate { true }

        //对service的结果流化
        val list = service.findAllByTitleMatchesTextReturnList(title).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.prefix2 }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }

        return list
    }

    @GetMapping(value = ["/findYearRefineByRSQL"])
    fun findYearRefineByRSQL(
        @RequestParam title: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<Long, Int>> {
        //初始化聚合结果list
        //对service的结果流化
        val p: Predicate<OnlyDoc> = if (filter != null) {
            QueryConversionPipeline.defaultPipeline().apply(filter, OnlyDoc::class.java)
                .query(InsensitivePredicateVisitor())
        } else Predicate { true }

        //对service的结果流化
        val parallelStream = service.findAllByTitleMatchesTextReturnList(title).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.year }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }
        return parallelStream
    }

    @GetMapping(value = ["/findTypeRefineByRSQL"])
    fun findTypeRefineByRSQL(
        @RequestParam title: String?,
        @RequestParam(required = false) filter: String?
    ): List<Pair<String, Int>> {
        //初始化聚合结果list
        //对service的结果流化
        val p: Predicate<OnlyDoc> = if (filter != null) {
            QueryConversionPipeline.defaultPipeline().apply(filter, OnlyDoc::class.java)
                .query(InsensitivePredicateVisitor())
        } else Predicate { true }

        //对service的结果流化
        val parallelStream = service.findAllByTitleMatchesTextReturnList(title).parallelStream()
            .filter(p)
            .collect(Collectors.toList())
            .groupingBy { it.type }
            .eachCount()
            .toList()
            .sortedWith { it1, it2 -> it2.second - it1.second }
        return parallelStream
    }
}

