
package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wad.domain.News;
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;
import wad.service.HTMLInfoGenerator;

@Transactional
@Controller
public class IndexController {
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private HTMLInfoGenerator htmlInfo;
    
    @GetMapping("/")
    public String list(Model model) {
        Pageable page = PageRequest.of(0, 5, Sort.Direction.DESC, "published");
        Page<News> news = newsRepo.findAll(page);
        model.addAttribute("news", news);
        
        model.addAttribute("newest", htmlInfo.getNewestNews());
        model.addAttribute("categories", htmlInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", htmlInfo.getMostPopularNews());
        return "index";
}
}
