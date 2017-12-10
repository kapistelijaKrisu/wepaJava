package wad.service.validators;

import java.time.LocalDateTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wad.domain.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsValidatorTest {

    private News news;
    @Autowired
    private NewsValidator newsValidator;

    @Before
    public void setUp() throws Exception {
        news = new News("aaaaabbbbb", "aaaaabbbbb", "aaaaabbbbb", null);
        news.addWriter(new Writer("a"));
        news.addCategory(new Category("a"));
        news.setPublished(LocalDateTime.now());
        byte[] bs = new byte[]{0, 0, 0, 0, 0};
        FileObject fo = new FileObject(null, "a", "image/png", 5l, bs);
        news.setKuva(fo);
    }

    @Test
    public void correctWorks() {
        assertTrue(newsValidator.validate(news).isEmpty());
    }

    @Test
    public void labelingWorks() {
        String mock = "";
        for (int i = 0; i < 123; i++) {
            mock += "a";
        }
        news.setLabel(mock);
        assertTrue(newsValidator.validate(news).isEmpty());
        news.setLabel("#####aaaaaaaaaa#####");
        mock = "";
        assertTrue(newsValidator.validate(news).isEmpty());
        for (int i = 0; i < 113; i++) {
            mock += "a";
        }
        mock += "##########";
        news.setLabel(mock);
        assertTrue(newsValidator.validate(news).isEmpty());

    }

    @Test
    public void ingressiWorks() {
        String mock = "";
        for (int i = 0; i < 1234; i++) {
            mock += "a";
        }
        news.setIngressi(mock);
        assertTrue(newsValidator.validate(news).isEmpty());
        news.setIngressi("#####aaaaa#####aaaaa");
        mock = "";
        assertTrue(newsValidator.validate(news).isEmpty());
        for (int i = 0; i < 1214; i++) {
            mock += " ";
        }
        mock += "#####aaaaa#####aaaaa";
        news.setIngressi(mock);
        assertTrue(newsValidator.validate(news).isEmpty());
    }

    @Test
    public void textWorks() {
        String mock = "";
        for (int i = 0; i < 12345; i++) {
            mock += "a";
        }

        news.setText(mock);
        assertTrue(newsValidator.validate(news).isEmpty());
        news.setText("#####aaaaa#####aaaaaa");
        mock = "";
        assertTrue(newsValidator.validate(news).isEmpty());
        for (int i = 0; i < 1232; i++) {
            mock += " ";
        }
        mock += "aaaaabbbbb";
        news.setText(mock);
        assertTrue(newsValidator.validate(news).isEmpty());
    }
    
    @Test
    public void illegalTextWorks() {
        String mock = "";
        for (int i = 0; i < 12346; i++) {
            mock += "a";
        }
        news.setText(mock);
        assertFalse(newsValidator.validate(news).isEmpty());
        news.setText("#aaaabbbbb");
        assertFalse(newsValidator.validate(news).isEmpty());    
    }
    
    @Test
    public void illegalIngressiWorks() {
        String mock = "";
        for (int i = 0; i < 1235; i++) {
            mock += "a";
        }
        news.setIngressi(mock);
        assertFalse(newsValidator.validate(news).isEmpty());
        news.setIngressi("#aaaabbbbb");
        assertFalse(newsValidator.validate(news).isEmpty());
        news.setIngressi("#####aaaa#####aaaaa#");
        assertFalse(newsValidator.validate(news).isEmpty());      
    }
    
    @Test
    public void illegalLabelWorks() {
        String mock = "";
        for (int i = 0; i < 124; i++) {
            mock += "a";
        }
        news.setLabel(mock);
        assertFalse(newsValidator.validate(news).isEmpty());
        news.setLabel("#aaaabbbbb");
        assertFalse(newsValidator.validate(news).isEmpty());
        news.setLabel("#####aaaaa#####aaaaa#");
        assertFalse(newsValidator.validate(news).isEmpty());       
    }
    
    @Test
    public void nulltests() {
        news.setCategories(null);
        assertTrue(newsValidator.validate(news).size()==1);
        news.setWriters(null);
        assertTrue(newsValidator.validate(news).size()==2);
        news.setKuva(null);
        assertTrue(newsValidator.validate(news).size()==3);
        news.setPublished(null);
        assertTrue(newsValidator.validate(news).size()==4);
        assertTrue(newsValidator.validate(news).size()==4);
    }
}
