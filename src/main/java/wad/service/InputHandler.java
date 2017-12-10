
package wad.service;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.Category;
import wad.domain.FileObject;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.CategoryRepository;
import wad.repository.WriterRepository;

@Service
public class InputHandler {
    
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private WriterRepository writerRepo;
 
    public FileObject handleFileObjectOpening(MultipartFile file, List<String> errors) {
        try {
            return new FileObject(null, file.getName(), file.getContentType(), file.getSize(), file.getBytes());
        } catch (IOException e) {
            errors.add("Kuvan käsittelyssä tapahtui virhe. Ehkä polku on virheellinen?");
        }
        return null;
    }
    
    public void handleCategoryInput(News news, String[] categories) {
        for (String categoryId : categories) {
            Category cat = catRepo.getOne(Long.parseLong(categoryId));
            news.addCategory(cat);
        }
    }

    public void handleWritersInput(News news, String[] writers) {
        for (String writerId : writers) {
            Writer writer = writerRepo.getOne(Long.parseLong(writerId));
            news.addWriter(writer);
        }
    }
}
