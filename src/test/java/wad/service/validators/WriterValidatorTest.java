
package wad.service.validators;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wad.domain.Writer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WriterValidatorTest {
    @Autowired
    WriterValidator writerValidator;
    
    @Test
    public void correctWorks() {
        Writer w = new Writer("aa");
        assertTrue(writerValidator.validate(w).isEmpty());
        w.setName("aa+++");
        assertTrue(writerValidator.validate(w).isEmpty());
        String mock = "";
        for (int i = 0; i < 127; i++) {
            mock+="a";
        }
        w.setName(mock);
        assertTrue(writerValidator.validate(w).isEmpty());
        mock="";
        for (int i = 0; i < 124; i++) {
            mock+="a";
        }
        mock+=">>>";
        w.setName(mock);
        assertTrue(writerValidator.validate(w).isEmpty());
    }
    @Test
    public void incorrectFails() {
        Writer w = new Writer("a");
        assertFalse(writerValidator.validate(w).isEmpty());
        w.setName("++++");
        assertFalse(writerValidator.validate(w).isEmpty());
        w.setName("++aa++");
        assertFalse(writerValidator.validate(w).isEmpty());
        
        String mock = "";
        for (int i = 0; i < 128; i++) {
            mock+="a";
        }
        assertFalse(writerValidator.validate(w).isEmpty());
        mock="";
        for (int i = 0; i < 125; i++) {
            mock+="a";
        }
        mock+=">>>";
        assertFalse(writerValidator.validate(w).isEmpty());
    }
}
