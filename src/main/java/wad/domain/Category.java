package wad.domain;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Category extends AbstractPersistable<Long> implements Comparable<Category> {

    private String name;
   
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "news_category",
            joinColumns = @JoinColumn(name = "Category_id"),
            inverseJoinColumns = @JoinColumn(name = "News_id"))
    private Set<News> news;

    public Category(String name) {
        this.name = name;
    }

    public void addNews(News news) {
        if (this.news == null) {
            this.news = new TreeSet<>();
        }
        this.news.add(news);
    }
    
    public void removeNews(News news) {
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
        Category cat = (Category) o;
        return this.getId() != null 
                && Objects.equals(this.name, cat.name);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Category o) {
        return name.compareTo(o.getName());
    }
}
