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
    private List<String> errors;
    private UserController userController;
    private ItemPropertyManagementController iTypeController;

    public Controllers() throws ClassNotFoundException {
        itemMan = new ItemTypeManager();
        errors = new ArrayList<>();
        userController = new UserController();
        iTypeController = new ItemPropertyManagementController(userController);
    }

    // <editor-fold desc="Item generation and removal">
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(ModelMap model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirects,
            @ModelAttribute(value = "itemsList") ArrayList<ItemType> stuff, @ModelAttribute(value = "checkboxStates") HashMap<String, Boolean> checkboxStates, @ModelAttribute("Sorting") String Sort) throws SQLException {
        String user = userController.getUserFromCookie(request);

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
        String user = userController.getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            redirects.addAttribute("targetURL", "newItem");
            return "redirect:/login";
        } else {
            return "newItem";
        }
    }

    @PostMapping("/removeItem")
    public String removeItem(ModelMap model, HttpServletRequest request, @RequestParam Integer itemID) throws SQLException {
        String user = userController.getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            model.addAttribute("error", "Not logged in, cannot delete");
            return "error";
        }

        itemMan.delete(itemID, user);
        return "redirect:/items";
    }

    public void validateBookParams(Optional<String> author, Optional<String> isbn, Optional<String> bookTitle, Optional<String> year) {
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
    }

    public void validateVideoOrBlogParams(Optional<String> url, Optional<String> title, Optional<String> author) {
        if (url.get().isEmpty()) {
            errors.add("Missing URL");
        }
        if (title.get().isEmpty()) {
            errors.add("Missing Title");
        }
        if (author.get().isEmpty()) {
            errors.add("Missing Poster");
        }
    }

    @PostMapping("/addItem")
    public String addItem(ModelMap model, @RequestParam String type, HttpServletRequest request,
            @RequestParam Optional<String> bookTitle, @RequestParam Optional<String> isbn, @RequestParam Optional<String> year, @RequestParam Optional<String> author, //book
            @RequestParam Optional<String> videoTitle, @RequestParam Optional<String> videoURL, @RequestParam Optional<String> videoPoster, //video 
            @RequestParam Optional<String> blogTitle, @RequestParam Optional<String> blogURL, @RequestParam Optional<String> blogPoster //blog 
    ) throws SQLException {
        clearErrorsBeforeAdding();

        String user = userController.getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            return "redirect:/login";
        }

        switch (type) {
            case "book": {
                System.out.println("Redirected user : " + user);
                validateBookParams(author, isbn, bookTitle, year);
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
                validateVideoOrBlogParams(videoURL, videoTitle, videoPoster);
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
                validateVideoOrBlogParams(blogURL, blogTitle, blogPoster);
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
    //<editor-fold desc="HTML Page getters">
    @RequestMapping(value = "/SelectWhatTypesAreShown", method = RequestMethod.GET)
    public String showSelectedItems(ModelMap model, RedirectAttributes redirects, HttpServletRequest request,
            @RequestParam(defaultValue = "false") boolean ViewBooks, @RequestParam(defaultValue = "false") boolean ViewBlogs, @RequestParam(defaultValue = "false") boolean ViewVideos,
            @RequestParam(defaultValue = "false") boolean ViewRead, @RequestParam(defaultValue = "false") boolean ViewUnread, @RequestParam(required = false) String tags, @RequestParam(required = true) String SortingSelect
    ) throws SQLException {
        String user = userController.getUserFromCookie(request);

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

        String user = userController.getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            model.addAttribute("error", "not logged in, please login before attempting to view an entry");
            return "error";
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
            model.addAttribute("error", "not logged in, please login before attempting to view an entry");
            return "error";
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
            model.addAttribute("error", "not logged in, please login before attempting to view an entry");
            return "error";
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
        String user = userController.getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            model.addAttribute("error", "You must be logged in to perform this action.");
            return "error";
        }

        if (comment == null || comment.equals("Leave a comment") || comment.length() == 0) {
            model.addAttribute("error", "Empty comments are not allowed!");
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

    @RequestMapping(value = "*/GiveRating", method = RequestMethod.POST)
    private String rate(ModelMap model, HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam String RatingSelect) {
        String user = userController.getUserFromCookie(request);

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
