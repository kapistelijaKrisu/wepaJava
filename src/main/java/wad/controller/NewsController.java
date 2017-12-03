package wad.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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
import wad.domain.Category;
import wad.domain.FileObject;
import wad.domain.News;
import wad.repository.CategoryRepository;
import wad.repository.FileObjectRepository;
import wad.repository.NewsRepository;

@Controller
public class NewsController {

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private FileObjectRepository fileRepo;

    @GetMapping("/")
    public String list(Model model) {
        Pageable page = PageRequest.of(0, 5, Sort.Direction.DESC, "published");
        Page<News> news = newsRepo.findAll(page);
        model.addAttribute("news", news);
        Pageable sort = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "name");
        model.addAttribute("categories", catRepo.findAll(sort));
        return "index";
    }

    @GetMapping("/news/category/{id}")
    public String listByCat(Model model, @PathVariable long id) {
        Pageable page = PageRequest.of(0, 5, Sort.Direction.DESC, "published");
        if (id != 0) {
            Category cat = catRepo.findById(id).get();
            List<News> news = newsRepo.findByCategories(cat, page);
            model.addAttribute("news", news);
        } else {
            Page<News> news = newsRepo.findAll(page);
            model.addAttribute("news", news);
        }

        Pageable sort = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "name");
        model.addAttribute("categories", catRepo.findAll(sort));
        return "sorted";
    }

    @GetMapping("/add")
    public String addform(Model model) {
        return "add";
    }

    @GetMapping("/news/{id}")
    public String readSingle(Model model, @PathVariable Long id) {
        model.addAttribute("news", newsRepo.findById(id).get());
        return "single";
    }

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
            News news = new News(otsikko, ingressi, teksti, LocalDateTime.now(), fo, null, null);
            newsRepo.save(news);
        } else {
            News news = new News(otsikko, ingressi, teksti, LocalDateTime.now(), null, null, null);
            newsRepo.save(news);
        }

        return "redirect:/";
    }
}
