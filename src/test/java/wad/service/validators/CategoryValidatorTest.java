
package wad.service.validators;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wad.domain.Category;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryValidatorTest {
    @Autowired
    private CategoryValidator categoryValidator;
    
    @Test
    public void correctWorks() {
        Category c = new Category("aaa");
       assertTrue(categoryValidator.validate(c).isEmpty());
        c.setName("a a a");
        
 assertTrue(categoryValidator.validate(c).isEmpty());
        String mock = "";
        for (int i = 0; i < 31; i++) {
            mock+="a";
        }
        c.setName(mock);
        assertTrue(categoryValidator.validate(c).isEmpty());
        mock="";
        for (int i = 0; i < 29; i++) {
            mock+="a";
        }
        mock+="  ";
        c.setName(mock);
        assertTrue(categoryValidator.validate(c).isEmpty());
    }
    @Test
    public void incorrectFails() {
        Category c = new Category("aa");
        assertFalse(categoryValidator.validate(c).isEmpty());
        c.setName("   ");
        assertFalse(categoryValidator.validate(c).isEmpty());
        c.setName("a a a ");
        assertFalse(categoryValidator.validate(c).isEmpty());
               
        String mock = "";
        for (int i = 0; i < 32; i++) {
            mock+="a";
        }
        assertFalse(categoryValidator.validate(c).isEmpty());
        mock="";
        for (int i = 0; i < 30; i++) {
            mock+="a";
        }
        mock+="  ";
        assertFalse(categoryValidator.validate(c).isEmpty());
    }
}
