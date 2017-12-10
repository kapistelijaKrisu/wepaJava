package wad.service.validators;

import java.util.ArrayList;
import java.util.List;
import wad.domain.Writer;
import org.springframework.stereotype.Service;

@Service
public final class WriterValidator extends Validator<Writer> {
    private final int maxLength;
    private final int minLength;
    private final int maxSpecialCharacterAmount;
    private final int minimumAlphaLetterCount;

    public WriterValidator() {
        maxLength = 127;
        minLength = 2;
        maxSpecialCharacterAmount = 3;
        minimumAlphaLetterCount = 2;
    }

    @Override
    public List<String> validate(Writer t) {
        List<String> errors = new ArrayList<>();
        if (!validateStringLength(t.getName(), minLength, maxLength, SPACES_COUNT)) {
            errors.add("Kirjoittajan nimi tulee olla " + minLength + "-" + maxLength + " merkkiä pitkä!");
        }
        if (specialCharacterCounter(t.getName(), SPACES_IGNORED) > maxSpecialCharacterAmount) {
             errors.add("Kirjoittajan nimessä saa olla enintään " + maxSpecialCharacterAmount + " erikoismerkkiä");
        }
        if (alphabeticCharacterCounter(t.getName()) < minimumAlphaLetterCount) {
            errors.add("Kirjoittajan nimellä tulee olla vähintään " +
                    minimumAlphaLetterCount +
                    " aakkosellista kirjainta unicodin mukaan!");
        }
        
        return errors;
    }

}
