
package wad.controller.listNews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wad.domain.News;
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;
import wad.service.ViewInfoGenerator;

@Controller
public class MainPageController {
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private ViewInfoGenerator viewInfo;
    
    @GetMapping("/")
    public String list(Model model) {
        Pageable page = PageRequest.of(0, 5, Sort.Direction.DESC, "published");
        Page<News> news = newsRepo.findAll(page);
        model.addAttribute("news", news);
        
        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "index";
}
}
