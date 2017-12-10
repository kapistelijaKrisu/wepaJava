package wad.controller.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import wad.service.validators.FileObjectValidator;
import wad.service.validators.NewsValidator;
import wad.service.viewServices.ViewUpdater;

@Transactional
@Controller
public class SingleNewsController {

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
    ViewUpdater viewUpdater;

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

    //new form
    @GetMapping("/newsForm")
    public String addformEmpty(Model model) {
        model.addAttribute("writers", writerRepo.findAll());

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "newsForm";
    }

    // edit form
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

    //luonti
    @PostMapping("/makeNews")
    public String save(@RequestParam("file") MultipartFile file,
            @RequestParam("label") String otsikko,
            @RequestParam("lead") String ingressi,
            @RequestParam("text") String teksti,
            @RequestParam("categories") String[] categories,
            @RequestParam("writers") String[] writers,
            RedirectAttributes attributes) throws IOException {

        News news = new News(otsikko, ingressi, teksti, null);
        handleCategoryInput(news, categories);
        handleWritersInput(news, writers);

        List<String> errors = new ArrayList<>();
        FileObject fo = handleFileObjectOpening(file, errors);
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

    @PostMapping("/modeNews/{id}")
    public String save(@RequestParam("label") String label,
            @RequestParam("lead") String lead,
            @RequestParam("text") String text,
            @PathVariable("id") long id,
            RedirectAttributes attributes) {

        News news = newsRepo.getOne(id);
        news.setIngressi(lead);
        news.setLabel(label);
        news.setText(text);

        List<String> errors = new ArrayList<>();
        errors.addAll(newsValidator.validate(news));

        if (errors.isEmpty()) {
            newsRepo.save(news);
            attributes.addFlashAttribute("success", "Uutinen on onnistuneesti päivitetty!");
        } else {
            attributes.addFlashAttribute("errors", errors);
        }
        return "redirect:/modeNews/" + news.getId();
    }

    //kuvan muokkaus
    @PostMapping("/modeNews/{id}/image")
    public String updateImageOnNews(@RequestParam("file") MultipartFile file,
            @PathVariable Long id,
            RedirectAttributes attributes) {

        List<String> errors = new ArrayList<>();
        News news = newsRepo.getOne(id);

        FileObject fo = handleFileObjectOpening(file, errors);
        errors.addAll(fileValidator.validate(fo));
        if (errors.isEmpty()) {
            fileRepo.save(fo);
            news.setKuva(fo);
            attributes.addFlashAttribute("success", "Uutinen on onnistuneesti päivitetty!");
        } else {
            attributes.addAttribute("errors", errors);
        }
        return "redirect:/modeNews/" + news.getId();
    }

    private FileObject handleFileObjectOpening(MultipartFile file, List<String> errors) {
        try {
            return new FileObject(null, file.getName(), file.getContentType(), file.getSize(), file.getBytes());
        } catch (IOException e) {
            errors.add("Kuvan käsittelyssä tapahtui virhe. Ehkä polku on virheellinen?");
        }
        return null;
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

    private void handleCategoryInput(News news, String[] categories) {
        for (String categoryId : categories) {
            Category cat = catRepo.getOne(Long.parseLong(categoryId));
            news.addCategory(cat);
        }
    }

    private void handleWritersInput(News news, String[] writers) {
        for (String writerId : writers) {
            Writer writer = writerRepo.getOne(Long.parseLong(writerId));
            news.addWriter(writer);
        }
    }
}
