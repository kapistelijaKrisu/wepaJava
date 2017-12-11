
package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wad.service.dataHandlers.FileObjectHandler;

@Controller
public class ImageController {
 
     @Autowired
     FileObjectHandler fileHandler;
     
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        return fileHandler.getFileForView(id);
    }
}
