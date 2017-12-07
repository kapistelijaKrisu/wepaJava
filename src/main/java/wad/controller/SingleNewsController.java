package wad.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.*;
import wad.repository.*;
import wad.service.ViewInfoGenerator;
import wad.service.ViewUpdater;

@Controller
public class SingleNewsController {

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

    //view single
    @GetMapping("/news/{id}")
    public String readSingle(Model model, @PathVariable long id) {
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        News news = newsRepo.findById(id).get();
        viewUpdater.addView(news);
        model.addAttribute("news", news);

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "single";
    }

    //new single
    @GetMapping("/add")
    public String addform(Model model) {
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "add";
    }

    //single in edit
    @GetMapping("/modeNews/{id}")
    public String addform(Model model, @PathVariable long id) {
        model.addAttribute("news", newsRepo.getOne(id));

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "add";
    }

    //posts
    @Transactional
    @PostMapping("/makeNews")
    public String create(@RequestParam("file") MultipartFile file,
            @RequestParam("label") String otsikko,
            @RequestParam("lead") String ingressi,
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

    //single in edit
    @Transactional
    @PostMapping("/modeNews/{id}")
    public String save(@RequestParam("file") MultipartFile file,
            @RequestParam("label") String label,
            @RequestParam("lead") String lead,
            @RequestParam("text") String text,
            @PathVariable("id") long id
    ) throws IOException {
        if (file != null) {

            String[] fileFrags = file.getOriginalFilename().split("\\.");
            String extension = fileFrags[fileFrags.length - 1];
            // if (!extension.equals("gif") || !file.getContentType().equals("image/gif")) {
            //    return "redirect:/";
            //  }

            FileObject fo = new FileObject(null, file.getName(), file.getContentType(), file.getSize(), file.getBytes());
            fileRepo.save(fo);
            News news = newsRepo.getOne(id);
            news.setIngressi(lead);
            news.setLabel(label);
            news.setText(text);
            news.setKuva(fo);
            newsRepo.save(news);
        } else {
            News news = new News(label, lead, text, null);
            newsRepo.save(news);
        }

        return "redirect:/";
    }

}
