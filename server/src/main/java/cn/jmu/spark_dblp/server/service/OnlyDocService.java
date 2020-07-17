package cn.jmu.spark_dblp.server.service;


import cn.jmu.spark_dblp.server.dao.OnlyDocDAO;
import cn.jmu.spark_dblp.server.entity.OnlyDoc;
import cn.jmu.spark_dblp.server.entity.sub.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OnlyDocService {
    @Autowired
    OnlyDocDAO dao;

    @Cacheable(value = "findAllByTitleMatchesTextReturnStream", key = "#title")
    public List<OnlyDoc> findAllByTitleMatchesTextReturnList(String title) {
        return dao.findAllByTitleMatchesTextReturnList(title);
    }

    public Stream<OnlyDoc> filterByYear(Stream<OnlyDoc> stream, String[] yearArray) {
        return stream.filter(onlyDoc -> {
            Long docYear = onlyDoc.getYear().orElse(0L);
            for (String it : yearArray) if (docYear.equals(Long.valueOf(it))) return true;
            return false;
        });
    }

    public Stream<OnlyDoc> filterByVenue(Stream<OnlyDoc> stream, String[] venueArray) {
        return stream.filter(onlyDoc -> {
            String docPrefix2 = onlyDoc.getPrefix2().orElse("");
            for (String it : venueArray) if (docPrefix2.equals(it)) return true;
            return false;
        });
    }

    public Stream<OnlyDoc> filterByAuthor(Stream<OnlyDoc> stream, String[] authorArray) {
        return stream.filter(onlyDoc -> {
            List<String> list = onlyDoc.getAuthor()
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(Author::get_VALUE)
                    .collect(Collectors.toList());
            for (String s : authorArray) if (list.contains(s)) return true;
            return false;
        });
    }
}
