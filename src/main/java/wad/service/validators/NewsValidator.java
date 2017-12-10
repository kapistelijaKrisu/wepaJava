package wad.service.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import wad.domain.News;
import org.springframework.stereotype.Service;

@Service
public class NewsValidator extends Validator<News> {

    private final int maxLabelLength, maxLeadLength, maxTextLength;
    private final int maxSpecialCharacterAmount;
    private final int minLength;
    private final int minimumAlphaLetterCount;

    public NewsValidator() {
        this.maxLabelLength = 31;
        this.maxLeadLength = 127;
        this.maxTextLength = 1234;
        this.maxSpecialCharacterAmount = 3;
        this.minLength = 3;
        this.minimumAlphaLetterCount = 2;
    }

    @Override
    public List<String> validate(News t) {
        List<String> errors = new ArrayList<>();
        errors.addAll(hasDateTime(t));
        errors.addAll(validateLabel(t.getLabel()));
        errors.addAll(validateIngressi(t.getIngressi()));
        errors.addAll(validateText(t.getText()));

        if (!atLeastOne(t.getCategories())) {
            errors.add("Uutisella täytyy kuulua ainakin yhteen kategoriaan!");
        }
        if (!atLeastOne(t.getWriters())) {
            errors.add("Uutisella täytyy olla ainakin yksi kirjoittaja!");
        }
        if (t.getKuva() == null) {
            errors.add("Uutisella täytyy olla kuva!");
        }

        return errors;
    }

    private boolean atLeastOne(Set set) {
        return set != null && set.size() >= 1;
    }

    private List<String> hasDateTime(News t) {
        List<String> errors = new ArrayList<>();
        if (t.getPublished() == null) {
            errors.add("Uutinen tarvitsee tarkan julkaisuajan!");
        }
        return errors;
    }

    private List<String> validateLabel(String t) {
        List<String> errors = new ArrayList<>();
        if (!validateStringLength(t, minLength, maxLabelLength, SPACES_COUNT)) {
            errors.add("Otsikon tulee olla " + minLength + "-" + maxLabelLength + " merkkiä!");
        }
        if (specialCharacterCounter(t, SPACES_IGNORED) > maxSpecialCharacterAmount) {
            errors.add("Otsikossa saa olla enintään " + maxSpecialCharacterAmount + " erikoismerkkiä");
        }
        if (alphabeticCharacterCounter(t) < minimumAlphaLetterCount) {
            errors.add("Otsikossa tulee olla vähintään "
                    + minimumAlphaLetterCount
                    + " aakkosellista kirjainta unicodin mukaan!");
        }
        return errors;
    }

    private List<String> validateIngressi(String t) {
        List<String> errors = new ArrayList<>();
        if (!validateStringLength(t, minLength, maxLeadLength, SPACES_COUNT)) {
            errors.add("Ingressin tulee olla " + minLength + "-" + maxLeadLength + " merkkiä!");
        }
        if (alphabeticCharacterCounter(t) < minimumAlphaLetterCount) {
            errors.add("Ingressissä tulee olla vähintään "
                    + minimumAlphaLetterCount
                    + " aakkosellista kirjainta unicodin mukaan!");
        }
        return errors;
    }

    private List<String> validateText(String t) {
        List<String> errors = new ArrayList<>();
        if (!validateStringLength(t, minLength, maxTextLength, SPACES_COUNT)) {
            errors.add("Tekstin tulee olla " + minLength + "-" + maxTextLength + " merkkiä!");
        }
        if (alphabeticCharacterCounter(t) < minimumAlphaLetterCount) {
            errors.add("Tekstissä tulee olla vähintään "
                    + minimumAlphaLetterCount
                    + " aakkosellista kirjainta unicodin mukaan!");
        }
        return errors;
    }

}
