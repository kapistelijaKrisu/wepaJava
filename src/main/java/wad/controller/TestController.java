package wad.controller;

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
    @Autowired
    private ViewRepository viewRepo;
    
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
                    null);

            News nb = new News(
                    "omfg more news!",
                    "this is ingression thingy ",
                    "this is text for news number2",
                    null);

            View v = new View(2017, 48, 5, na);
            View vv = new View(2017, 48, 10, nb);
            viewRepo.save(v);
            viewRepo.save(vv); 
            
            for (int i = 0; i < 11; i++) {
                News n = new News(
                    ""+i,
                    "this is ingression thingy ",
                    "this is text for news number2",
                    null);
                View vvv = new View(2017, 48, 10+1, n);
                viewRepo.save(vvv);
                n.getViews().add(v);
                n.addCategory(cb);
                newsRepo.save(n);
            }
            
            na.getViews().add(v);
            nb.getViews().add(vv);
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
