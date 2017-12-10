package wad.controller.newsForm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import wad.service.InputHandler;
import wad.service.HTMLInfoGenerator;
import wad.service.validators.FileObjectValidator;
import wad.service.validators.NewsValidator;

@Transactional
@Controller
public class NewsFormController {

    @Autowired
    private HTMLInfoGenerator viewInfo;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private FileObjectRepository fileRepo;
    @Autowired
    private FileObjectValidator fileValidator;
    @Autowired
    private NewsValidator newsValidator;
    @Autowired
    private InputHandler inputHandler;

    //get new news form
    @GetMapping("/newsForm")
    public String addformEmpty(Model model) {
        model.addAttribute("writers", writerRepo.findAll());

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "newsForm";
    }

    //get edit form
    @GetMapping("/modeNews/{id}")
    public String addform(Model model, @PathVariable long id) {
        News n = newsRepo.getOne(id);
        model.addAttribute("news", n);

        model.addAttribute("missingWriters", getNotHavingWriters(n));
        model.addAttribute("missingCategories", getNotHavingCategories(n));

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "newsForm";
    }

    //uuden posti
    @PostMapping("/makeNews")
    public String save(@RequestParam("file") MultipartFile file,
            @RequestParam("label") String otsikko,
            @RequestParam("lead") String ingressi,
            @RequestParam("text") String teksti,
            @RequestParam("categories") String[] categories,
            @RequestParam("writers") String[] writers,
            RedirectAttributes attributes) throws IOException {

        News news = new News(otsikko, ingressi, teksti, null);
        inputHandler.handleCategoryInput(news, categories);
        inputHandler.handleWritersInput(news, writers);

        List<String> errors = new ArrayList<>();
        FileObject fo = inputHandler.handleFileObjectOpening(file, errors);
        errors.addAll(fileValidator.validate(fo));
        news.setKuva(fo);
        errors.addAll(newsValidator.validate(news));

        if (errors.isEmpty()) {
            fileRepo.save(fo);
            newsRepo.save(news);
            attributes.addFlashAttribute("success", "Uutinen on onnistuneesti julkaistu!");
            return "redirect:/modeNews/" + news.getId();
        } else {
            System.out.println("aaaaa");
            attributes.addFlashAttribute("errors", errors);
            return "redirect:/newsForm";
        }

    }

    @DeleteMapping("/news/{id}")
    public String deleteNews(@PathVariable("id") long id) {
        News news = newsRepo.getOne(id);
        newsRepo.delete(news);
        return "redirect:/";
    }

    private List<Category> getNotHavingCategories(News n) {
        List<Category> cats = catRepo.findAll();
        cats.removeAll(n.getCategories());
        return cats;
    }

    private List<Writer> getNotHavingWriters(News n) {
        List<Writer> writers = writerRepo.findAll();
        writers.removeAll(n.getWriters());
        return writers;
    }

}
