package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.model.UuidModel;
import cn.jmu.spark_dblp.server.service.CacheService;
import cn.jmu.spark_dblp.server.service.OnlyDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/onlyDocs/restquery")
public class ReSTQueryController {
    @Autowired
    OnlyDocService service;
    @Autowired
    CacheService cache;

    /**
     * 创建ReSTQuery
     *
     * @return
     */
    @PostMapping
    public UuidModel creatReSTQuery(
            @RequestBody Properties json
    ) {
        String title = (String) json.get("title");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        UuidModel uuidModel = new UuidModel(uuid);


        List<OnlyDoc> list2 = cache.putOnlyDocListCache(uuid, title);

//        Properties properties = new Properties();
//        properties.setProperty("uuid", uuid);
        /*return EntityModel.of(properties,
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(uuid))
                        .withRel("queryHandler"),
                linkTo(methodOn(ReSTQueryController.class).creatReSTQuery(null))
                        .withSelfRel());*/
        return uuidModel.add(
                linkTo(methodOn(ReSTQueryController.class).getQueryResult(uuid))
                        .withRel("queryHandler"),
                linkTo(methodOn(ReSTQueryController.class).creatReSTQuery(null))
                        .withSelfRel()
        );
    }

    @GetMapping(value = "/{queryid}")
    public ResponseEntity<CollectionModel<OnlyDoc>> getQueryResult(
            @PathVariable("queryid") String queryId
    ) {
        List<OnlyDoc> onlyDocList = cache.getOnlyDocListCache(queryId);
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


