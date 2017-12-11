package wad.service.dataHandlers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.FileObject;
import wad.domain.News;
import wad.repository.FileObjectRepository;
import wad.repository.NewsRepository;
import wad.service.InputHandler;
import wad.service.validators.FileObjectValidator;

@Transactional
@Service
public class FileObjectHandler {

    @Autowired
    NewsRepository newsRepo;
    @Autowired
    private InputHandler inputHandler;
    @Autowired
    private FileObjectRepository fileRepo;
    @Autowired
    private FileObjectValidator fileValidator;

    public ResponseEntity<byte[]> getFileForView(Long id) {
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
    
    public boolean updateImageOnNews(MultipartFile file, Long id,
            RedirectAttributes attributes) {

        List<String> errors = new ArrayList<>();
        News news = newsRepo.getOne(id);

        FileObject fo = inputHandler.handleFileObjectOpening(file, errors);
        errors.addAll(fileValidator.validate(fo));
        if (errors.isEmpty()) {
            fileRepo.save(fo);
            news.setKuva(fo);
            attributes.addFlashAttribute("success", "Uutinen on onnistuneesti päivitetty!");
            return true;
        } else {
            attributes.addFlashAttribute("errors", errors);
            return false;
        }
    }
}
