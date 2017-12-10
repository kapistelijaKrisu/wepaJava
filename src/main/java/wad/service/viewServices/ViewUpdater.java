package wad.service.viewServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.domain.News;
import wad.domain.View;
import wad.repository.NewsRepository;
import wad.repository.ViewRepository;
import wad.service.TimeService;

@Service
public class ViewUpdater {

    @Autowired
    private TimeService timeCalculator;
    @Autowired
    private ViewRepository viewRepo;
    @Autowired
    private NewsRepository newsRepo;

    public void addView(News news) {
        int week = timeCalculator.getCurrentWeekNumber();
        int year = timeCalculator.getCurrentYear();
        View views = viewRepo.findByNewsAndYearAndWeek(news, year, week);
        if (views == null) {
            views = new View(year, week, 1, news);
            viewRepo.save(views);
            news.getViews().add(views);
            newsRepo.save(news);
        } else {
            views.setViews(views.getViews() + 1);
            viewRepo.save(views);
        }
    }
}
