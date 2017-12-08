package wad.controller.single;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

@Transactional
@Controller
public class SingleNewsController {

    @Autowired
    private ViewInfoGenerator viewInfo;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private WriterRepository writerRepo;
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
        model.addAttribute("writers", writerRepo.findAll());

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "add";
    }

    //single in edit
    @GetMapping("/modeNews/{id}")
    public String addform(Model model, @PathVariable long id) {
        News n = newsRepo.getOne(id);
        model.addAttribute("news", n);
        model.addAttribute("missingWriters", getNotHavingWriters(n));
        model.addAttribute("missingCategories", getNotHavingCategories(n));

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "add";
    }
//lisää

    @PostMapping("/makeNews")
    public String save(@RequestParam("file") MultipartFile file,
            @RequestParam("label") String otsikko,
            @RequestParam("lead") String ingressi,
            @RequestParam("text") String teksti,
            @RequestParam("categories") String[] categories,
            @RequestParam("writers") String[] writers) throws IOException {

        News news = new News(otsikko, ingressi, teksti, null);
        handleCategoryInput(news, categories);
        handleWritersInput(news, writers);
        if (file != null) {
            FileObject fo = createFileObject(file);
            fileRepo.save(fo);
            news.setKuva(fo);
        }
        newsRepo.save(news);
        return "redirect:/modeNews/" + news.getId();
    }

    //single in edit
    @PostMapping("/modeNews/{id}")
    public String save(@RequestParam("file") MultipartFile file,
            @RequestParam("label") String label,
            @RequestParam("lead") String lead,
            @RequestParam("text") String text,
            @PathVariable("id") long id
    ) throws IOException {

        News news = newsRepo.getOne(id);
        news.setIngressi(lead);
        news.setLabel(label);
        news.setText(text);

        if (file != null) {

            FileObject fo = createFileObject(file);
            fileRepo.save(fo);
            news.setKuva(fo);
        }
        newsRepo.save(news);
        return "redirect:/modeNews/" + news.getId();
    }

    private List<Category> getNotHavingCategories(News n) {
        List<Category> cats = catRepo.findAll();
        cats.removeAll(n.getCategories());
        return cats;
    }

    private List<Writer> getNotHavingWriters(News n) {
        List<Writer> writers = writerRepo.findAll();
        writers.removeAll(n.getWriters());
        return writers;
    }

    private void handleCategoryInput(News news, String[] categories) {
        for (String categoryId : categories) {
            Category cat = catRepo.getOne(Long.parseLong(categoryId));
            news.addCategory(cat);
        }
    }

    private void handleWritersInput(News news, String[] writers) {
        for (String writerId : writers) {
            Writer writer = writerRepo.getOne(Long.parseLong(writerId));
            news.addWriter(writer);
        }
    }

    private FileObject createFileObject(MultipartFile file) throws IOException {
        String[] fileFrags = file.getOriginalFilename().split("\\.");
        String extension = fileFrags[fileFrags.length - 1];
        // if (!extension.equals("gif") || !file.getContentType().equals("image/gif")) {
        //    return "redirect:/";
        //  }
        return new FileObject(null, file.getName(), file.getContentType(), file.getSize(), file.getBytes());
    }
}
