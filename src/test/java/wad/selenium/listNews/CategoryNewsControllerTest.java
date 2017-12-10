
package wad.selenium.listNews;

import java.util.List;
import org.fluentlenium.adapter.junit.FluentTest;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
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
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryNewsControllerTest extends FluentTest {
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    NewsRepository newsRepo;
    @Autowired
    CategoryRepository catRepo;
    
    
    @Before
    public void setUp() throws InterruptedException {
        Category a = new Category("success");
        Category b = new Category("fail");
        catRepo.save(b);
        catRepo.save(a);
        a=catRepo.findByName("success");
        b=catRepo.findByName("fail");
        
        
        News f1 = new News("fail", "fail", "fail", null);
        newsRepo.save(f1);
        f1.addCategory(a);
        newsRepo.save(f1);
        Thread.sleep(10);
        
        News a1 = new News("1", "fail", "fail", null);
        newsRepo.save(a1);
        a1.addCategory(a);
        newsRepo.save(a1);
        
        News a2 = new News("2", "fail", "fail", null);
        newsRepo.save(a2);
        a2.addCategory(a);
        newsRepo.save(a2);
        
        News a3 = new News("3", "fail", "fail", null);
        newsRepo.save(a3);
        a3.addCategory(a);
        newsRepo.save(a3);
        
        News a4 = new News("4", "fail", "fail", null);
        newsRepo.save(a4);
        a4.addCategory(a);
        newsRepo.save(a4);
        
        News a5 = new News("5", "fail", "fail", null);
        newsRepo.save(a5);
        a5.addCategory(a);
        newsRepo.save(a5);
        Thread.sleep(10);
        
        News f2 = new News("fail", "fail", "fail", null);
        newsRepo.save(f2);
        f2.addCategory(b);
        newsRepo.save(f2);
        
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
    public void first() throws InterruptedException {
        goTo("http://localhost:" + port + "/");
        Thread.sleep(100);
        find("button", withText("Kategoriat")).click();
        Thread.sleep(100);
        find("a", withText("success")).click();
  
        Thread.sleep(100);
        assertFalse(find("ul", withId("list")).attributes("li").contains("fail"));
        String[] mustHave = new String[] {
        "1","2","3","4","5"};
        
        for (String string : mustHave) {
            assertTrue(pageSource().contains(string));
        }
        
    }
    @Test
    public void newest() throws InterruptedException {
        goTo("http://localhost:" + port + "/");
        Thread.sleep(100);
        find("button", withText("Muut lajitteluperusteet")).click();
        Thread.sleep(100);
        find("a", withText("Uutuusjärjestys")).click();
  
        Thread.sleep(100);
        assertFalse(find("ul", withId("list")).attributes("li").contains("1"));
        String[] mustHave = new String[] {
        "2","3","4","5"};
        
        for (String string : mustHave) {
            assertTrue(pageSource().contains(string));
        }
        
    }
}
