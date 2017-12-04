package wad.controller;

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

@Controller
public class CategoryController {

    private final int PAGE_SIZE = 5;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;

    @GetMapping("/news/category/{id}/{pageNro}")
    public String listByCat(Model model, @PathVariable long id, @PathVariable int pageNro) {
        Pageable page = PageRequest.of(pageNro, PAGE_SIZE, Sort.Direction.DESC, "published");
        int pageCount = 0;
        if (id != 0) {
            Category cat = catRepo.findById(id).get();
            List<News> news = newsRepo.findByCategories(cat, page);
            model.addAttribute("news", news);

            pageCount = newsRepo.findByCategories(
                    cat, PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "published")).size() / PAGE_SIZE;

        } else {
            Page<News> news = newsRepo.findAll(page);
            model.addAttribute("news", news);
            pageCount = (int) newsRepo.count();
        }
        List<Integer> pages = new ArrayList<>();
        for (int i = Math.max(pageNro - 10, 0); i < Math.min(pageNro + 10, pageCount); i++) {
            pages.add(i);            
        }
        model.addAttribute("pages", pages);

        Pageable sort = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "name");
        model.addAttribute("categories", catRepo.findAll(sort));

        model.addAttribute("link", "/news/category/" + id + "/");
        return "sorted";
    }
}
