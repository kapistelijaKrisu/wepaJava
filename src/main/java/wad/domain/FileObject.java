package wad.domain;
        
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class FileObject extends AbstractPersistable<Long> {

    @OneToOne
    private News news;
    private String name;
    private String contentType;
    private Long contentLength;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    @Override
    public int hashCode() {
        long length = contentLength;
        int namehash = name.hashCode();
        int typehash = contentType.hashCode();
        
        if (length > Integer.MAX_VALUE - namehash - typehash) {
            length = length % (Integer.MAX_VALUE - namehash - typehash);
        }
        
        return (int) (name.hashCode() + length + contentType.hashCode());
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
        final FileObject other = (FileObject) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.contentType, other.contentType)) {
            return false;
        }
        if (!Objects.equals(this.contentLength, other.contentLength)) {
            return false;
        }
        return true;
    }

}