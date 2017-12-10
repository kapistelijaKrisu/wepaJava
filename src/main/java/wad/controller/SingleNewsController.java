package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wad.domain.News;
import wad.repository.NewsRepository;
import wad.service.HTMLInfoGenerator;
import wad.service.viewServices.ViewUpdater;

@Transactional
@Controller
public class SingleNewsController {

    @Autowired
    private HTMLInfoGenerator viewInfo;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    ViewUpdater viewUpdater;

    @GetMapping("/news/{id}")
    public String readSingle(Model model, @PathVariable long id) {
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        News news = newsRepo.findById(id).get();
        viewUpdater.addView(news);
        model.addAttribute("news", news);

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "singleNews";
    }
}
