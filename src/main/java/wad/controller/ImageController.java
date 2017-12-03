
package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wad.domain.News;
import wad.repository.NewsRepository;

@Controller
public class ImageController {
 
     @Autowired
     NewsRepository newsRepo;
     
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        News fo = newsRepo.findById(id).get();
        if (fo == null) {
            return null;
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fo.getKuva().getContentType()));
        headers.setContentLength(fo.getKuva().getContentLength());
        headers.add("Content-Disposition", "attachment; filename=" + fo.getKuva().getName());

        return new ResponseEntity<>(fo.getKuva().getContent(), headers, HttpStatus.CREATED);
    }
}
