package ohtu;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import ohtu.db.ItemTypeManager;
import ohtu.types.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Controllers {

    private final ItemTypeManager itemMan;

    public Controllers() throws ClassNotFoundException {
        itemMan = new ItemTypeManager();
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(ModelMap model, @ModelAttribute("user") String user) throws SQLException {
        System.out.println("Gotten redirect : " + user);
        if (user.length() < 1) {
            user = "default";
        }
        List<ItemType> items = itemMan.findAll(user);
        model.addAttribute("items", items);
        return "itemView";
    }

    @RequestMapping(value = "/newItem", method = RequestMethod.GET)
    public String itemAdd(ModelMap model) throws SQLException {
        return "newItem";
    }

    @PostMapping("/addItem")
    public String addItem(ModelMap model, RedirectAttributes userAttribute, @RequestParam String user, @RequestParam String type,
            @RequestParam Optional<String> bookTitle, @RequestParam Optional<String> isbn, @RequestParam Optional<String> year, @RequestParam Optional<String> author, //book
            @RequestParam Optional<String> videoTitle, @RequestParam Optional<String> videoURL, @RequestParam Optional<String> videoPoster, //video 
            @RequestParam Optional<String> blogTitle, @RequestParam Optional<String> blogURL, @RequestParam Optional<String> blogPoster //blog 
    ) throws SQLException {

        switch (type) {
            case "book": {
                int intYear;
                if (!year.get().matches("[0-9]+")) {
                    model.addAttribute("error", "year not numeric");
                    return "error";
                } else {
                    intYear = Integer.parseInt(year.get());
                }

                try {
                    userAttribute.addFlashAttribute("user", user);
                    System.out.println("Redirected user : " + user);
                    Book book = new Book(isbn.get(), bookTitle.get(), author.get(), intYear);
                    itemMan.getBookMan().add(book, user);
                    return "redirect:/items";
                } catch (Exception e) {
                    //model.addAttribute("error", e.getMessage());
                    //return "newItem";
                    return "error";
                }
            }

            case "video": {
                try {
                    userAttribute.addFlashAttribute("user", user);
                    System.out.println("Redirected user : " + user);
                    Video vid = new Video(videoURL.get(), videoTitle.get(), videoPoster.get());
                    itemMan.getVideoMan().add(vid, user);
                    return "redirect:/items";
                } catch (Exception e) {
                    //model.addAttribute("error", e.getMessage());
                    //return "newItem";
                    return "error";
                }
            }
            
            case "blog": {
                try {
                    userAttribute.addFlashAttribute("user", user);
                    System.out.println("Redirected user : " + user);
                    Blog blog = new Blog(blogURL.get(), blogTitle.get(), blogPoster.get());
                    itemMan.getBlogMan().add(blog, user);
                    return "redirect:/items";
                } catch (Exception e) {
                    model.addAttribute("error", e.getMessage());
                    return "newItem";
                    //return "error";
                }
            }

            default: {
                return "error";
            }
        }

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public String book(@PathVariable int id, ModelMap model) throws SQLException {
        Book book = itemMan.getBookMan().findOne(id);
        model.addAttribute("book", book);
        return "book";
    }

    @RequestMapping(value = "/blog/{id}", method = RequestMethod.GET)
    public String blog(@PathVariable int id, ModelMap model) throws SQLException {
        Blog blog = itemMan.getBlogMan().findOne(id);
        model.addAttribute("blog", blog);
        return "blog";
    }

    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public String video(@PathVariable int id, ModelMap model) throws SQLException {
        Video video = itemMan.getVideoMan().findOne(id);
        model.addAttribute("video", video);
        return "video";
    }
    
}
