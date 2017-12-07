package wad.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.FileObject;
import wad.domain.News;
import wad.repository.CategoryRepository;
import wad.repository.FileObjectRepository;
import wad.repository.NewsRepository;
import wad.repository.ViewRepository;
import wad.service.ViewInfoGenerator;
import wad.service.ViewUpdater;

public class AdminController {

    @Autowired
    private ViewInfoGenerator viewInfo;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private FileObjectRepository fileRepo;
    @Autowired
    private ViewRepository viewRepo;
    @Autowired
    ViewUpdater viewUpdater;

    @PostMapping("/news")
    public String save(@RequestParam("file") MultipartFile file,
            @RequestParam("otsikko") String otsikko,
            @RequestParam("ingressi") String ingressi,
            @RequestParam("text") String teksti
    ) throws IOException {
        if (file != null) {

            String[] fileFrags = file.getOriginalFilename().split("\\.");
            String extension = fileFrags[fileFrags.length - 1];
            // if (!extension.equals("gif") || !file.getContentType().equals("image/gif")) {
            //    return "redirect:/";
            //  }

            FileObject fo = new FileObject(null, file.getName(), file.getContentType(), file.getSize(), file.getBytes());
            fileRepo.save(fo);
            News news = new News(otsikko, ingressi, teksti, fo);
            newsRepo.save(news);
        } else {
            News news = new News(otsikko, ingressi, teksti, null);
            newsRepo.save(news);
        }

        return "redirect:/";
    }
    //add
    @PostMapping("/category")
    public String addCat(@RequestParam String category) {
       
        return "redirect:/";
    }
    @PostMapping("/writer")
    public String addWriter(@RequestParam String category) {
       
        return "redirect:/";
    }
    
    
    //del
    @DeleteMapping("/category")
    public String deleteCat(@RequestParam String category) {
       
        return "redirect:/";
    }
    @DeleteMapping("/writer")
    public String deleteWriter(@RequestParam String category) {
       
        return "redirect:/";
    }
}
