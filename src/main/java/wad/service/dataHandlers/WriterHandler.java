package wad.service.dataHandlers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.Category;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.WriterRepository;
import wad.service.validators.WriterValidator;
import wad.service.verifiers.WriterVerifier;

@Transactional
@Service
public class WriterHandler {

    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private WriterValidator writerValidator;
    @Autowired
    private WriterVerifier writerVerifier;

    public boolean save(String writer, RedirectAttributes attributes) {
        Writer w = new Writer(writer);
        List<String> errors = writerValidator.validate(w);

        if (errors.isEmpty()) {//varoitetaan samannimisyydestä //muuta verifiointia ei ole
            List<String> warning = writerVerifier.warn(w);
            if (!warning.isEmpty()) {
                attributes.addFlashAttribute("warnings", warning);
            }
            attributes.addFlashAttribute("success", "Kirjoittaja on onnistuneesti rekisteröity!");
            writerRepo.save(w);
            return true;
        } else {
            attributes.addFlashAttribute("errors", errors);
        }
        return false;
    }

    public boolean deleteWriters(String[] writers, RedirectAttributes attributes) {
        for (String writerId : writers) {
            Writer writer = writerRepo.getOne(Long.parseLong(writerId));
            List<String> errors = writerVerifier.verifyDelete(writer);

            if (errors.isEmpty()) {
                for (News aNew : writer.getNews()) {
                    aNew.deleteWriter(writer);
                }
                writerRepo.delete(writer);
                attributes.addFlashAttribute("success", "Kirjailija:" + writer.getName() + " on onnistuneesti poistettu listoilta D:!");
               
            } else {
                attributes.addFlashAttribute("errors", errors);
                return false;
            }
        }
         return true;
    }
}
