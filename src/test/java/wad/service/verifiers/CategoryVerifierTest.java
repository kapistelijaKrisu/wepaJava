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
import wad.repository.CategoryRepository;
import wad.repository.NewsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryVerifierTest {

    @Autowired
    private CategoryVerifier catVerifier;
    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;

    //assertTrue(foValidator.validate(fo).isEmpty());
    @Test
    public void verifyNewTest() {
        Category a = new Category("abc");
        assertTrue(catVerifier.verifyNew(a).isEmpty());
        catRepo.save(a);
        Category b = new Category("abc");
        assertFalse(catVerifier.verifyNew(b).isEmpty());

    }

    @Test
    public void verifyDeleteTest() {
        Category a = new Category("abbb");
        catRepo.save(a);
        Category b = new Category("bbbb");
        catRepo.save(b);
        News n = new News("a", "v", "d", null);
        newsRepo.save(n);

        n.addCategory(a);
        n.addCategory(b);
        a.addNews(n);
        b.addNews(n);

        newsRepo.save(n);
        catRepo.save(b);
        catRepo.save(a);

        a = catRepo.findByName(a.getName());
        assertTrue(catVerifier.verifyDelete(a).isEmpty());

        n.deleteCategory(a);

        assertFalse(catVerifier.verifyDelete(b).isEmpty());
        System.out.println(n.getCategories().size());
    }
}
