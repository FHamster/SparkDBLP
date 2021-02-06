package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.entity.AggClass;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.service.CacheService;
import cn.jmu.spark_dblp.server.service.OnlyDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/onlyDocs/restquery")
public class ReSTQueryController {
    @Autowired
    OnlyDocService service;
    @Autowired
    CacheService cache;

    /**
     * 创建ReSTQuery
     */
    @PostMapping
    public String creatReSTQuery(
            @RequestBody Map json
    ) {
        String title = (String) json.get("title");
        UUID uuid = UUID.randomUUID();
        List<OnlyDoc> list2 = cache.putOnlyDocListCache(uuid.toString(), title);
        return uuid.toString();
    }

    @GetMapping(value = "/{queryid}")
    public List<OnlyDoc> getQueryResult(
            @PathVariable("queryid") String queryId
    ) {
        return cache.getOnlyDocListCache(queryId);
    }

    @GetMapping(value = "/findAllByTitleMatchesTextYearRefineList")
    public List<AggClass> findAllByTitleMatchesTextYearRefineList(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String type
    ) {
        //初始化聚合结果list
        List<AggClass> aggClassList = new LinkedList<>();
        //对service的结果流化
        Stream<OnlyDoc> parallelStream = service.findAllByTitleMatchesTextReturnList(title).parallelStream();

        //处理根据author的过滤
        String[] authorArray;
        if (author == null) authorArray = null;
        else authorArray = author.split(",");
        if (authorArray != null) {
            parallelStream = service.filterByAuthor(parallelStream, authorArray);
        }

        //处理根据year的过滤
        String[] yearArray;
        if (year == null) yearArray = null;
        else yearArray = year.split(",");
        if (yearArray != null) {
            parallelStream = service.filterByYear(parallelStream, yearArray);
        }

        //处理根据prefix2的过滤
        String[] venueArray;
        if (venue == null) venueArray = null;
        else venueArray = venue.split(",");
        if (venueArray != null) {
            parallelStream = service.filterByVenue(parallelStream, venueArray);
        }

        //处理根据type的过滤
        String[] typeArray;
        if (type == null) typeArray = null;
        else typeArray = type.split(",");
        if (typeArray != null) {
            parallelStream = service.filterByType(parallelStream, typeArray);
        }

        //聚合处理
        parallelStream
                .collect(Collectors.groupingByConcurrent(OnlyDoc::getYear, Collectors.counting()))
                .forEach((key, value) -> aggClassList.add(new AggClass(String.valueOf(key), value)));
//        System.out.println(aggClassList.size());
        aggClassList.sort((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()));
//        aggClassList.forEach(System.out::println);
        return aggClassList;
    }

    @GetMapping(value = "/findAllByTitleMatchesTextTypeRefineList")
    public List<AggClass> findAllByTitleMatchesTextTypeRefineList(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String type
    ) {
        //初始化聚合结果list
        List<AggClass> aggClassList = new LinkedList<>();
        //对service的结果流化
        Stream<OnlyDoc> parallelStream = service.findAllByTitleMatchesTextReturnList(title).parallelStream();

        //处理根据author的过滤
        String[] authorArray;
        if (author == null) authorArray = null;
        else authorArray = author.split(",");
        if (authorArray != null) {
            parallelStream = service.filterByAuthor(parallelStream, authorArray);
        }

        //处理根据year的过滤
        String[] yearArray;
        if (year == null) yearArray = null;
        else yearArray = year.split(",");
        if (yearArray != null) {
            parallelStream = service.filterByYear(parallelStream, yearArray);
        }

        //处理根据prefix2的过滤
        String[] venueArray;
        if (venue == null) venueArray = null;
        else venueArray = venue.split(",");
        if (venueArray != null) {
            parallelStream = service.filterByVenue(parallelStream, venueArray);
        }

        //处理根据type的过滤
        String[] typeArray;
        if (type == null) typeArray = null;
        else typeArray = type.split(",");
        if (typeArray != null) {
            parallelStream = service.filterByType(parallelStream, typeArray);
        }

        //聚合处理
        parallelStream
                .collect(Collectors.groupingByConcurrent(OnlyDoc::getType, Collectors.counting()))
                .forEach((key, value) -> aggClassList.add(new AggClass(key, value)));
//        System.out.println(aggClassList.size());
        aggClassList.sort((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()));
//        aggClassList.forEach(System.out::println);
        return aggClassList;
    }

    @GetMapping(value = "/findAllByTextReturnList")
    public ResponseEntity findAllByTitleMatchesTextAllList(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String type,
            Pageable pageable,
            PagedResourcesAssembler<OnlyDoc> assembler
    ) {
        //对service的结果流化
        Stream<OnlyDoc> parallelStream = service.findAllByTitleMatchesTextReturnList(title).parallelStream();

        //处理根据author的过滤
        String[] authorArray;
        if (author == null) authorArray = null;
        else authorArray = author.split(",");
        if (authorArray != null) {
            parallelStream = service.filterByAuthor(parallelStream, authorArray);
        }

        //处理根据year的过滤
        String[] yearArray;
        if (year == null) yearArray = null;
        else yearArray = year.split(",");
        if (yearArray != null) {
            parallelStream = service.filterByYear(parallelStream, yearArray);
        }

        //处理根据prefix2的过滤
        String[] venueArray;
        if (venue == null) venueArray = null;
        else venueArray = venue.split(",");
        if (venueArray != null) {
            parallelStream = service.filterByVenue(parallelStream, venueArray);
        }

        //处理根据type的过滤
        String[] typeArray;
        if (type == null) typeArray = null;
        else typeArray = type.split(",");
        if (typeArray != null) {
            parallelStream = service.filterByType(parallelStream, typeArray);
        }

        //初始化聚合结果list
        List<OnlyDoc> onlyDocList = parallelStream
                .sorted((o1, o2) -> Math.toIntExact(o2.getYearOption().orElse(0L) - o1.getYearOption().orElse(0L)))
                .collect(Collectors.toList());


        Page<OnlyDoc> onlyDocPage = new PageImpl<>(
                onlyDocList.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .collect(Collectors.toList()),
                pageable,
                onlyDocList.size()
        );


        PagedModel model = assembler.toModel(onlyDocPage);

        return ResponseEntity.ok(model);

    }
}
