package wad.controller.news;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.Category;
import wad.domain.News;
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;
import wad.service.validators.NewsValidator;

@Transactional
@Controller
public class ModeNewsCategoryController {

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private NewsValidator newsValidator;

    //single in edit
    @DeleteMapping("/modeNews/deleteCategories/{id}")
    public String deleteCategories(
            @PathVariable("id") long id,
            @RequestParam("delCategories") String[] delCategories,
            RedirectAttributes  attributes) {

        News news = newsRepo.getOne(id);
        handleDeleteCategoryInput(delCategories, news);
        List<String> errors = newsValidator.validate(news);
        if (errors.isEmpty()) {
        newsRepo.save(news);
        
        attributes.addAttribute("success", "Uutiselta on onnistuneesti poistettu kategoriat!");
        } else {
            attributes.addAttribute("errors", errors);
        }
        return "redirect:/modeNews/" + news.getId();
    }

    private void handleDeleteCategoryInput(String[] categories, News n) {
        for (String cat : categories) {
            Category c = catRepo.getOne(Long.parseLong(cat));
            n.deleteCategory(c);
        }
    }

    @PostMapping("/modeNews/addCategories/{id}")
    public String addCategories(
            @PathVariable("id") long id,
            @RequestParam("categories") String[] delCategories) {

        News news = newsRepo.getOne(id);
        handleCategoryInput(delCategories, news);
        newsRepo.save(news);
        return "redirect:/modeNews/" + news.getId();
    }

    private void handleCategoryInput(String[] categories, News news) {
        for (String categoryId : categories) {
            Category cat = catRepo.getOne(Long.parseLong(categoryId));
            if (cat != null) {
                news.addCategory(cat);
            }
        }
    }
}
