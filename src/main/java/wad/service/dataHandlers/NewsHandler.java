package wad.service.dataHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.Category;
import wad.domain.FileObject;
import wad.domain.News;
import wad.domain.Writer;
import wad.repository.CategoryRepository;
import wad.repository.FileObjectRepository;
import wad.repository.NewsRepository;
import wad.repository.WriterRepository;
import wad.service.InputHandler;
import wad.service.validators.FileObjectValidator;
import wad.service.validators.NewsValidator;

@Transactional
@Service
public class NewsHandler {

    @Autowired
    private NewsRepository newsRepo;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private WriterRepository writerRepo;
    @Autowired
    private NewsValidator newsValidator;
    @Autowired
    private InputHandler inputHandler;
    @Autowired
    private FileObjectValidator fileValidator;
    @Autowired
    private FileObjectRepository fileRepo;

    public News save(MultipartFile file, String otsikko, String ingressi, String teksti,
            String[] categories, String[] writers, RedirectAttributes attributes) throws IOException {

        News news = new News(otsikko, ingressi, teksti, null);
        inputHandler.handleCategoryInput(news, categories);
        inputHandler.handleWritersInput(news, writers);

        List<String> errors = new ArrayList<>();
        FileObject fo = inputHandler.handleFileObjectOpening(file, errors);
        errors.addAll(fileValidator.validate(fo));
        news.setKuva(fo);
        errors.addAll(newsValidator.validate(news));

        if (errors.isEmpty()) {
            fileRepo.save(fo);
            newsRepo.save(news);
            attributes.addFlashAttribute("success", "Uutinen on onnistuneesti julkaistu!");
            return news;
        } else {
            attributes.addFlashAttribute("errors", errors);
            return null;
        }

    }
    //poisto
    public News deleteNews(long id) {
        News news = newsRepo.getOne(id);
        newsRepo.delete(news);
        return news;
    }
    

    public boolean modeTextValuesFromNews(String label, String lead, String text, long id,
            RedirectAttributes attributes) {

        News news = newsRepo.getOne(id);
        news.setIngressi(lead);
        news.setLabel(label);
        news.setText(text);

        List<String> errors = new ArrayList<>();
        errors.addAll(newsValidator.validate(news));

        if (errors.isEmpty()) {
            newsRepo.save(news);
            attributes.addFlashAttribute("success", "Uutinen on onnistuneesti päivitetty!");
            return true;
        } else {
            attributes.addFlashAttribute("errors", errors);
        }
        return false;
    }

    //uutisen kategoria poisto
    public void deleteCategoriesFromNews(long id, String[] delCategories,
            RedirectAttributes attributes) {

        News news = newsRepo.getOne(id);
        handleDeleteCategoryInput(delCategories, news);
        List<String> errors = newsValidator.validate(news);
        if (errors.isEmpty()) {
            newsRepo.save(news);

            attributes.addAttribute("success", "Uutiselta on onnistuneesti poistettu kategoriat!");
        } else {
            attributes.addAttribute("errors", errors);
        }
    }

    private void handleDeleteCategoryInput(String[] categories, News n) {
        for (String cat : categories) {
            Category c = catRepo.getOne(Long.parseLong(cat));
            n.deleteCategory(c);
        }
    }

    public void addCategories(long id, String[] delCategories) {
        News news = newsRepo.getOne(id);
        handleCategoryInput(delCategories, news);
        newsRepo.save(news);
    }

    private void handleCategoryInput(String[] categories, News news) {
        for (String categoryId : categories) {
            Category cat = catRepo.getOne(Long.parseLong(categoryId));
            if (cat != null) {
                news.addCategory(cat);
            }
        }
    }
     public List<Category> getNotHavingCategories(News n) {
        List<Category> cats = catRepo.findAll();
        cats.removeAll(n.getCategories());
        return cats;
    }

    public List<Writer> getNotHavingWriters(News n) {
        List<Writer> writers = writerRepo.findAll();
        writers.removeAll(n.getWriters());
        return writers;
    }
    
    public boolean deleteWritersFromNews( long id, String[] delWriters,
            RedirectAttributes attributes) {

        News news = newsRepo.getOne(id);
        handleDeleteWriterInput(delWriters, news);
        //verify

        List<String> errors = newsValidator.validate(news);
        if (errors.isEmpty()) {
            newsRepo.save(news);
            attributes.addAttribute("success", "Uutiselta on onnistuneesti poistettu kirjoittajat!");
            return true;
        } else {
            attributes.addAttribute("errors", errors);
            attributes.addAttribute("news", news);
        }
        return false;
    }

    private void handleDeleteWriterInput(String[] writers, News n) {
        for (String writer : writers) {
            Writer w = writerRepo.getOne(Long.parseLong(writer));
            n.deleteWriter(w);
        }
    }

    public void addWritersToNews( long id, String[] delArtists) {
        News news = newsRepo.getOne(id);
        handleWriterInput(delArtists, news);
        newsRepo.save(news);
    }

    private void handleWriterInput(String[] writers, News news) {
        for (String writerId : writers) {
            Writer w = writerRepo.getOne(Long.parseLong(writerId));
            if (w != null) {
                news.addWriter(w);
            }
        }
    }
}
