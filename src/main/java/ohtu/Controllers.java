package ohtu;

import java.sql.SQLException;
import java.util.List;
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
        List<ItemType> books = itemMan.findAll(user);
        model.addAttribute("items", books);
        return "itemView";
    }

    @RequestMapping(value = "/newItem", method = RequestMethod.GET)
    public String itemAdd(ModelMap model) throws SQLException {
        return "newItem";
    }

    @PostMapping("/addItem")
    public String addItem(ModelMap model, RedirectAttributes userAttribute, @RequestParam String type, @RequestParam String title, @RequestParam String isbn, @RequestParam String year, @RequestParam String author, @RequestParam String user) throws SQLException {

        switch (type) {
            case "book": {
                int intYear;
                if (!year.matches("[0-9]+")) {
                    model.addAttribute("error", "year not numeric");
                    return "error";
                } else {
                    intYear = Integer.parseInt(year);
                }
                
                try {
                    userAttribute.addFlashAttribute("user", user);
                    System.out.println("Redirected user : " + user);
                    Book book = new Book(isbn, title, author, intYear);
                    itemMan.getBookMan().add(book, user);
                    return "redirect:/items";
                } catch (Exception e) {
                    //model.addAttribute("error", e.getMessage());
                    //return "newItem";
                    return "error";
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
        Blog blog = null;  // TODO -> missing manager.
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
