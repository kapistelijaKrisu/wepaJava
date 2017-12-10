package wad.service.validators;

import java.util.ArrayList;
import java.util.List;
import wad.domain.Category;
import org.springframework.stereotype.Service;

@Service
public final class CategoryValidator extends Validator<Category> {

    private final int maxLength;
    private final int minLength;
    private final int maxSpacesAmount;
    private final int minimumAlphaLetterCount;

    public CategoryValidator() {
        maxLength = 31;
        minLength = 3;
        maxSpacesAmount = 2;
        minimumAlphaLetterCount = 3;
    }

    @Override
    public List<String> validate(Category t) {
        List<String> errors = new ArrayList<>();

        if (!validateStringLength(t.getName(), minLength, maxLength, SPACES_IGNORED)) {
            errors.add("Kategorian nimi tulee olla " + minLength + "-" + maxLength + " kirjainta pitkä!");
        }
        if (alphabeticCharacterCounter(t.getName()) < minimumAlphaLetterCount) {
            errors.add("Kategorian nimellä nimellä tulee olla vähintään "
                    + minimumAlphaLetterCount
                    + " aakkosellista kirjainta unicodin mukaan!");
        }

        if (specialCharacterCounter(t.getName(), SPACES_COUNT) > maxSpacesAmount) {
            errors.add("Kirjoittajan nimessä saa olla enintään " + maxSpacesAmount + " erikoismerkkiä");
        }

        return errors;
    }

}
