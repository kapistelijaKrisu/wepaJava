
package wad.service;

import java.util.Comparator;
import org.springframework.stereotype.Service;
import wad.domain.News;

@Service
public class NewsSorterByTotalViews implements Comparator<News>{

    @Override
    public int compare(News o1, News o2) {
        int diff =  (int) (o2.getTotalViewCount() - o1.getTotalViewCount());
        if (diff == 0) {
            diff = o2.getPublished().compareTo(o1.getPublished());
        }
        return diff;
    }
    
}
