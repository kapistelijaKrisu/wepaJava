package wad.domain;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class News extends AbstractPersistable<Long> implements Comparable<News>{

    private String label;
    private String ingressi;
    private String text;
    private LocalDateTime published;
    @OneToOne
    private FileObject kuva;

    //views
    @OneToMany(mappedBy = "news")
    private Set<View> views;

    //writer
    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "leWriter",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id"))
    private Set<Writer> writers;

    //writer
    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "leCats",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "cats_id"))
    private Set<Category> categories;

    public News(String label, String ingress, String text, FileObject kuva) {
        this.label = label;
        this.ingressi = ingress;
        this.text = text;
        this.published = LocalDateTime.now();
        this.kuva = kuva;

        writers = new TreeSet<>();
        categories = new TreeSet<>();
        views = new TreeSet<>();

    }
    
    public long getTotalViewCount() {
        long count = 0;
        for (View view : views) {
            count += view.getViews();
        }
        return count;
    }

    public void addWriter(Writer w) {
        if (writers == null) {
            writers = new TreeSet<>();
        }
        writers.add(w);

    }

    public void addCategory(Category c) {
        if (categories == null) {
            categories = new TreeSet<>();
        }
        categories.add(c);

    }
    @Override
    public int compareTo(News o) {
        return this.published.compareTo(o.getPublished());
    }
}
