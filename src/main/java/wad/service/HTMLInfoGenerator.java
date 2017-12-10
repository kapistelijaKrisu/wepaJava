package wad.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wad.domain.Category;
import wad.domain.News;
import wad.domain.View;
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;
import wad.repository.ViewRepository;

@Service
public class HTMLInfoGenerator {

    private static final int PAGESIZE = 5;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private ViewRepository viewRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private NewsSorterByTotalViews newsSorter;

    public List<News> getMostPopularNews() {
        List<News> newsList = new ArrayList<>();

        newsList = newsRepo.findAll();
        newsList.sort(newsSorter);

        int size = Math.min(newsList.size(), PAGESIZE);
        newsList = newsList.subList(0, size);

        return newsList;
    }

    public Page<News> getNewestNews() {
        Pageable page = PageRequest.of(0, PAGESIZE, Sort.Direction.DESC, "published");
        return newsRepo.findAll(page);
    }

    public Page<Category> getCategoriesByAlphabet() {
        Pageable sort = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "name");
        return catRepo.findAll(sort);
    }

    public List<Integer> listifyPageCount(long size, int pageNro) {
        double pageCount = size / (double) PAGESIZE;

        List<Integer> pages = new ArrayList<>();
        for (int i = Math.max(pageNro - 10, 0); i < Math.min(pageNro + 10, pageCount); i++) {
            pages.add(i);
        }
        return pages;
    }

}
