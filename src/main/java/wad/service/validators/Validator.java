package wad.service.validators;

import java.util.List;

public abstract class Validator<T> {

    protected static final int SPACES_COUNT = 0;
    protected static final int SPACES_IGNORED = 1;

    private static final String illegalModeMSG = "mode not recognized, only 0 or 1 are allowed!";
    private static final String minOverMaxMsg = "min cannot be bigger than max";

    public abstract List<String> validate(T t);

    protected boolean validateStringLength(String toValidate, int min, int max, int mode) throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException(minOverMaxMsg);
        } else if (mode < 0 && mode > 1) {
            throw new IllegalArgumentException(illegalModeMSG);
        }
        
        if (mode == SPACES_IGNORED) {
            int length = toValidate.trim().length();
            return length >= min && length <= max;
        } else {
            int length = toValidate.length();
            return length >= min && length <= max;
        }
    }

    protected boolean validateStringIsAlphabetic(String toValidate) {
        for (int i = 0; i < toValidate.length(); i++) {
            if (!Character.isAlphabetic(toValidate.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    protected int specialCharacterCounter(String toValidate, int mode) {
        String string = toValidate;
        if (mode == SPACES_IGNORED) {
            string = string.trim();
        }
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isAlphabetic(string.codePointAt(i))) {
                count++;
            }
        }
        return count;
    }
    
    protected int alphabeticCharacterCounter(String toValidate) {
        int count = 0;
        for (int i = 0; i < toValidate.length(); i++) {
            if (Character.isAlphabetic(toValidate.codePointAt(i))) {
                count++;
            }
        }
        return count;
    }

}
