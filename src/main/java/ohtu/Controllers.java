package ohtu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public Controllers() throws ClassNotFoundException {
        itemMan = new ItemTypeManager();
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(ModelMap model, @ModelAttribute(value = "itemsList") ArrayList<ItemType> stuff, @ModelAttribute(value = "checkboxStates") HashMap<String, Boolean> checkboxStates, @ModelAttribute("user") String user) throws SQLException {
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

        checkboxStates.entrySet().forEach(entry -> {
            model.addAttribute(entry.getKey(), entry.getValue().toString());
        });

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
                    model.addAttribute("error", "year not numeric or missing");
                    return "error";
                } else {
                    intYear = Integer.parseInt(year.get());
                }

                try {
                    userAttribute.addFlashAttribute("user", user);
                    System.out.println("Redirected user : " + user);
                    if (isbn.get().isEmpty()) {
                        model.addAttribute("error", "Missing ISBN");
                        return "error";
                    }
                    if (bookTitle.get().isEmpty()) {
                        model.addAttribute("error", "Missing Title");
                        return "error";
                    }
                    if (author.get().isEmpty()) {
                        model.addAttribute("error", "Missing Author");
                        return "error";
                    }
                    if (year.get().isEmpty()) {
                        model.addAttribute("error", "Missing Year");
                        return "error";
                    }
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
                try {
                    userAttribute.addFlashAttribute("user", user);
                    System.out.println("Redirected user : " + user);
                    if (videoURL.get().isEmpty()) {
                        model.addAttribute("error", "Missing URL");
                        return "error";
                    }
                    if (videoTitle.get().isEmpty()) {
                        model.addAttribute("error", "Missing Title");
                        return "error";
                    }
                    if (videoPoster.get().isEmpty()) {
                        model.addAttribute("error", "Missing Poster");
                        return "error";
                    }
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
                try {
                    userAttribute.addFlashAttribute("user", user);
                    System.out.println("Redirected user : " + user);
                    if (blogURL.get().isEmpty()) {
                        model.addAttribute("error", "Missing URL");
                        return "error";
                    }
                    if (blogTitle.get().isEmpty()) {
                        model.addAttribute("error", "Missing Title");
                        return "error";
                    }
                    if (blogPoster.get().isEmpty()) {
                        model.addAttribute("error", "Missing Poster");
                        return "error";
                    }
                    Blog blog = new Blog(blogURL.get(), blogTitle.get(), blogPoster.get());
                    itemMan.getBlogMan().add(blog, user);
                    return "redirect:/items";
                } catch (Exception e) {
                    System.out.println("Error occurred :" + e.getMessage());
                    model.addAttribute("error", e.getMessage());
                    return "newItem";
                    //return "error";
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
    public String showSelectedItems(ModelMap model, RedirectAttributes redirects, @RequestParam String user,
            @RequestParam(defaultValue = "false") boolean ViewBooks, @RequestParam(defaultValue = "false") boolean ViewBlogs, @RequestParam(defaultValue = "false") boolean ViewVideos,
            @RequestParam(defaultValue = "false") boolean ViewRead, @RequestParam(defaultValue = "false") boolean ViewUnread
    ) throws SQLException {
        if (user.length() < 1) {
            user = "default";
        }

        List<ItemType> items = new ArrayList<>();
        HashMap<String, Boolean> states = new HashMap<>();

        if (ViewBooks) {
            items.addAll(itemMan.getBookMan().findAll(user));
            states.put("ViewBooks", true);
        } else {
            states.put("ViewBooks", false);
        }

        if (ViewBlogs) {
            items.addAll(itemMan.getBlogMan().findAll(user));
            states.put("ViewBlogs", true);
        } else {
            states.put("ViewBlogs", false);
        }

        if (ViewVideos) {
            items.addAll(itemMan.getVideoMan().findAll(user));
            states.put("ViewVideos", true);
        } else {
            states.put("ViewVideos", false);
        }

        if (ViewRead) {
            states.put("ViewRead", true);
        } else {
            states.put("ViewRead", false);
        }

        if (ViewUnread) {
            states.put("ViewUnread", true);
        } else {
            states.put("ViewUnread", false);
        }

        if (ViewRead && !ViewUnread) {
            items.removeIf(item -> item.getIsRead() == 0);
        }

        if (!ViewRead && ViewUnread) {
            items.removeIf(item -> item.getIsRead() == 1);
        }

        if (items.isEmpty()) {
            model.addAttribute("error", "No items match selection, please try again!");
            return "error";
        }

        redirects.addFlashAttribute("checkboxStates", states);
        
        /* This line of code will filter the items that were selected with checkboxes so that they are also filtered by TAGS.
        TODO : Make UI implmementation to insert comma delimited tags that are split into a List of Strings, each string representing a Tag.
        Works like > filterByTags method returns the items that match the tags defined in the Tags list.
        
        List<String> tags = new ArrayList<>();
        itemMan.getAndApplyTags(items);
        items = itemMan.filterByTags(items, tags );
        */
        
        
        
        redirects.addFlashAttribute(
                "itemsList", items);

        return "redirect:/items";
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public String book(@PathVariable int id, ModelMap model) throws SQLException {
        Book book = itemMan.getBookMan().findOne(id);
        book.setTags(itemMan.getTags(book.getId()));
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
