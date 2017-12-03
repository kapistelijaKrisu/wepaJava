
package wad.domain;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

    

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Writer extends AbstractPersistable<Long> implements Comparable<Writer>{

    private String name;
    @ManyToMany(mappedBy="writers",fetch = FetchType.EAGER)
    private Set<News> news;

    public Writer(String name) {
        this.name = name;
    }

    public void addTip(News news) {
        if (this.news == null) {
            this.news = new TreeSet<>();
        }
        this.news.add(news);
    }
    
    public void removeTip(News news) {
        this.news.remove(news);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
        ///hashauksee
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Writer wat = (Writer) o;
        return this.getId() != null 
                && Objects.equals(this.name, wat.name);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Writer o) {
        return this.name.compareTo(o.getName());
    }
}
