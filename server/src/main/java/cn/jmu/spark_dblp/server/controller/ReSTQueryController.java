package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.model.ContextModel;
import cn.jmu.spark_dblp.server.service.CacheServiceScalaImpl;
import cn.jmu.spark_dblp.server.service.OnlyDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/onlyDocs/restquery")
public class ReSTQueryController {
    @Autowired
    OnlyDocService service;
    @Autowired
    CacheServiceScalaImpl cache;

    /**
     * POST创建语义的实现
     */
    @PostMapping
    public HttpEntity<ContextModel> postRSQLQueryContext(
            @RequestBody List<String> context
    ) {
        //md5作为资源表述
        String contextID = UUID.randomUUID().toString().replace("-", "");
        //存储上下文环境
        cache.pushContext(contextID, context);

        ContextModel model = new ContextModel(contextID, context).add(
                linkTo(methodOn(ReSTQueryController.class).getRSQLQueryResult(contextID)).withSelfRel()
                        .andAffordance(afford(methodOn(ReSTQueryController.class).patchRSQLQueryContext(contextID, context)))
                        .andAffordance(afford(methodOn(ReSTQueryController.class).putRSQLQueryContext(contextID, context)))
        );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(model);
    }


    /**
     * Patch补充语义的实现
     */
    @PatchMapping(value = "/{contextID}")
    public HttpEntity<ContextModel> patchRSQLQueryContext(
            @PathVariable("contextID") String contextID,
            @RequestBody List<String> patchContext
    ) {
        //取出上下文环境
        List<String> context = cache.getContext(contextID);

        if (context == null) throw new ResourceNotFoundException();

        //更新上下文环境
        context.addAll(patchContext);
        cache.pushContext(contextID, context);

        //获取结果集
//        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(context);

        //设置链接
/*        List<Link> links = Arrays.asList(
                linkTo(methodOn(ReSTQueryController.class).patchRSQLQueryContext(contextID, patchContext)).withSelfRel(),
                linkTo(methodOn(ReSTQueryController.class).getRSQLQueryResult(contextID)).withRel("queryHandler")
        );*/

        ContextModel model = new ContextModel(contextID, context).add(
                linkTo(methodOn(ReSTQueryController.class).getRSQLQueryResult(contextID)).withSelfRel()
                        .andAffordance(afford(methodOn(ReSTQueryController.class).patchRSQLQueryContext(contextID, context)))
                        .andAffordance(afford(methodOn(ReSTQueryController.class).putRSQLQueryContext(contextID, context)))
        );

        /*        linkTo(methodOn(ReSTQueryController.class).patchRSQLQueryContext(contextID, context)).withSelfRel(),
                linkTo(methodOn(ReSTQueryController.class).putRSQLQueryContext(contextID, context)).withRel("putRSQLQueryContext"),
                linkTo(methodOn(ReSTQueryController.class).DeleteQueryResult(contextID)).withRel("DeleteQueryResult")
        */
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(model);
    }

    /**
     * Put替换语义的实现
     */
    @PutMapping(value = "/{contextID}")
    public HttpEntity<ContextModel> putRSQLQueryContext(
            @PathVariable("contextID") String contextID,
            @RequestBody List<String> context
    ) {
        //取出上下文环境
        if (cache.getContext(contextID) == null) throw new ResourceNotFoundException();

        //覆盖上下文环境
        cache.pushContext(contextID, context);

        ContextModel model = new ContextModel(contextID, context).add(
                linkTo(methodOn(ReSTQueryController.class).getRSQLQueryResult(contextID)).withSelfRel()
                        .andAffordance(afford(methodOn(ReSTQueryController.class).patchRSQLQueryContext(contextID, context)))
                        .andAffordance(afford(methodOn(ReSTQueryController.class).putRSQLQueryContext(contextID, context)))
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(model);
    }

    /**
     * Get获取语义的实现
     */
    @GetMapping(value = "/{contextID}")
    public HttpEntity<ContextModel> getRSQLQueryResult(
            @PathVariable("contextID") String contextID
    ) {
        //读取上下文环境
        List<String> context = cache.getContext(contextID);
        //取出上下文环境
        if (context == null) throw new ResourceNotFoundException();


        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(context);

        ContextModel model = new ContextModel(contextID, context, onlyDocList).add(
                linkTo(methodOn(ReSTQueryController.class).getRSQLQueryResult(contextID)).withSelfRel()
                        .andAffordance(afford(methodOn(ReSTQueryController.class).patchRSQLQueryContext(contextID, context)))
                        .andAffordance(afford(methodOn(ReSTQueryController.class).putRSQLQueryContext(contextID, context)))
        );
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(model);
    }

    /**
     * Delete删除语义的实现
     */
    @DeleteMapping(value = "/{contextID}")
    public HttpEntity<ContextModel> DeleteQueryResult(
            @PathVariable("contextID") String contextID
    ) {
        //取出上下文环境
        if (cache.getContext(contextID) == null) throw new ResourceNotFoundException();

        cache.deleteContext(contextID);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public HttpEntity<ContextModel> handleResourceNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }


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


