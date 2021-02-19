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
import java.util.List;
import java.util.Properties;
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
            @RequestBody Properties json
    ) {
        String title = (String) json.get("title");
        String md5 = DigestUtils.md5DigestAsHex(title.getBytes(StandardCharsets.UTF_8));
        Md5Model md5Model = new Md5Model(md5);

        cache.push(md5, title);

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
                linkTo(methodOn(ReSTQueryController.class).patchQueryResult(md5, "1"))
                        .withRel("patchQueryHandler"),
                linkTo(methodOn(ReSTQueryController.class).creatReSTQuery(null))
                        .withSelfRel()
        );
    }

    @PatchMapping(value = "/{queryid}")
    public ResponseEntity<CollectionModel<OnlyDoc>> patchQueryResult(
            @PathVariable("queryid") String queryId,
            @Param("rsql") String rsql
    ) {
        Condition<GeneralQueryBuilder> condition = pipeline.apply(rsql, OnlyDoc.class);
        Predicate<OnlyDoc> predicate = condition.query(new PredicateVisitor<>());
        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(queryId)._1.stream()
                .filter(predicate)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
                onlyDocList,
                linkTo(methodOn(ReSTQueryController.class).patchQueryResult(queryId, rsql)).withSelfRel(),
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(queryId)).withRel("queryHandler")
        ));
    }

    @PutMapping(value = "/{queryid}")
    public ResponseEntity<CollectionModel<OnlyDoc>> putQueryResult(
            @PathVariable("queryid") String queryId,
            @RequestBody List<String> querys
    ) {
        //TODO
//        cache.getOnlyDocListCache(queryId, predicates)
//                .filter(predicate)
//                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
                null,
//                onlyDocList,
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(queryId)).withRel("queryHandler")
        ));
    }

    @GetMapping(value = "/{queryid}")
    public ResponseEntity<CollectionModel<OnlyDoc>> getQueryResult(
            @PathVariable("queryid") String queryId
    ) {
        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(queryId)._1;
        return ResponseEntity.ok(CollectionModel.of(
                onlyDocList,
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


