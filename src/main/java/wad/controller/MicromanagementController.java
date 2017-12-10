package wad.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.Category;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.CategoryRepository;
import wad.repository.WriterRepository;
import wad.service.HTMLInfoGenerator;
import wad.service.validators.CategoryValidator;
import wad.service.validators.WriterValidator;
import wad.service.verifiers.CategoryVerifier;
import wad.service.verifiers.WriterVerifier;

@Controller//kirjailija ja kategorian
public class MicromanagementController {

    @Autowired
    private HTMLInfoGenerator htmlInfo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private CategoryValidator categoryValidator;
    @Autowired
    private CategoryVerifier categoryVerifier;
    @Autowired
    private WriterValidator writerValidator;
    @Autowired
    private WriterVerifier writerVerifier;

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
        Category c = new Category(category);
        List<String> errors = categoryValidator.validate(c);
        if (errors.isEmpty()) {
            errors.addAll(categoryVerifier.verifyNew(c));
        }
        if (errors.isEmpty()) {
            attributes.addFlashAttribute("success", "Kategoria on onnistuneesti luotu!");
            categoryRepo.save(c);
        } else {
            attributes.addFlashAttribute("errors", errors);
        }

        return "redirect:/micromanagement";
    }

    @PostMapping("/writer")
    public String addWriter(@RequestParam String writer, RedirectAttributes attributes) {
        Writer w = new Writer(writer);
        List<String> errors = writerValidator.validate(w);

        if (errors.isEmpty()) {//varoitetaan samannimisyydestä //muuta verifiointia ei ole
            List<String> warning = writerVerifier.warn(w);
            if (!warning.isEmpty()) {
                attributes.addFlashAttribute("warnings", warning);
            }
            attributes.addFlashAttribute("success", "Kirjoittaja on onnistuneesti rekisteröity!");
            writerRepo.save(w);
        } else {
            attributes.addFlashAttribute("errors", errors);
        }
        return "redirect:/micromanagement";
    }

    //del
    @Transactional
    @DeleteMapping("/category")
    public String deleteCat(@RequestParam String[] categories, RedirectAttributes attributes) {
        for (String cateId : categories) {
            Category category = categoryRepo.getOne(Long.parseLong(cateId));

            List<String> errors = categoryVerifier.verifyDelete(category);
            if (errors.isEmpty()) {
                for (News aNew : category.getNews()) {
                    aNew.deleteCategory(category);
                }

                categoryRepo.delete(category);
                attributes.addFlashAttribute("success", "Kategoria: " + category.getName() + " on onnistuneesti poistettu D:!");

            } else {
                attributes.addFlashAttribute("errors", errors);
            }
        }

        return "redirect:/micromanagement";
    }

    @Transactional
    @DeleteMapping("/writer")
    public String deleteWriter(@RequestParam String[] writers, RedirectAttributes attributes) {
        for (String writerId : writers) {
            Writer writer = writerRepo.getOne(Long.parseLong(writerId));
            List<String> errors = writerVerifier.verifyDelete(writer);

            if (errors.isEmpty()) {
                for (News aNew : writer.getNews()) {
                    aNew.deleteWriter(writer);
                }
                writerRepo.delete(writer);
                attributes.addFlashAttribute("success", "Kirjailija:" + writer.getName() + " on onnistuneesti poistettu listoilta D:!");
            } else {
                attributes.addFlashAttribute("errors", errors);
            }
        }
        return "redirect:/micromanagement";
    }
}
