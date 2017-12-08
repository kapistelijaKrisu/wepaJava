
package wad.controller.single;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Category;
import wad.domain.News;
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;

@Transactional
@Controller
public class ModeNewsCategoryController {

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;

   //single in edit
    @PostMapping("/modeNews/deleteCategories/{id}")
    public String deleteCategories(
            @PathVariable("id") long id,
            @RequestParam("delCategories") String[] delCategories) {

        News news = newsRepo.getOne(id);        
        handleDeleteCategoryInput(delCategories, news);
        newsRepo.save(news);
        return "redirect:/modeNews/" + news.getId();
    }
    
    @PostMapping("/modeNews/addCategories/{id}")
    public String addCategories(
            @PathVariable("id") long id,
            @RequestParam("categories") String[] delCategories) {

        System.out.println(delCategories.length);
        News news = newsRepo.getOne(id);        
        handleCategoryInput(delCategories, news);
        newsRepo.save(news);
        return "redirect:/modeNews/" + news.getId();
    }
    
    private void handleDeleteCategoryInput(String[] categories, News n) {    
        for (String cat : categories) {
            Category c = catRepo.getOne(Long.parseLong(cat));
            n.deleteCategory(c);
        }
    }

    private void handleCategoryInput(String[] categories, News news) {
        for (String categoryId : categories) {
            Category cat = catRepo.getOne(Long.parseLong(categoryId));
            news.addCategory(cat);
        }
    }
}
