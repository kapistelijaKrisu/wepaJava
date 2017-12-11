package wad.controller.newsForm;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.*;
import wad.repository.*;
import wad.service.HTMLInfoGenerator;
import wad.service.dataHandlers.NewsHandler;
import wad.service.viewServices.ViewUpdater;

@Transactional
@Controller
public class NewsFormController {

    @Autowired
    private HTMLInfoGenerator viewInfo;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private NewsHandler newsHandler;
    @Autowired
    private ViewUpdater viewUpdater;

    //näytä
    @GetMapping("/news/{id}")
    public String readSingle(Model model, @PathVariable long id) {
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        News news = newsRepo.findById(id).get();
        viewUpdater.addView(news);
        model.addAttribute("news", news);

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "singleNews";
    }
    //uuden formi
    @GetMapping("/newsForm")
    public String addformEmpty(Model model) {
        model.addAttribute("writers", writerRepo.findAll());

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "newsForm";
    }

    //muokkausformi
    @GetMapping("/modeNews/{id}")
    public String addform(Model model, @PathVariable long id) {
        News n = newsRepo.getOne(id);
        model.addAttribute("news", n);

        model.addAttribute("missingWriters", newsHandler.getNotHavingWriters(n));
        model.addAttribute("missingCategories", newsHandler.getNotHavingCategories(n));

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "newsForm";
    }

    //uus uutinen
    @PostMapping("/makeNews")
    public String save(@RequestParam("file") MultipartFile file,
            @RequestParam("label") String otsikko,
            @RequestParam("lead") String ingressi,
            @RequestParam("text") String teksti,
            @RequestParam("categories") String[] categories,
            @RequestParam("writers") String[] writers,
            RedirectAttributes attributes) throws IOException {

        News n = newsHandler.save(file, otsikko, ingressi, teksti, categories, writers, attributes);
        if (n != null) {
            return "redirect:/news/" + n.getId();
        } else {
            return "redirect:/newsForm";
        }

    }

    @DeleteMapping("/news/{id}")
    public String deleteNews(@PathVariable("id") long id) {
        newsHandler.deleteNews(id);
        return "redirect:/";
    }
}
