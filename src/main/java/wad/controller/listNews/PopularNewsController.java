package wad.controller.listNews;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wad.domain.News;
import wad.domain.View;
import wad.repository.ViewRepository;
import wad.service.viewServices.ViewSorter;
import wad.service.TimeService;
import wad.service.HTMLInfoGenerator;

@Controller
public class PopularNewsController {

    private final int PAGESIZE = 5;
    @Autowired
    private ViewRepository viewRepo;
    @Autowired
    private TimeService timeCalculator;
    @Autowired
    private ViewSorter viewSorter;
    @Autowired
    private HTMLInfoGenerator viewInfo;

    @GetMapping("/news/views/week/{pageNro}")
    public String listByViews(Model model, @PathVariable int pageNro) {
        model.addAttribute("news", getLastWeekViewedNews(pageNro));
        model.addAttribute("pages", getPageCount(pageNro));

        model.addAttribute("newest", viewInfo.getNewestNews());
        model.addAttribute("categories", viewInfo.getCategoriesByAlphabet());
        model.addAttribute("top5", viewInfo.getMostPopularNews());
        return "listNews";
    }

    private List<News> getLastWeekViewedNews(int pageNro) {
        Pageable pageable = PageRequest.of(pageNro, PAGESIZE, Sort.Direction.DESC, "views");
        int year = timeCalculator.getCurrentYear();
        int week = timeCalculator.getCurrentWeekNumber();

        List<View> viewList = new ArrayList<>();
        if (week != 1) {
            viewList = viewRepo.findByYearAndWeek(year, timeCalculator.getLastWeekNumber(), pageable);
        } else {
            viewList = viewRepo.findByYearAndWeek(year - 1, timeCalculator.getLastWeekNumber(), pageable);
        }
        return viewSorter.sortNewsByWeekViews(viewList);
    }

    private List<Integer> getPageCount(int pageNro) {
        int year = timeCalculator.getCurrentYear();
        int week = timeCalculator.getCurrentWeekNumber();
        Pageable sort = PageRequest.of(0, Integer.MAX_VALUE);

        int size = 0;
        if (week != 1) {
            size = viewRepo.findByYearAndWeek(year, timeCalculator.getLastWeekNumber(), sort).size();
        } else {
            size = viewRepo.findByYearAndWeek(year - 1, timeCalculator.getLastWeekNumber(), sort).size();
        }
        return viewInfo.listifyPageCount(size, pageNro);
    }
}
