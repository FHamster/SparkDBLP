package cn.jmu.spark_dblp.server.controller;

import cn.jmu.spark_dblp.server.entity.AggClass;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import cn.jmu.spark_dblp.server.service.OnlyDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/onlyDoc")
public class OnlyDocController {
    @Autowired
    OnlyDocService service;

    @GetMapping(value = "/findAllByTitleMatchesTextAuthorRefineList")
//    public List<AggClass> findAll(
    public List<AggClass> findAllByTitleMatchesTextAuthorRefineList(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String venue
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

        //处理根据venue的过滤
        String[] venueArray;
        if (venue == null) venueArray = null;
        else venueArray = venue.split(",");
        if (venueArray != null) {
            parallelStream = service.filterByVenue(parallelStream, venueArray);
        }

        //聚合处理
        parallelStream.map(it -> it.getAuthorOption().orElse(new ArrayList<>()))
                .flatMap(List::stream)
                .collect(Collectors.groupingByConcurrent(Author::get_VALUE, Collectors.counting()))
                .forEach((key, value) -> aggClassList.add(new AggClass(key, value)));
//        System.out.println(aggClassList.size());
        aggClassList.sort((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()));
        aggClassList.forEach(System.out::println);
        return aggClassList;
    }

    @GetMapping(value = "/findAllByTitleMatchesTextPrefix2RefineList")
    public List<AggClass> findAllByTitleMatchesTextPrefix2RefineList(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String venue
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

        //聚合处理
        parallelStream
                .collect(Collectors.groupingByConcurrent(OnlyDoc::getPrefix2Option, Collectors.toList()))
                .forEach((key, value) -> {
                    String pro = "";
                    OnlyDoc sample = value.get(0);
                    if (sample.getBooktitle() != null) {
                        pro = sample.getBooktitle();
                    }
                    if (sample.getJournal() != null) {
                        pro = sample.getJournal();
                    }
                    aggClassList.add(new AggClass(key.orElse(""), value.size(), pro));
                });
//        System.out.println(aggClassList.size());
        aggClassList.sort((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()));
        aggClassList.forEach(System.out::println);
        return aggClassList;
    }

    @GetMapping(value = "/findAllByTitleMatchesTextYearRefineList")
    public List<AggClass> findAllByTitleMatchesTextYearRefineList(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String venue
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

        //聚合处理
        parallelStream
                .collect(Collectors.groupingByConcurrent(OnlyDoc::getYear, Collectors.counting()))
                .forEach((key, value) -> aggClassList.add(new AggClass(String.valueOf(key), value)));
//        System.out.println(aggClassList.size());
        aggClassList.sort((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()));
        aggClassList.forEach(System.out::println);
        return aggClassList;
    }
    @GetMapping(value = "/findAllByTitleMatchesTextTypeRefineList")
    public List<AggClass> findAllByTitleMatchesTextTypeRefineList(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String venue
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

        //聚合处理
        parallelStream
                .collect(Collectors.groupingByConcurrent(OnlyDoc::getType, Collectors.counting()))
                .forEach((key, value) -> aggClassList.add(new AggClass(String.valueOf(key), value)));
//        System.out.println(aggClassList.size());
        aggClassList.sort((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount()));
        aggClassList.forEach(System.out::println);
        return aggClassList;
    }
}
