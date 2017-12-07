package wad.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.*;
import wad.repository.*;
import wad.service.TimeService;
import wad.service.ViewUpdater;

@Controller
public class SingleNewsController {

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
    
    
    @GetMapping("/add")
    public String addform(Model model) {
        return "add";
    }
//get single
    @GetMapping("/news/{id}")
    public String readSingle(Model model, @PathVariable Long id) {
        News news = newsRepo.findById(id).get();
        viewUpdater.addView(news);
        model.addAttribute("news", news);
        return "single";

    }
//lisää
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
}