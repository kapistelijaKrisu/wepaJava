package wad.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Category;
import wad.domain.Writer;
import wad.repository.CategoryRepository;
import wad.repository.WriterRepository;
import wad.service.ViewInfoGenerator;

@Controller
public class MicromanagementController {

    @Autowired
    private ViewInfoGenerator viewInfo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private CategoryRepository catRepo;

    @GetMapping("/micromanagement")
    public String save(Model model) {
        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());

        model.addAttribute("writers", writerRepo.findAll());
        return "micromanagement";
    }

    //add
    @PostMapping("/category")
    public String addCat(@RequestParam String category) {

        if (catRepo.findByName(category) == null) {
            Category c = new Category(category);
            //pituus validointi ja alphabetic
            catRepo.save(c);
        }

        return "redirect:/micromanagement";
    }

    @PostMapping("/writer")
    public String addWriter(@RequestParam String writer) {
        List<Writer> found = writerRepo.findByName(writer);
        Writer w = new Writer(writer);
        //vali
        if (!found.isEmpty()) {
            //warnign msg 
            
        }
        writerRepo.save(w);
        return "redirect:/micromanagement";
    }

    //del
    @DeleteMapping("/category")
    public String deleteCat(@RequestParam String[] categories) {
        for (String category : categories) {
            Category c = catRepo.getOne(Long.parseLong(category));
            c.setNews(null);
            catRepo.save(c);
            catRepo.delete(c);
        }

        return "redirect:/micromanagement";
    }

    @DeleteMapping("/writer")
    public String deleteWriter(@RequestParam String[] writers) {
        for (String writer : writers) {
            Writer w = writerRepo.getOne(Long.parseLong(writer));
            writerRepo.delete(w);
        }

        return "redirect:/micromanagement";
    }
}
