package wad.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Category;
import wad.domain.News;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByCategories(Category categories, Pageable pageable);

}
