
package wad.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class View extends AbstractPersistable<Long> implements Comparable<View>{
    
    private int year;
    private int week;  
    private long views;
    @ManyToOne
    @JoinColumn
    private News news;
    
 
    @Override
    public int hashCode() {
        return Objects.hash(year, week, views);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final View other = (View) obj;
        if (this.views != other.views) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        if (this.week != other.week) {
            return false;
        }
        return Objects.equals(this.news, other.news);
    }

    @Override
    public int compareTo(View o) {
        if (year != o.getYear()) {
            return year - o.getYear();
        } else {
            return week - o.getWeek();
        }
    }

    


}
