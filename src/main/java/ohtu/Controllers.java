package ohtu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import ohtu.db.*;
import ohtu.types.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

@Controller
public class Controllers {

    private final ItemTypeManager itemMan;
    private LoginManager loginMan;
    private List<String> errors;

    public Controllers() throws ClassNotFoundException {
        itemMan = new ItemTypeManager();
        errors = new ArrayList<>();
        loginMan = new LoginManager();
    }

    // <editor-fold desc="Login management">
    @PostMapping("/login")
    public String login(ModelMap model, HttpServletRequest request, HttpServletResponse response, @RequestParam String username, @RequestParam String password, @RequestParam("targetURL") String target) {
        if (username.equals("default") || username.equals("testUser")) {
            Cookie cookie = new Cookie("user", username);
            cookie.setValue(username);
            response.addCookie(cookie);
            return "redirect:/" + target;
        } else {
            if (loginMan.login(username, password)) {
                Cookie cookie = new Cookie("user", username);
                cookie.setValue(username);
                response.addCookie(cookie);
                return "redirect:/" + target;
            } else {
                return "redirect:/error";
            }
        }
    }

    @PostMapping("/createUser")
    public String createUser(ModelMap model, @RequestParam String username, @RequestParam String password) {
        if (username.length() < 4) {
            model.addAttribute("error", "Username too short (min 4 length)");
            return "error";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "password too short (min 6 length)");
            return "error";
        }

        loginMan.createUser(username, password);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("user", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model, @ModelAttribute(value = "targetURL") String target) {
        model.addAttribute("targetURL", target);
        return "login";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public String createUser(ModelMap model) {
        return "createUser";
    }

    // </editor-fold>
    // Spacer
    //
    // <editor-fold desc="Item generation and removal">
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(ModelMap model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirects,
            @ModelAttribute(value = "itemsList") ArrayList<ItemType> stuff, @ModelAttribute(value = "checkboxStates") HashMap<String, Boolean> checkboxStates, @ModelAttribute("Sorting") String Sort) throws SQLException {
        String user = getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            redirects.addFlashAttribute("targetURL", "items");
            return "redirect:/login";
        }

        if (stuff == null || stuff.isEmpty()) {
            List<ItemType> items = itemMan.findAll(user);
            model.addAttribute("items", items);
        } else {
            model.addAttribute("items", stuff);
        }

        model.addAttribute("sortSelect", Sort);

        checkboxStates.entrySet().forEach(entry -> {
            model.addAttribute(entry.getKey(), entry.getValue().toString());
        });

        return "itemView";
    }

    @RequestMapping(value = "/newItem", method = RequestMethod.GET)
    public String itemAdd(ModelMap model, HttpServletRequest request, RedirectAttributes redirects) throws SQLException {
        String user = getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            redirects.addAttribute("targetURL", "newItem");
            return "redirect:/login";
        } else {
            return "newItem";
        }
    }

    @PostMapping("/removeItem")
    public String removeItem(ModelMap model, HttpServletRequest request, @RequestParam Integer itemID) throws SQLException {
        String user = getUserFromCookie(request);

        if (!user.equals("NOT LOGGED IN")) {
            itemMan.delete(itemID, user);
            return "redirect:/items";
        }

        model.addAttribute("error", "Not logged in, cannot delete");
        return "redirect:/error";
    }

    @PostMapping("/addItem")
    public String addItem(ModelMap model, @RequestParam String type, HttpServletRequest request,
            @RequestParam Optional<String> bookTitle, @RequestParam Optional<String> isbn, @RequestParam Optional<String> year, @RequestParam Optional<String> author, //book
            @RequestParam Optional<String> videoTitle, @RequestParam Optional<String> videoURL, @RequestParam Optional<String> videoPoster, //video 
            @RequestParam Optional<String> blogTitle, @RequestParam Optional<String> blogURL, @RequestParam Optional<String> blogPoster //blog 
    ) throws SQLException {
        clearErrorsBeforeAdding();

        String user = getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            return "redirect:/login";
        }

        switch (type) {
            case "book": {
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

    // </editor-fold>
    // Spacer
    //
    // <editor-fold desc="Item property management">
    @PostMapping("/book/{id}/addTag")
    public String addTagForBook(@PathVariable int id, RedirectAttributes errs, ModelMap model, @RequestParam String tag) throws SQLException {
        Book book = itemMan.getBookMan().findOne(id);
        return addTagForItem(id, model, tag, book);
    }

    @PostMapping("/blog/{id}/addTag")
    public String addTagForBlog(@PathVariable int id, RedirectAttributes errs, ModelMap model, @RequestParam String tag) throws SQLException {
        Blog blog = itemMan.getBlogMan().findOne(id);
        return addTagForItem(id, model, tag, blog);
    }

    @PostMapping("/video/{id}/addTag")
    public String addTagForVideo(@PathVariable int id, RedirectAttributes errs, ModelMap model, @RequestParam String tag) throws SQLException {
        Video video = itemMan.getVideoMan().findOne(id);
        return addTagForItem(id, model, tag, video);
    }

    @RequestMapping(value = "*/markRead", method = RequestMethod.GET)
    public String markItemAsReadOrUnRead(ModelMap model, HttpServletRequest request, @RequestParam Integer id, @RequestParam(value = "action", required = true) String action) {
        String user = getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            return "error";
        }

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

    //</editor-fold>
    //Spacer
    //
    //<editor-fold desc="HTML Page getters">
    @RequestMapping(value = "/SelectWhatTypesAreShown", method = RequestMethod.GET)
    public String showSelectedItems(ModelMap model, RedirectAttributes redirects, HttpServletRequest request,
            @RequestParam(defaultValue = "false") boolean ViewBooks, @RequestParam(defaultValue = "false") boolean ViewBlogs, @RequestParam(defaultValue = "false") boolean ViewVideos,
            @RequestParam(defaultValue = "false") boolean ViewRead, @RequestParam(defaultValue = "false") boolean ViewUnread, @RequestParam(required = false) String tags, @RequestParam(required = true) String SortingSelect
    ) throws SQLException {
        String user = getUserFromCookie(request);

        List<ItemType> items = new ArrayList<>();
        HashMap<String, Boolean> states = new HashMap<>();

        if (ViewBlogs) {
            items.addAll(itemMan.getBlogMan().findAll(user));
            states.put("ViewBlogs", true);
        } else {
            states.put("ViewBlogs", false);
        }
        if (ViewBooks) {
            items.addAll(itemMan.getBookMan().findAll(user));
            states.put("ViewBooks", true);
        } else {
            states.put("ViewBooks", false);
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
                        .sorted(Comparator.comparing(ItemType::getAuthor).thenComparing(ItemType::getTitle))
                        .collect(Collectors.toList());
                break;
            }
            case "SortByTitle": {
                items = items.stream()
                        .sorted(Comparator.comparing(ItemType::getTitle).thenComparing(ItemType::getType))
                        .collect(Collectors.toList());
                break;
            }
            case "SortByRating": {
                items = items.stream()
                        .sorted(Comparator.comparing(ItemType::getRating).reversed().thenComparing(ItemType::getType))
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

        String user = getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            Book book = itemMan.getBookMan().findOne(id);
            book.setTags(itemMan.getTags(book.getId()));
            book.setComments(itemMan.getCommentsForID(book.getId()));
            model.addAttribute("book", book);
            model.addAttribute("tags", book.getTags());
            model.addAttribute("comments", book.getComments());
            return "book";
        }

        Book book = itemMan.getBookMan().findOne(id, user);
        book.setTags(itemMan.getTags(book.getId()));
        book.setComments(itemMan.getCommentsForID(book.getId()));
        model.addAttribute("book", book);
        model.addAttribute("tags", book.getTags());
        model.addAttribute("comments", book.getComments());
        return "book";
    }

    @RequestMapping(value = "/blog/{id}", method = RequestMethod.GET)
    public String blog(HttpServletRequest request,
            @PathVariable int id, ModelMap model) throws SQLException {

        Cookie[] cookies = request.getCookies();
        String user = "NOT LOGGED IN";
        if (cookies != null) {
            user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findFirst().get().getValue();
        }

        if (user == null || user.equals("NOT LOGGED IN")) {
            Blog blog = itemMan.getBlogMan().findOne(id);
            blog.setTags(itemMan.getTags(id));
            blog.setComments(itemMan.getCommentsForID(blog.getId()));
            model.addAttribute("blog", blog);
            model.addAttribute("tags", blog.getTags());
            model.addAttribute("comments", blog.getComments());
            return "blog";
        }

        Blog blog = itemMan.getBlogMan().findOne(id, user);
        blog.setTags(itemMan.getTags(id));
        blog.setComments(itemMan.getCommentsForID(blog.getId()));
        model.addAttribute("blog", blog);
        model.addAttribute("tags", blog.getTags());
        model.addAttribute("comments", blog.getComments());
        return "blog";
    }

    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public String video(HttpServletRequest request,
            @PathVariable int id, ModelMap model) throws SQLException {

        Cookie[] cookies = request.getCookies();
        String user = "NOT LOGGED IN";
        if (cookies != null) {
            user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findFirst().get().getValue();
        }

        if (user == null || user.equals("NOT LOGGED IN")) {
            Video video = itemMan.getVideoMan().findOne(id);
            video.setTags(itemMan.getTags(id));
            video.setComments(itemMan.getCommentsForID(video.getId()));
            model.addAttribute("video", video);
            model.addAttribute("tags", video.getTags());
            model.addAttribute("comments", video.getComments());
            return "video";
        }

        Video video = itemMan.getVideoMan().findOne(id, user);
        video.setTags(itemMan.getTags(id));
        video.setComments(itemMan.getCommentsForID(video.getId()));
        model.addAttribute("video", video);
        model.addAttribute("tags", video.getTags());
        model.addAttribute("comments", video.getComments());
        return "video";
    }

    @GetMapping("/author/{author}")
    public String author(@PathVariable String author, ModelMap model) throws SQLException {
        model.addAttribute("author", author);
        model.addAttribute("items", itemMan.getItemsByAuthor(author));
        return "author";
    }

    //</editor-fold>
    //Spacer
    //
    @PostMapping(value = {"/blog/{id}/addComment", "/book/{id}/addComment", "/video/{id}/addComment"})
    public String addCommentToItem(@PathVariable int id, ModelMap model, HttpServletRequest request, @RequestParam String type, @RequestParam String comment) throws SQLException {
        String user = getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            model.addAttribute("error", "You must be logged in to perform this action.");
            return "error";
        }

        try {
            itemMan.addCommentToItem(comment, id, user);
            return "redirect:/" + type + '/' + id;
        } catch (Exception e) {
            model.addAttribute("error", "Adding comment failed. " + e.toString());
        }
        return "error";
    }

    public String getUserFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String user = "NOT LOGGED IN";
        if (cookies != null) {
            Optional<Cookie> cooki = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findFirst();
            if (cooki.isPresent()) {
                user = cooki.get().getValue();
            }
        }
        return user;
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
            }
        }
        if (!errors.isEmpty()) {
            model.addAttribute(item.getType().name(), item);
            model.addAttribute("errors", errors);
            return item.getType().name();
        }
        return "redirect:/" + item.getType().name() + '/' + id;
    }

    @RequestMapping(value = "*/GiveRating", method = RequestMethod.POST)
    private String rate(ModelMap model, HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam String RatingSelect) {
        String user = getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            return "error";
        }

        int rating = 0;

        switch (RatingSelect) {
            case "RatedOne": {
                rating = 1;
                break;
            }
            case "RatedTwo": {
                rating = 2;
                break;
            }
            case "RatedThree": {
                rating = 3;
                break;
            }
            case "RatedFour": {
                rating = 4;
                break;
            }
            case "RatedFive": {
                rating = 5;
                break;
            }
        }

        try {
            if (rating != 0) {
                itemMan.rateItem(rating, id, user);
            }
        } catch (SQLException ex) {
            System.out.println("Rating failed.");
            return "error";
        }

        return "redirect:/items";

    }

    private void clearErrorsBeforeAdding() {
        errors.clear();
    }

}
