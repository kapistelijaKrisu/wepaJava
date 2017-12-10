
package wad.selenium;

import java.util.List;
import org.fluentlenium.adapter.junit.FluentTest;
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
import wad.domain.News;
import wad.repository.NewsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerTest extends FluentTest {
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    NewsRepository newsRepo;
    
    @Before
    public void setUp() throws InterruptedException {
        News a1 = new News("fail", "fail", "fail", null);
        Thread.sleep(10);
        News a2 = new News("1l", "1i", "fail", null);
        News a3 = new News("2l", "2i", "fail", null);
        News a4 = new News("3l", "3i", "fail", null);
        News a5 = new News("4l", "4i", "fail", null);
        News a6 = new News("5l", "5i", "fail", null);
        newsRepo.save(a1);
       
        newsRepo.save(a2);
        newsRepo.save(a3);
        newsRepo.save(a4);
        newsRepo.save(a5);
        newsRepo.save(a6);
    }
    
    @After
    public void cleanUp() {
        List<News> news = newsRepo.findAll();
        for (News aNew : news) {
            newsRepo.delete(aNew);
        }
        
    }
    
    @Test
    public void first() {
        goTo("http://localhost:" + port + "/");
        assertFalse(pageSource().contains("fail"));
        String[] mustHave = new String[] {
        "1i","2i","3i","4i","5i", "1l", "2l", "3l","4l", "5l"};
        
        for (String string : mustHave) {
            assertTrue(pageSource().contains(string));
        }
        
    }
}
