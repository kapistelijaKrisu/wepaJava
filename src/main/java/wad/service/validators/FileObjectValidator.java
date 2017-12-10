package wad.service.validators;

import java.util.ArrayList;
import java.util.List;
import wad.domain.FileObject;
import org.springframework.stereotype.Service;

@Service
public class FileObjectValidator extends Validator<FileObject> {

    private final int maxSize;
    private final int maxNameLength;
    private final String[] validTypes;

    public FileObjectValidator() {
        maxNameLength = 63;
        validTypes = new String[]{"image/png", "image/jpeg", "image/jpg", "image/gif"};
        maxSize = 1234567;
    }

    @Override
    public List<String> validate(FileObject t) {
        List<String> errors = new ArrayList<>();
        if (t == null || t.getContent() == null || t.getContent().length < 1) {
            errors.add("Kuvaa ei löydy!");
        }
        if (!validateStringLength(t.getName(), 0, maxNameLength, SPACES_COUNT)) {
            errors.add("Kuvan nimi saa olla korkeintaan" + maxNameLength + " pitkä!");
        }
        if (!isValidType(t.getContentType())) {
            errors.add(makeContentTypeErrorMessage());
        }
        if (t.getContentLength() != t.getContent().length) {
            errors.add(("Kuvan koko on erilainen kuin ilmoitettu koko!"));
        }
        if (t.getContentLength() > maxSize) {
            errors.add("maksimikoko kuvalle on " + maxSize + "!");
        }
        return errors;
    }

    private String makeContentTypeErrorMessage() {
        StringBuilder sb = new StringBuilder("Kuvan sallitut tyypit ovat:");
        for (String validType : validTypes) {
            sb.append(" ").append(validType);
        }
        return sb.toString();
    }

    private boolean isValidType(String type) {
        for (String validType : validTypes) {
            if (type.equals(validType)) {
                return true;
            }
        }
        return false;
    }

    public String[] getValidTypes() {
        return validTypes;
    }
    
    
}
