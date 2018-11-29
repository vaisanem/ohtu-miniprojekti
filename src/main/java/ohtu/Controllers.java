package ohtu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import ohtu.db.ItemTypeManager;
import ohtu.types.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Controllers {

    private final ItemTypeManager itemMan;
    private List<String> errors;

    public Controllers() throws ClassNotFoundException {
        itemMan = new ItemTypeManager();
        errors = new ArrayList<>();
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(ModelMap model, @ModelAttribute(value = "itemsList") ArrayList<ItemType> stuff, @ModelAttribute("user") String user) throws SQLException {
        System.out.println("Gotten redirect : " + user);
        if (user.length() < 1) {
            user = "default";
        }
        if (stuff == null || stuff.isEmpty()) {
            List<ItemType> items = itemMan.findAll(user);
            model.addAttribute("items", items);
        } else {
            model.addAttribute("items", stuff);
        }
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
        
        errors.clear();
        
        switch (type) {
            case "book": {
                userAttribute.addFlashAttribute("user", user);
                System.out.println("Redirected user : " + user);
                if (!Book.checkNumericality(year.get())) {
                    errors.add("Missing year or not numeric");
                }
                if (isbn.get().isEmpty()) {
                    errors.add("Missing ISBN");
                }
                if (bookTitle.get().isEmpty()) {
                    errors.add("Missing Title");
                }
                if (author.get().isEmpty()) {
                    errors.add("Missing Author");
                }
                if (!errors.isEmpty()) {
                    model.addAttribute("errors", errors);
                    return "newItem";
                }
                int intYear = Integer.parseInt(year.get());

                try {
                    Book book = new Book(isbn.get(), bookTitle.get(), author.get(), intYear);
                    itemMan.getBookMan().add(book, user);
                    return "redirect:/items";
                } catch (Exception e) {
                     System.out.println("Error occurred :" + e.getMessage());
                    //model.addAttribute("error", e.getMessage());
                    //return "newItem";
                    return "error";

                }
            }

            case "video": {
                userAttribute.addFlashAttribute("user", user);
                System.out.println("Redirected user : " + user);
                if (videoURL.get().isEmpty()) {
                    errors.add("Missing URL");
                }
                if (videoTitle.get().isEmpty()) {
                    errors.add("Missing Title");
                }
                if (videoPoster.get().isEmpty()) {
                    errors.add("Missing Poster");
                }
                if (!errors.isEmpty()) {
                    model.addAttribute("errors", errors);
                    return "newItem";
                }
                try {
                    Video vid = new Video(videoURL.get(), videoTitle.get(), videoPoster.get());
                    itemMan.getVideoMan().add(vid, user);
                    return "redirect:/items";
                } catch (Exception e) {
                     System.out.println("Error occurred :" + e.getMessage());
                    //model.addAttribute("error", e.getMessage());
                    //return "newItem";
                    return "error";
                }
            }

            case "blog": {
                userAttribute.addFlashAttribute("user", user);
                System.out.println("Redirected user : " + user);
                if (blogURL.get().isEmpty()) {
                    errors.add("Missing URL");
                }
                if (blogTitle.get().isEmpty()) {
                    errors.add("Missing Title");
                }
                if (blogPoster.get().isEmpty()) {
                    errors.add("Missing Poster");
                }
                if (!errors.isEmpty()) {
                    model.addAttribute("errors", errors);
                    return "newItem";
                }
                try {
                    Blog blog = new Blog(blogURL.get(), blogTitle.get(), blogPoster.get());
                    itemMan.getBlogMan().add(blog, user);
                    return "redirect:/items";
                } catch (Exception e) {
                    System.out.println("Error occurred :" + e.getMessage());
                    model.addAttribute("error", e.getMessage());

                    return "newItem";
                }
            }

            default: {
                System.out.println("Something went wrong");
                return "error";
            }
        }

    }

    @RequestMapping(value = "*/markRead", method = RequestMethod.GET)
    public String markItemAsReadOrUnRead(ModelMap model, @RequestParam Integer id, @RequestParam String user, @RequestParam(value = "action", required = true) String action) {
        switch (action) {
            case "Mark as read": {
                try {
                    itemMan.markAsRead(id, user);
                    return "redirect:/items";
                } catch (SQLException ex) {
                    model.addAttribute("error", "marking book as read failed... Error stack : " + ex.toString());
                    return "error";
                }
            }

            case "Mark as unread": {
                try {
                    itemMan.markAsUnRead(id, user);
                    return "redirect:/items";
                } catch (SQLException ex) {
                    model.addAttribute("error", "marking book as unread failed... Error stack : " + ex.toString());
                    return "error";
                }
            }

            default: {
                System.out.println(action);
            }
        }

        return "items";
    }

    @RequestMapping(value = "/SelectWhatTypesAreShown", method = RequestMethod.GET)
    public String showSelectedItems(ModelMap model,RedirectAttributes itemsList, @RequestParam String user, @RequestParam(defaultValue = "false") boolean ViewBooks, @RequestParam(defaultValue = "false") boolean ViewBlogs, @RequestParam(defaultValue = "false") boolean ViewVideos) throws SQLException {
        if (user.length() < 1) {
            user = "default";
        }

        List<ItemType> items = new ArrayList<>();
        
        if (ViewBooks) {
            items.addAll(itemMan.getBookMan().findAll(user));
            
        }
        if (ViewBlogs) {
            items.addAll(itemMan.getBlogMan().findAll(user));
            
        }
        if (ViewVideos) {
            items.addAll(itemMan.getVideoMan().findAll(user));
        }
        System.out.println("ADDING TO LIST WORKED");
        itemsList.addFlashAttribute("itemsList", items);
        System.out.println("POST WORKED");
        return "redirect:/items";
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public String book(@PathVariable int id, ModelMap model) throws SQLException {
        Book book = itemMan.getBookMan().findOne(id);
        model.addAttribute("book", book);
        model.addAttribute("tags", book.getTags());
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
