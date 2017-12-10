package wad.service.viewServices;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import wad.domain.News;
import wad.domain.View;

@Service
public class ViewSorter implements Comparator<View> {

    public List<News> sortNewsByWeekViews(List<View> views) {
        views.sort(this);
        List<News> news = new ArrayList<>();
        views.forEach((view) -> {
            news.add(view.getNews());
        });
        return news;
    }

    @Override
    public int compare(View o1, View o2) {
        int diff = (int) (o2.getViews() - o1.getViews());

        if (diff == 0) {
            diff = o2.compareTo(o1);

        }
        return diff;

    }

}
