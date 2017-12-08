
package wad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Writer;

public interface WriterRepository extends JpaRepository<Writer, Long> {

    public List<Writer> findByName(String writer);
    
}
