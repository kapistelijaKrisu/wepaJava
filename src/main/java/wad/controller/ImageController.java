
package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wad.domain.News;
import wad.repository.NewsRepository;

@Transactional
@Controller
public class ImageController {
 
     @Autowired
     NewsRepository newsRepo;
     
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        try {
        News news = newsRepo.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(news.getKuva().getContentType()));
        headers.setContentLength(news.getKuva().getContentLength());
        headers.add("Content-Disposition", "attachment; filename=" + news.getKuva().getName());

        return new ResponseEntity<>(news.getKuva().getContent(), headers, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return null;
        }
    }
}
