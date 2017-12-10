package wad.service.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wad.domain.FileObject;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileObjectValidatorTest {

    @Autowired
    private FileObjectValidator foValidator;

    @Test
    public void correctTypeWorks() {
        byte[] bs = new byte[]{0, 0, 0, 0, 0};
        FileObject fo = new FileObject(null, "a", "type", 5l, bs);
        for (String s : foValidator.getValidTypes()) {
            fo.setContentType(s);
            assertTrue(foValidator.validate(fo).isEmpty());
        }
    }

    @Test
    public void incorrectTypeWorks() {
        byte[] bs = new byte[]{0, 0, 0, 0, 0};
        FileObject fo = new FileObject(null, "a", "type", 5l, bs);
        fo.setContentType("images/asd");
        assertFalse(foValidator.validate(fo).isEmpty());

    }

    @Test
    public void correctnameWorks() {
        byte[] bs = new byte[]{0, 0, 0, 0, 0};
        FileObject fo = new FileObject(null, "a", "image/png", 5l, bs);
        assertTrue(foValidator.validate(fo).isEmpty());

        String mock = "";
        for (int i = 0; i < 63; i++) {
            mock += "a";
        }
        fo.setName(mock);
        assertTrue(foValidator.validate(fo).isEmpty());
    }

    @Test
    public void incorrectnameWorks() {
        byte[] bs = new byte[]{0, 0, 0, 0, 0};
        FileObject fo = new FileObject(null, "a", "image/png", 5l, bs);

        String mock = "";
        for (int i = 0; i < 64; i++) {
            mock += "a";
        }
        fo.setName(mock);
        assertFalse(foValidator.validate(fo).isEmpty());
    }

    @Test
    public void lengthsMatch() {
        byte[] bs = new byte[]{0, 0, 0, 0, 0, 1};
        FileObject fo = new FileObject(null, "a", "image/png", 6l, bs);
        assertTrue(foValidator.validate(fo).isEmpty());
    }

    @Test
    public void lengthsNotMatching() {
        byte[] bs = new byte[]{0, 0, 0, 0, 0, 1};
        FileObject fo = new FileObject(null, "a", "image/png", 5l, bs);
        assertFalse(foValidator.validate(fo).isEmpty());
    }
}
