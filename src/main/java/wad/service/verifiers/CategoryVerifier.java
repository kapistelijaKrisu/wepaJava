package wad.service.verifiers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import wad.domain.Category;
import wad.domain.News;
import wad.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryVerifier implements Verifier<Category> {

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public List<String> verifyNew(Category t) {
        List<String> errors = new ArrayList<>();
        if (categoryRepo.findByName(t.getName()) != null) {
            errors.add("Saman niminen kategoria on jo olemassa!");
        }
        return errors;
    }

    @Override
    public List<String> verifyDelete(Category t) {
        List<String> errors = new ArrayList<>();
        for (News aNew : t.getNews()) {
            if (aNew.getCategories().size() == 1) {
                errors.add("Uutisella on pakko olla ainakin yksi kategoria! Et voi poistaa tätä kategoriaa");
                errors.add("voit muokata uutista juuripolkuun lisäämällä /modeNews/" + aNew.getId());
            }
        }
        return errors;
    }

    @Override
    public List<String> warn(Category t) {// ei käyttöä ainakaan vielä
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
