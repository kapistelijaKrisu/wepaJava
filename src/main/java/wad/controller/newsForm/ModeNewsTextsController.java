package wad.controller.newsForm;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.News;
import wad.repository.NewsRepository;
import wad.service.validators.NewsValidator;

@Transactional
@Controller
public class ModeNewsTextsController {

    @Autowired
    private NewsValidator newsValidator;
    @Autowired
    private NewsRepository newsRepo;

    // päivitys tekstikenttiin
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
}
