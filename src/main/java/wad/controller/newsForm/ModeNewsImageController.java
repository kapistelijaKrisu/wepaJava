
package wad.controller.newsForm;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.FileObject;
import wad.domain.News;
import wad.repository.FileObjectRepository;
import wad.repository.NewsRepository;
import wad.service.InputHandler;
import wad.service.validators.FileObjectValidator;
import wad.service.validators.NewsValidator;

@Transactional
@Controller
public class ModeNewsImageController {
    @Autowired
    private FileObjectRepository fileRepo;
    @Autowired
    private FileObjectValidator fileValidator;
    @Autowired
    private NewsValidator newsValidator;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private InputHandler inputHandler;
    
    //kuvan muokkaus
    @PostMapping("/modeNews/{id}/image")
    public String updateImageOnNews(@RequestParam("file") MultipartFile file,
            @PathVariable Long id,
            RedirectAttributes attributes) {

        List<String> errors = new ArrayList<>();
        News news = newsRepo.getOne(id);

        FileObject fo = inputHandler.handleFileObjectOpening(file, errors);
        errors.addAll(fileValidator.validate(fo));
        if (errors.isEmpty()) {
            fileRepo.save(fo);
            news.setKuva(fo);
            attributes.addFlashAttribute("success", "Uutinen on onnistuneesti päivitetty!");
        } else {
            attributes.addFlashAttribute("errors", errors);
        }
        return "redirect:/modeNews/" + news.getId();
    }

}
