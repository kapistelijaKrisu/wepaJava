package wad.service.verifiers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wad.domain.Category;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.NewsRepository;
import wad.repository.WriterRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WriterVerifierTest {

    @Autowired
    private WriterVerifier writerVerifier;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private WriterRepository writerRepo;

    //assertTrue(foValidator.validate(fo).isEmpty());
    @Test
    public void verifyWarn() {
        Writer a = new Writer("abc");
        assertTrue(writerVerifier.warn(a).isEmpty());
        writerRepo.save(a);
        Writer b = new Writer("abb");
        assertTrue(writerVerifier.warn(b).isEmpty());
        Writer c = new Writer("abc");
        assertFalse(writerVerifier.warn(c).isEmpty());

    }

    @Test
    public void verifyDeleteTest() {
        Writer a = new Writer("abbb");
        writerRepo.save(a);
        Writer b = new Writer("bbbb");
        writerRepo.save(b);
        News n = new News("a", "v", "d", null);
        newsRepo.save(n);

        n.addWriter(a);
        n.addWriter(b);
        a.addNews(n);
        b.addNews(n);

        newsRepo.save(n);
        writerRepo.save(b);
        writerRepo.save(a);

        a = writerRepo.findByName(a.getName()).get(0);
        assertTrue(writerVerifier.verifyDelete(a).isEmpty());

        n.deleteWriter(a);

        assertFalse(writerVerifier.verifyDelete(b).isEmpty());
        System.out.println(n.getCategories().size());
    }
}
