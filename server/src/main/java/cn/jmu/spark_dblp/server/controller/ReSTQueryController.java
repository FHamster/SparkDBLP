package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.model.Md5Model;
import cn.jmu.spark_dblp.server.service.CacheServiceScalaImpl;
import cn.jmu.spark_dblp.server.service.OnlyDocService;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/onlyDocs/restquery")
public class ReSTQueryController {
    @Autowired
    OnlyDocService service;
    @Autowired
    CacheServiceScalaImpl cache;

    QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

    /**
     * 创建ReSTQuery
     *
     * @return
     */
    @PostMapping
    public Md5Model creatReSTQuery(
            @Param("rsql") String rsql
    ) {

//        String title = (String) json.get("title");
        //md5作为资源表述
        String md5 = DigestUtils.md5DigestAsHex(rsql.getBytes(StandardCharsets.UTF_8));
        Md5Model md5Model = new Md5Model(md5);

//        Condition<GeneralQueryBuilder> condition = pipeline.apply(rsql, OnlyDoc.class);

        //存储上下文环境
        List<String> context = new ArrayList<>();
        context.add(rsql);
        cache.pushContext(md5, context);

//        Properties properties = new Properties();
//        properties.setProperty("uuid", uuid);
        /*return EntityModel.of(properties,
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(uuid))
                        .withRel("queryHandler"),
                linkTo(methodOn(ReSTQueryController.class).creatReSTQuery(null))
                        .withSelfRel());*/
        return md5Model.add(
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(md5))
                        .withRel("queryHandler"),
                linkTo(methodOn(ReSTQueryController.class).patchQueryResult(md5, rsql))
                        .withRel("patchQueryHandler"),
                linkTo(methodOn(ReSTQueryController.class).creatReSTQuery(rsql))
                        .withSelfRel()
        );
    }

    @PatchMapping(value = "/{queryid}")
    public ResponseEntity<CollectionModel<OnlyDoc>> patchQueryResult(
            @PathVariable("queryid") String md5,
            @Param("rsql") String rsql
    ) {

        String queryId = "a";
        //取出上下文环境
        List<String> context = cache.getContext(md5);

        //更新上下文环境
        context.add(rsql);
        cache.pushContext(md5, context);

        Condition<GeneralQueryBuilder> condition = pipeline.apply(rsql, OnlyDoc.class);
        Predicate<OnlyDoc> predicate = condition.query(new PredicateVisitor<>());
//        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(md5)._1.stream()
//                .filter(predicate)
//                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
//                onlyDocList,
                new ArrayList<>(),
                linkTo(methodOn(ReSTQueryController.class).patchQueryResult(queryId, rsql)).withSelfRel(),
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(queryId)).withRel("queryHandler")
        ));
    }

    @PutMapping(value = "/{queryid}")
    public ResponseEntity<CollectionModel<OnlyDoc>> putQueryResult(
            @PathVariable("queryid") String md5,
            @RequestBody List<String> querys
    ) {
        //覆盖上下文环境
        cache.pushContext(md5, querys);


        return ResponseEntity.ok(CollectionModel.of(
                null,
//                onlyDocList,
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(md5)).withRel("queryHandler")
        ));
    }

    @GetMapping(value = "/{queryid}")
    public ResponseEntity<CollectionModel<OnlyDoc>> getQueryResult(
            @PathVariable("queryid") String queryId
    ) {
        //读取上下文环境
        List<String> l = cache.getContext(queryId);
        l.stream().collect(Collectors.joining(";"));

//        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(queryId)._1;
        return ResponseEntity.ok(CollectionModel.of(
                new ArrayList<>(),
//                onlyDocList,
                linkTo(methodOn(ReSTQueryController.class)
                        .getQueryResult(queryId))
                        .withSelfRel()
        ));
    }

/*

    @GetMapping(value = "/{queryid}")
    public ResponseEntity<PagedModel<OnlyDoc>> getQueryResult(
            @PathVariable("queryid") String queryId,
            Pageable pageable,
            PagedResourcesAssembler assembler
    ) {

        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(queryId);
        Page<OnlyDoc> onlyDocPage = new PageImpl<OnlyDoc>(
                onlyDocList.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .collect(Collectors.toList()),
                onlyDocList.size()
        );


//        PagedModel model = assembler.toModel(onlyDocPage);
        return new ResponseEntity<PagedModel<OnlyDoc>>(assembler.toModel(onlyDocPage), HttpStatus.OK);
      */
/*  return PagedModel.of(
                onlyDocList,
                new PagedModel.PageMetadata(
                        Long.parseLong(size.orElse("30")),
                        Long.parseLong(number.orElse("0")),
                        onlyDocList.size()),
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(queryId, null, null))
                        .withRel("queryHandler")*//*

        );
*/

}

/*

    @GetMapping(value = "/{queryid}")
    public PagedModel<OnlyDoc> getQueryResult(
            @PathVariable("queryid") String queryId,
            @RequestParam(required = false, defaultValue = "30") Optional<String> size,
            @RequestParam(required = false, defaultValue = "0") Optional<String> number
    ) {

        Pageable.unpaged()

        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(queryId);
        Page<OnlyDoc> onlyDocPage = new PageImpl<OnlyDoc>(
                onlyDocList.stream()
                        .skip(pageable.getOffset())
                        .limit(size)
                        .collect(Collectors.toList()),
                onlyDocList.size()
        );


//        PagedModel model = assembler.toModel(onlyDocPage);

        return PagedModel.of(
                onlyDocList,
                new PagedModel.PageMetadata(
                        Long.parseLong(size.orElse("30")),
                        Long.parseLong(number.orElse("0")),
                        onlyDocList.size()),
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(queryId, null, null))
                        .withRel("queryHandler")
        );

    }
*/


