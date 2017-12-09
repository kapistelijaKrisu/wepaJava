
package wad.controller.single;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.NewsRepository;
import wad.repository.WriterRepository;

@Transactional
@Controller
public class ModeNewsWriterController {

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private WriterRepository writerRepo;

   //single in edit
    @DeleteMapping("/modeNews/deleteWriters/{id}")
    public String deleteWriters(
            @PathVariable("id") long id,
            @RequestParam("delWriters") String[] delWriters) {
        
        
        News news = newsRepo.getOne(id);        
        handleDeleteWriterInput(delWriters, news);

        newsRepo.save(news);
        return "redirect:/modeNews/" + news.getId();
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
    
    private void handleDeleteWriterInput(String[] writers, News n) {
        for (String writer : writers) {
           Writer w = writerRepo.getOne(Long.parseLong(writer));
            n.deleteWriter(w);
        }
    }

    private void handleWriterInput(String[] writers,News news) {
        for (String writerId : writers) {
            Writer w = writerRepo.getOne(Long.parseLong(writerId));
            news.addWriter(w);
        }
    }
}
