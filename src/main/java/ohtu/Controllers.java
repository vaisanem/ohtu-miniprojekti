package ohtu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import ohtu.db.ItemTypeManager;
import ohtu.types.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Controllers {

    private final ItemTypeManager itemMan;
    private List<String> errors;

    public Controllers() throws ClassNotFoundException {
        itemMan = new ItemTypeManager();
        errors = new ArrayList<>();
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(ModelMap model, HttpServletResponse response, @ModelAttribute(value = "itemsList") ArrayList<ItemType> stuff, @ModelAttribute(value = "checkboxStates") HashMap<String, Boolean> checkboxStates, @ModelAttribute("user") String user, @ModelAttribute("Sorting") String Sort) throws SQLException {
        System.out.println("Gotten redirect : " + user);
        if (user.length() < 1) {
            user = "default";
        }

        Cookie cookie = new Cookie("user", user);
        cookie.setValue(user);
        response.addCookie(cookie);

        if (stuff == null || stuff.isEmpty()) {
            List<ItemType> items = itemMan.findAll(user);
            model.addAttribute("items", items);
        } else {
            model.addAttribute("items", stuff);
        }
        model.addAttribute("tags", itemMan.getSetOfAllTags());
        
        model.addAttribute("sortSelect", Sort);
        
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

        clearErrorsBeforeAdding();

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
                    if (e.toString().contains("Violation of UNIQUE KEY constraint")) {
                        errors.add("ISBN already in use");
                    } else {
                        errors.add(e.toString());
                    }
                    model.addAttribute("errors", errors);
                    return "newItem";
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
                    errors.add(e.toString());
                    model.addAttribute("errors", errors);
                    return "newItem";
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
                    errors.add(e.toString());
                    model.addAttribute("errors", errors);
                    return "newItem";
                }
            }

            default: {
                System.out.println("Something went wrong");
                return "error";
            }
        }

    }

    @PostMapping("/book/{id}/addTag")
    public String addTagForBook(@PathVariable int id, RedirectAttributes errs, ModelMap model, @RequestParam String user, @RequestParam String tag) throws SQLException {
        Book book = itemMan.getBookMan().findOne(id);
        return addTagForItem(id, model, tag, book);
    }

    @PostMapping("/blog/{id}/addTag")
    public String addTagForBlog(@PathVariable int id, RedirectAttributes errs, ModelMap model, @RequestParam String user, @RequestParam String tag) throws SQLException {
        Blog blog = itemMan.getBlogMan().findOne(id);
        return addTagForItem(id, model, tag, blog);
    }

    @PostMapping("/video/{id}/addTag")
    public String addTagForVideo(@PathVariable int id, RedirectAttributes errs, ModelMap model, @RequestParam String user, @RequestParam String tag) throws SQLException {
        Video video = itemMan.getVideoMan().findOne(id);
        return addTagForItem(id, model, tag, video);
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
            @RequestParam(defaultValue = "false") boolean ViewRead, @RequestParam(defaultValue = "false") boolean ViewUnread, @RequestParam(required = false) String tags, @RequestParam(required = true) String SortingSelect
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
         */
        List<String> tagses = Arrays.asList(tags.split("\\s*,\\s*"));
        tagses.replaceAll(String::toLowerCase);
        itemMan.getAndApplyTags(items);
        
        if (tagses.size() > 0 && !tagses.get(0).equals("")) {
            items = itemMan.filterByTags(items, tagses);
        }
        switch (SortingSelect) {
            case "SortByAuthor": {
                items = items.stream()
                        .sorted((item1, item2) -> item1.getAuthor().compareTo(item2.getAuthor()))
                        .collect(Collectors.toList());
                break;
            }
            case "SortByTitle": {
                items = items.stream()
                        .sorted((item1, item2) -> item1.getTitle().compareTo(item2.getTitle()))
                        .collect(Collectors.toList());
                break;
            }

            default: {
                items = items.stream()
                        .sorted((item1, item2) -> item1.getType().name().compareTo(item2.getType().name()))
                        .collect(Collectors.toList());
                break;
            }
        }
        redirects.addFlashAttribute("Sorting", SortingSelect);
        redirects.addFlashAttribute(
                "itemsList", items);

        return "redirect:/items";
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public String book(HttpServletRequest request,
            @ModelAttribute(value = "errs") String errors,
            @PathVariable int id, ModelMap model) throws SQLException {
        Cookie[] cookies = request.getCookies();
        String user = "default";
        if (cookies != null) {
            user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findFirst().get().getValue();
        }

        Book book = itemMan.getBookMan().findOne(id, user);
        book.setTags(itemMan.getTags(book.getId()));
        model.addAttribute("book", book);
        model.addAttribute("tags", book.getTags());
        return "book";
    }

    @RequestMapping(value = "/blog/{id}", method = RequestMethod.GET)
    public String blog(HttpServletRequest request,
            @PathVariable int id, ModelMap model) throws SQLException {
        Cookie[] cookies = request.getCookies();
        String user = "default";
        if (cookies != null) {
            user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findFirst().get().getValue();
        }

        Blog blog = itemMan.getBlogMan().findOne(id, user);
        blog.setTags(itemMan.getTags(id));
        model.addAttribute("blog", blog);
        model.addAttribute("tags", blog.getTags());
        return "blog";
    }

    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public String video(HttpServletRequest request,
            @PathVariable int id, ModelMap model) throws SQLException {
        Cookie[] cookies = request.getCookies();
        String user = "default";
        if (cookies != null) {
            user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findFirst().get().getValue();
        }

        Video video = itemMan.getVideoMan().findOne(id, user);
        video.setTags(itemMan.getTags(id));
        model.addAttribute("video", video);
        model.addAttribute("tags", video.getTags());
        return "video";
    }

    private String addTagForItem(int id, ModelMap model, String tag, ItemType item) throws SQLException {
        clearErrorsBeforeAdding();

        if (tag.isEmpty()) {
            errors.add("Missing tag");
        } else {
            try {
                itemMan.addTagToItem(id, tag);
            } catch (Exception e) {
                errors.add(e.toString());
                System.out.println(e.toString());
            }
        }
        model.addAttribute("errors", errors);
        return "redirect:/" + item.getType().name() + '/' + id;
    }

    private void clearErrorsBeforeAdding() {
        errors.clear();
    }

}
