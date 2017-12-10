
package wad.selenium.newsForm;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import wad.domain.Category;
import wad.domain.FileObject;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.CategoryRepository;
import wad.repository.FileObjectRepository;
import wad.repository.NewsRepository;
import wad.repository.WriterRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsFormControllerTest {
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    NewsRepository newsRepo;
    @Autowired
    CategoryRepository catRepo;
    @Autowired
    FileObjectRepository fileRepo;
    @Autowired
    WriterRepository writerRepo;
    
    Category category;
    Writer writer;
    
   @Before
    public void setUp() throws InterruptedException {
        category = new Category("category");
        catRepo.save(category);
       
        writer = new Writer("writer");
        writerRepo.save(writer);
                
        
    }
    @After
    public void cleanUp() {
        List<News> news = newsRepo.findAll();
        for (News aNew : news) {
            newsRepo.delete(aNew);
        }
        List<Category> cats = catRepo.findAll();
        for (Category cat : cats) {
            catRepo.delete(cat);
        }
        List<Writer> writers = writerRepo.findAll();
        for (Writer writer : writers) {
            writerRepo.delete(writer);
        }
        List<FileObject> fos = fileRepo.findAll();
        for (FileObject fo : fos) {
            fileRepo.delete(fo);
        }
    }
    @Test
    public void canPostAFile() throws Exception {
        //myöhemmin
    }
}
