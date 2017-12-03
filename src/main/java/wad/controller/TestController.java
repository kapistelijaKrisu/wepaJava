package wad.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.transaction.Transactional;
import wad.domain.*;
import wad.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private FileObjectRepository fileRepo;
    
    private boolean init = false;

    @GetMapping("/init")
    @Transactional
    public String init() {
        if (!init) {
            Writer wa = new Writer("Tolkien");
            Writer wb = new Writer("Martin the killer");
            writerRepo.save(wa);
            writerRepo.save(wb);

            Category ca = new Category("kaffe");
            Category cb = new Category("best Cat");
            catRepo.save(ca);
            catRepo.save(cb);
            
            //FileObject fo = getFileObject("bananas.gif");
            //FileObject foo = getFileObject("bananas.gif");
          //  fileRepo.save(fo);
        //    fileRepo.save(foo);
            
            
            News na = new News(
                    "omfg news!",
                    "can u believe there are news D;",
                    "this is text for news blargh",
                    LocalDateTime.now(), null, null, null);

            News nb = new News(
                    "omfg more news!",
                    "this is ingression thingy ",
                    "this is text for news number2",
                    LocalDateTime.now(), null, null, null);

            // newsRepo.save(nb);
            // newsRepo.save(na);
            na.addWriter(wb);
            na.addWriter(wa);
            na.addCategory(cb);
            na.addCategory(ca);

            nb.addWriter(wb);
            nb.addCategory(cb);

            newsRepo.save(nb);
            newsRepo.save(na);

            init = true;
        }

        return "redirect:/";
    }

}
