package wad.selenium;

import java.util.List;
import org.fluentlenium.adapter.junit.FluentTest;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.fluentlenium.core.filter.FilterConstructor.withName;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import wad.domain.Category;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;
import wad.repository.WriterRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MicromanagementTest extends FluentTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    NewsRepository newsRepo;
    @Autowired
    CategoryRepository catRepo;
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

        News f1 = new News("fail", "fail", "fail", null);
        newsRepo.save(f1);
        f1.addCategory(category);
        f1.addWriter(writer);
        newsRepo.save(f1);
        Thread.sleep(10);

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

    }

    @Test
    public void addCategory() throws InterruptedException {
        goTo("http://localhost:" + port + "/");
        Thread.sleep(100);
        find("button", withText("Hallintapaneeli")).click();
        Thread.sleep(100);
        find("a", withText("Mikromanagerointi")).click();
        assertTrue(pageSource().contains("writer"));
        assertTrue(pageSource().contains("category"));
        
        find("form", withId("categoryAdd")).find("input", withName("category")).fill().with("newCategory");
        find("form", withId("categoryAdd")).first().submit();
        Thread.sleep(10);
        assertTrue(pageSource().contains("writer"));
        assertTrue(pageSource().contains("category"));
        assertTrue(pageSource().contains("newCategory"));

    }
    @Test
    public void addWriter() throws InterruptedException {
        goTo("http://localhost:" + port + "/");
        Thread.sleep(100);
        find("button", withText("Hallintapaneeli")).click();
        Thread.sleep(100);
        find("a", withText("Mikromanagerointi")).click();
        assertTrue(pageSource().contains("writer"));
        assertTrue(pageSource().contains("category"));
        
        find("form", withId("writerAdd")).find("input", withName("writer")).fill().with("newWriter");
        find("form", withId("writerAdd")).first().submit();
        Thread.sleep(10);
        assertTrue(pageSource().contains("writer"));
        assertTrue(pageSource().contains("category"));
        assertTrue(pageSource().contains("newWriter"));

    }
    
    
    @Test
    public void addinvalidCategory() throws InterruptedException {
        goTo("http://localhost:" + port + "/");
        Thread.sleep(100);
        find("button", withText("Hallintapaneeli")).click();
        Thread.sleep(100);
        find("a", withText("Mikromanagerointi")).click();
        assertTrue(pageSource().contains("writer"));
        assertTrue(pageSource().contains("category"));
        
        find("form", withId("categoryAdd")).find("input", withName("category")).fill().with("a");
        find("form", withId("categoryAdd")).first().submit();
        Thread.sleep(10);
        assertTrue(pageSource().contains("writer"));
        assertTrue(pageSource().contains("category"));
        assertFalse(pageSource().contains("newCategory"));

    }
    @Test
    public void addInvalidWriter() throws InterruptedException {
        goTo("http://localhost:" + port + "/");
        Thread.sleep(100);
        find("button", withText("Hallintapaneeli")).click();
        Thread.sleep(100);
        find("a", withText("Mikromanagerointi")).click();
        assertTrue(pageSource().contains("writer"));
        assertTrue(pageSource().contains("category"));
        
        find("form", withId("writerAdd")).find("input", withName("writer")).fill().with("b");
        find("form", withId("writerAdd")).first().submit();
        Thread.sleep(10);
        assertTrue(pageSource().contains("writer"));
        assertTrue(pageSource().contains("category"));
        assertFalse(pageSource().contains("newWriter"));

    }
    
}
