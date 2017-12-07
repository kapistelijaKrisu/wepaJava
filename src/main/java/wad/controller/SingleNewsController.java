package wad.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    @PostMapping("/makeNews")
    public String create(@RequestParam String label, @RequestParam String lead, @RequestParam String text, @RequestParam MultipartFile file) {
        News news  = new News(label, lead, text, null);
        //valid   
        System.out.println(news.getId());
        newsRepo.save(news);
        
        try {
            FileObject fo = new FileObject(news, file.getName(), file.getContentType(), file.getSize(), file.getBytes());
            fileRepo.save(fo);
            news.setKuva(fo);
            newsRepo.save(news);
            System.out.println("succ");
        } catch (IOException ex) {
            Logger.getLogger(SingleNewsController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("fail");
        }
    
        return "redirect:/modeNews/"+news.getId();
    }
   
    //single in edit
    @PostMapping("/modeNews/{id}")
    public String modify(@PathVariable long id, @RequestParam String label, @RequestParam String lead, @RequestParam String text, @RequestParam MultipartFile file) {
        News news  = newsRepo.getOne(id);
        news.setLabel(label);
        news.setIngressi(lead);
        news.setText(text);
        //valid
        newsRepo.save(news);
        
        try {
            FileObject fo = new FileObject(news, file.getName(), file.getContentType(), file.getSize(), file.getBytes());
            //valid
            fileRepo.save(fo);
            news.setKuva(fo);
            newsRepo.save(news);
        } catch (IOException ex) {
            Logger.getLogger(SingleNewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return "redirect:/modeNews/"+news.getId();
    }


    
}