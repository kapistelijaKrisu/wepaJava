package wad.controller.newsForm;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.NewsRepository;
import wad.repository.WriterRepository;
import wad.service.validators.NewsValidator;

@Transactional
@Controller
public class ModeNewsWriterController {

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private NewsValidator newsValidator;

    //single in edit
    @DeleteMapping("/modeNews/deleteWriters/{id}")
    public String deleteWriters(
            @PathVariable("id") long id,
            @RequestParam("delWriters") String[] delWriters,
            RedirectAttributes attributes) {

        News news = newsRepo.getOne(id);
        handleDeleteWriterInput(delWriters, news);
        //verify

        List<String> errors = newsValidator.validate(news);
        if (errors.isEmpty()) {
            newsRepo.save(news);
            attributes.addAttribute("success", "Uutiselta on onnistuneesti poistettu kirjoittajat!");
        } else {
            attributes.addAttribute("errors", errors);
            attributes.addAttribute("news", news);
        }
        return "redirect:/modeNews/" + news.getId();
    }

    private void handleDeleteWriterInput(String[] writers, News n) {
        for (String writer : writers) {
            Writer w = writerRepo.getOne(Long.parseLong(writer));
            n.deleteWriter(w);
        }
    }

    @PostMapping("/modeNews/addWriters/{id}")
    public String addWriters(
            @PathVariable("id") long id,
            @RequestParam("writers") String[] delArtists) {

        News news = newsRepo.getOne(id);
        handleWriterInput(delArtists, news);

        newsRepo.save(news);
        return "redirect:/modeNews/" + news.getId();
    }

    private void handleWriterInput(String[] writers, News news) {
        for (String writerId : writers) {
            Writer w = writerRepo.getOne(Long.parseLong(writerId));
            if (w != null) {
                news.addWriter(w);
            }
        }
    }
}
