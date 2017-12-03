
package wad.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import wad.domain.News;
import wad.domain.View;

@Service
public class NewsSorter implements Comparator<View>{
    
    public List<News> sortNewsByViews(List<View> views) {
        views.sort(this);
        List<News> news = new ArrayList<>();
        views.forEach((view) -> {
            news.add(view.getNews());
        });
        return news;
    }

    @Override
    public int compare(View o1, View o2) {
       return (int) (o1.getViews() - o2.getViews());
    }
    
}
