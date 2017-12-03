package wad.controller;

import java.util.Comparator;
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
import wad.domain.View;
import wad.domain.News;
import wad.repository.ViewRepository;
import wad.repository.NewsRepository;
import wad.service.TimeSerice;
import wad.service.NewsSorter;

@Controller
public class ViewsController {

    private final int PAGESIZE = 5;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private ViewRepository viewRepo;
    @Autowired
    private TimeSerice timeCalculator;
    @Autowired
    private NewsSorter viewSorter;

    @GetMapping("/news/views/week/{page}")
    public String listByViews(Model model, @PathVariable int page) {
        Pageable pageable = PageRequest.of(page, PAGESIZE, Sort.Direction.DESC, "views");
        int year = timeCalculator.getCurrentYear();
        int week = timeCalculator.getCurrentWeekNumber();
        List<View> viewList = viewRepo.findByYearAndWeek(year, week, pageable);

        model.addAttribute("news", viewSorter.sortNewsByViews(viewList));

        if (viewList.size() < PAGESIZE * page + page) {
            String message = "No more viewed news this week! D:";
            model.addAttribute("message", message);
        }

        return "sorted";
    }
}
