package wad.controller.newsForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.service.dataHandlers.FileObjectHandler;
import wad.service.dataHandlers.NewsHandler;

@Transactional
@Controller
public class NewsModeController {

    @Autowired
    private NewsHandler newsHandler;
    @Autowired
    private FileObjectHandler fileHandler;

    //single in edit
    @DeleteMapping("/modeNews/deleteCategories/{id}")
    public String deleteCategories(
            @PathVariable("id") long id,
            @RequestParam("delCategories") String[] delCategories,
            RedirectAttributes attributes) {

        newsHandler.deleteCategoriesFromNews(id, delCategories, attributes);
        return "redirect:/modeNews/" + id;
    }

    @PostMapping("/modeNews/addCategories/{id}")
    public String addCategories(
            @PathVariable("id") long id,
            @RequestParam("categories") String[] delCategories) {

        newsHandler.addCategories(id, delCategories);
        return "redirect:/modeNews/" + id;
    }

    @DeleteMapping("/modeNews/deleteWriters/{id}")
    public String deleteWriters(
            @PathVariable("id") long id,
            @RequestParam("delWriters") String[] delWriters,
            RedirectAttributes attributes) {

        newsHandler.deleteWritersFromNews(id, delWriters, attributes);
        return "redirect:/modeNews/" + id;
    }

    @PostMapping("/modeNews/addWriters/{id}")
    public String addWriters(
            @PathVariable("id") long id,
            @RequestParam("writers") String[] writers) {

        newsHandler.addWritersToNews(id, writers);
        return "redirect:/modeNews/" + id;
    }

    @PostMapping("/modeNews/{id}")
    public String save(@RequestParam("label") String label,
            @RequestParam("lead") String lead,
            @RequestParam("text") String text,
            @PathVariable("id") long id,
            RedirectAttributes attributes) {

        newsHandler.modeTextValuesFromNews(label, lead, text, id, attributes);
        return "redirect:/modeNews/" + id;
    }

    @PostMapping("/modeNews/{id}/image")
    public String updateImageOnNews(@RequestParam("file") MultipartFile file,
            @PathVariable Long id,
            RedirectAttributes attributes) {

        fileHandler.updateImageOnNews(file, id, attributes);
        return "redirect:/modeNews/" + id;
    }
}
