
package wad.service.dataHandlers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.Category;
import wad.domain.News;
import wad.repository.CategoryRepository;
import wad.service.validators.CategoryValidator;
import wad.service.verifiers.CategoryVerifier;

@Service
public class CategoryHandler {
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private CategoryValidator categoryValidator;
    @Autowired
    private CategoryVerifier categoryVerifier;
    
    public boolean save(String category, RedirectAttributes attributes) {
        Category c = new Category(category);
        List<String> errors = categoryValidator.validate(c);
        if (errors.isEmpty()) {
            errors.addAll(categoryVerifier.verifyNew(c));
        }
        if (errors.isEmpty()) {
            attributes.addFlashAttribute("success", "Kategoria on onnistuneesti luotu!");
            categoryRepo.save(c);
        } else {
            attributes.addFlashAttribute("errors", errors);
        }
        return false;
    }

    public boolean deleteCategories(String[] categories, RedirectAttributes attributes) {
        for (String writerId : categories) {
            Category category = categoryRepo.getOne(Long.parseLong(writerId));
            List<String> errors = categoryVerifier.verifyDelete(category);

            if (errors.isEmpty()) {
                for (News aNew : category.getNews()) {
                    aNew.deleteCategory(category);
                }
                categoryRepo.delete(category);
       attributes.addFlashAttribute("success", "Kategoria: " + category.getName() + " on onnistuneesti poistettu D:!");
        
            } else {
                attributes.addFlashAttribute("errors", errors);
                return false;
            }
        }
         return true;
    }
}
