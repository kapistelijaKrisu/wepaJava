
package wad.service.verifiers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.WriterRepository;
import org.springframework.stereotype.Service;

@Service
public class WriterVerifier implements Verifier<Writer>{
    @Autowired
    WriterRepository writerRepo;

    @Override
    public List<String> verifyNew(Writer t) {// ei käyttöä vielä
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> verifyDelete(Writer t) {
        List<String> errors = new ArrayList<>();      
        for (News aNew : t.getNews()) {
            if (aNew.getWriters().size() == 1) {
                errors.add("Uutisella on pakko olla ainakin yksi kirjoittaja! Et voi poistaa tätä kategoriaa"+
                " voit muokata uutista juuripolkuun lisäämällä /modeNews/"+aNew.getId());
            }
        }
        return errors;
    }
    
    @Override
    public List<String> warn(Writer t) {
        List<String> warnings = new ArrayList<>();

        if (!writerRepo.findByName(t.getName()).isEmpty()) {
            warnings.add("Varoitus saman nimisestä kirjailijasta!");
        }
        return warnings;
    }

    
    
}
