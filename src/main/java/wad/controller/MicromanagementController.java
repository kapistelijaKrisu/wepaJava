package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.repository.WriterRepository;
import wad.service.HTMLInfoGenerator;
import wad.service.dataHandlers.CategoryHandler;
import wad.service.dataHandlers.WriterHandler;
@Controller//kirjailija ja kategorian
public class MicromanagementController {

    @Autowired
    private HTMLInfoGenerator htmlInfo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    WriterHandler writerHandler;
    @Autowired
    CategoryHandler categoryHandler;
    
    @GetMapping("/micromanagement")
    public String save(Model model) {
        model.addAttribute("newest", htmlInfo.getNewestNews());
        model.addAttribute("categories", htmlInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", htmlInfo.getMostPopularNews());

        model.addAttribute("writers", writerRepo.findAll());
        return "micromanagement";
    }

    //add
    @PostMapping("/category")
    public String addCat(@RequestParam String category, RedirectAttributes attributes) {
        categoryHandler.save(category, attributes);
        return "redirect:/micromanagement";
    }

    @PostMapping("/writer")
    public String addWriter(@RequestParam String writer, RedirectAttributes attributes) {
        writerHandler.save(writer, attributes);
        return "redirect:/micromanagement";
    }

    //del
    @DeleteMapping("/category")
    public String deleteCat(@RequestParam String[] categories, RedirectAttributes attributes) {
        categoryHandler.deleteCategories(categories, attributes);
        return "redirect:/micromanagement";
    }

    
    @DeleteMapping("/writer")
    public String deleteWriter(@RequestParam String[] writers, RedirectAttributes attributes) {
        writerHandler.deleteWriters(writers, attributes);
        return "redirect:/micromanagement";
    }
}
