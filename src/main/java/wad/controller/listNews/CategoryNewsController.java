package wad.controller.listNews;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wad.domain.Category;
import wad.domain.News;
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;
import wad.service.ViewInfoGenerator;

@Controller
public class CategoryNewsController {

    private final double PAGE_SIZE = 5.0;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;
@Autowired
    private ViewInfoGenerator viewInfo;

    @GetMapping("/news/category/{catId}/{pageNro}")
    public String listByCat(Model model, @PathVariable long catId, @PathVariable int pageNro) {
        Page<News> news = getNewsBySortType(catId, pageNro);
        model.addAttribute("news", news);
        model.addAttribute("pages", getPageCount(catId, pageNro));
        
        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());


        return "sorted";
    }

    private Page<News> getNewsBySortType(long id, int pageNro) {
        Pageable page = PageRequest.of(pageNro, (int) PAGE_SIZE, Sort.Direction.DESC, "published");
        if (id != 0) {
            Category cat = catRepo.findById(id).get();
            Page<News> news = newsRepo.findByCategories(cat, page);
            return news;

        } else {//kategoria kaikki
            Page<News> news = newsRepo.findAll(page);
            return news;
        }
    }

    private List<Integer> getPageCount(long id, int pageNro) {
        double pageCount = 0;
        if (id != 0) {
            Category cat = catRepo.getOne(id);
            pageCount = newsRepo.findByCategories(
                    cat, PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "published")).getTotalElements() / PAGE_SIZE;

        } else {//kategoria kaikki
            List<News> news = newsRepo.findAll();
            pageCount = newsRepo.count() / PAGE_SIZE;

        }
         List<Integer> pages = new ArrayList<>();
        for (int i = Math.max(pageNro - 10, 0); i < Math.min(pageNro + 10, pageCount); i++) {
            pages.add(i);
        }
        return pages;
    }
}
