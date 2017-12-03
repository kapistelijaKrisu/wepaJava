
package wad.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.News;
import wad.domain.View;

public interface ViewRepository extends JpaRepository<View, Long> {
    
    View findByNewsAndYearAndWeek(News news, int year, int week);

    public List<View> findByYearAndWeek(int year, int week, Pageable page);
}
