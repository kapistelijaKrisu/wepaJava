package wad.service.dataHandlers;

import java.io.IOException;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
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
public class NewsHandlerTest {

    @Autowired
    private NewsHandler newsHandler;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private FileObjectRepository fileRepo;
    @Autowired
    private NewsRepository newsRepo;
    Category a = new Category("cat");
    Writer b = new Writer("writer");

    @Before
    public void setUp() throws InterruptedException {

        writerRepo.save(b);
        catRepo.save(a);

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
        List<Writer> ww = writerRepo.findAll();
        for (Writer cat : ww) {
            writerRepo.delete(cat);
        }
        List<News> nn = newsRepo.findAll();
        for (News ne : nn) {
            newsRepo.delete(ne);
        }

    }

    @Test
    public void IncorrectWorks() throws IOException {
        MultipartFile foMock = new MockMultipartFile("asd", "dsa", "asss", "sdsda".getBytes());
        RedirectAttributes mock = new RedirectAttributesModelMap();

        assertTrue(newsHandler.save(foMock, "a", "a", "a",
                new String[]{"" + a.getId()},
                new String[]{"" + b.getId()},
                mock) == null);

        assertFalse(mock.getFlashAttributes().isEmpty());
    }
    
    @Test
    public void correctWorks() throws IOException {
        MultipartFile foMock = new MockMultipartFile("image/jpg", "image/jpg", "image/jpg", "sdsda".getBytes());
        RedirectAttributes mock = new RedirectAttributesModelMap();

        News news = newsHandler.save(foMock, "aaaaaaaaaaaaaaaaaaaaaaa",
                "aaaaaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaa",
                new String[]{"" + a.getId()},
                new String[]{"" + b.getId()},
                mock);

        assertTrue(news!=null);
        
        assertTrue(mock.getFlashAttributes().size()==1);
    }

}
