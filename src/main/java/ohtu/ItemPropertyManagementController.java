/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import ohtu.db.ItemTypeManager;
import ohtu.types.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ColdFish
 */
@Controller
public class ItemPropertyManagementController {

    private ItemTypeManager itemMan;
    protected static List<String> errors;
    private UserController userController;

    public ItemPropertyManagementController(UserController userCtrl) throws ClassNotFoundException {
        itemMan = new ItemTypeManager();
        userController = userCtrl;
        errors = Controllers.errors;
    }

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
    public String markItemAsReadOrUnRead(ModelMap model, HttpServletRequest request, @RequestParam Integer id, @RequestParam(value = "action", required = true) String action) throws SQLException {
        String user = userController.getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            return "redirect:/login";
        }

        switch (action) {

            case "Mark as read": {
                itemMan.markAsRead(id, user);
                return "redirect:/items";
            }

            case "Mark as unread": {
                itemMan.markAsUnRead(id, user);
                return "redirect:/items";
            }

            default: {
                System.out.println(action);
            }
        }

        return "items";
    }

    @RequestMapping(value = "*/edit", method = RequestMethod.POST)
    public String goToEditPage(ModelMap model, HttpServletRequest request, @RequestParam Integer itemId, @RequestParam String itemTypeId, RedirectAttributes redirects) {
        String user = userController.getUserFromCookie(request);

        if (user == null || user.equals("NOT LOGGED IN")) {
            return "redirect:/login";
        }
        redirects.addFlashAttribute(itemId);
        switch (itemTypeId) {
            case "video": {
                return "redirect:/video/" + itemId + "/editVideo";
            }
            case "blog": {
                return "redirect:/blog/" + itemId + "/editBlog";
            }
            case "book": {
                return "redirect:/book/" + itemId + "/editBook";
            }
            default: {
                return "error";
            }
        }
    }

    @RequestMapping(value = "/video/{id}/editVideo", method = RequestMethod.GET)
    public String editVideo(ModelMap model, @PathVariable int id) throws SQLException {
        Video v = itemMan.getVideoMan().findOne(id);
        model.addAttribute("video", v);
        return "editVideo";
    }

    @RequestMapping(value = "/blog/{id}/editBlog", method = RequestMethod.GET)
    public String editBlog(ModelMap model, @PathVariable int id) throws SQLException {
        Blog b = itemMan.getBlogMan().findOne(id);
        model.addAttribute("blog", b);
        return "editBlog";
    }

    @RequestMapping(value = "/book/{id}/editBook", method = RequestMethod.GET)
    public String editBook(ModelMap model, @PathVariable int id) throws SQLException {
        Book b = itemMan.getBookMan().findOne(id);
        model.addAttribute("book", b);
        return "editBook";
    }

    @PostMapping("/video/{id}/SaveVideo")
    public String SaveVid(ModelMap model, @RequestParam String videoTitle, @RequestParam String videoURL, @RequestParam String videoPoster, @PathVariable int id) throws SQLException {
        clearErrorsBeforeAdding();
        Controllers.validateVideoOrBlogParams(videoURL, videoTitle, videoPoster);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("video", new Video(id, videoURL, videoTitle, videoPoster));
            return "editVideo";
        }
        if (videoURL.contains("watch?v=")) {
            videoURL = videoURL.substring(videoURL.indexOf('=') + 1);
        } else if (videoURL.contains("youtu.be")) {
            videoURL = videoURL.substring(videoURL.indexOf('.') + 4);
        }
        itemMan.getVideoMan().edit(id, videoTitle, videoURL, videoPoster);
        return "redirect:/items";
    }

    @PostMapping("/blog/{id}/SaveBlog")
    public String SaveBlog(ModelMap model, @RequestParam String blogTitle, @RequestParam String blogURL, @RequestParam String blogPoster, @PathVariable int id) throws SQLException {
        clearErrorsBeforeAdding();
        Controllers.validateVideoOrBlogParams(blogURL, blogTitle, blogPoster);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("blog", new Blog(id, blogURL, blogTitle, blogPoster));
            return "editBlog";
        }
        itemMan.getBlogMan().edit(id, blogTitle, blogURL, blogPoster);
        return "redirect:/items";
    }

    @PostMapping("/book/{id}/SaveBook")
    public String SaveBook(ModelMap model, @RequestParam String bookTitle, @RequestParam String isbn, @RequestParam String author, @RequestParam String year, @PathVariable int id) throws SQLException {
        clearErrorsBeforeAdding();
        Controllers.validateBookParams(author, isbn, bookTitle, year);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("book", new Book(id, isbn, bookTitle, author, 2018));
            return "editBook";
        }
        int integerYear = Integer.parseInt(year);
        
        try {
            itemMan.getBookMan().edit(id, bookTitle, isbn, author, integerYear);
            return "redirect:/items";
        } catch (Exception e) {
            errors.add(e.toString());
            model.addAttribute("errors", errors);
            return "editBook";
        }
    }

    //</editor-fold>
    //Spacer
    //
    private String addTagForItem(int id, ModelMap model, String tag, ItemType item) throws SQLException {
        clearErrorsBeforeAdding();

        if (tag.isEmpty()) {
            errors.add("Missing tag");
        } else {
            itemMan.addTagToItem(id, tag);
        }
        if (!errors.isEmpty()) {
            model.addAttribute(item.getType().name(), item);
            model.addAttribute("errors", errors);
            return item.getType().name();
        }
        return "redirect:/" + item.getType().name() + '/' + id;
    }

    private void clearErrorsBeforeAdding() {
        errors.clear();
    }

}
