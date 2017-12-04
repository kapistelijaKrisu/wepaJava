package wad.controller;

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

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;

    
    @GetMapping("/news/category/{id}/{pageNro}")
    public String listByCat(Model model, @PathVariable long id, @PathVariable int pageNro) {
        Pageable page = PageRequest.of(pageNro, 5, Sort.Direction.DESC, "published");
        if (id != 0) {
            Category cat = catRepo.findById(id).get();
            List<News> news = newsRepo.findByCategories(cat, page);
            model.addAttribute("news", news);
        } else {
            Page<News> news = newsRepo.findAll(page);
            model.addAttribute("news", news);
        }

        Pageable sort = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "name");
        model.addAttribute("categories", catRepo.findAll(sort));
        return "sorted";
    }
}
