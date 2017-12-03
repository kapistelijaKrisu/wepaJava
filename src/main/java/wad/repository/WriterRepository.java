
package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Writer;

public interface WriterRepository extends JpaRepository<Writer, Long> {
    
}
