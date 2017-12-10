package wad.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Category;
import wad.domain.News;

public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findByCategories(Category categories, Pageable pageable);

}
