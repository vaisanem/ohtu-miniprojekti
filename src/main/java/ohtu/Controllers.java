package ohtu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ohtu.db.BookManager;
import ohtu.db.Database;
import ohtu.types.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Controllers {

    private Database db;
    private BookManager bookMan;

    public Controllers() throws ClassNotFoundException {
        String addr = "ohmipro.ddns.net";
        String url = "jdbc:sqlserver://" + addr + ":34200;databaseName=OhtuMP;user=ohtuadm;password=hakimi1337";

        db = new Database(url);
        bookMan = new BookManager(db);
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String books(ModelMap model) throws SQLException {
        List<Book> books = bookMan.findAll();
        model.addAttribute("books", books);
        return "bookview";
    }

    @RequestMapping(value = "/newItem", method = RequestMethod.GET)
    public String itemAdd(ModelMap model) throws SQLException {
        return "newItem";
    }

    @PostMapping("/addItem")
    public String addItem(ModelMap model, @RequestParam String title, @RequestParam String isbn, @RequestParam Integer year, @RequestParam String author) throws SQLException {
       Book book = new Book(isbn, title, author, year);
       boolean succeeded = bookMan.addBook(book);
       return "redirect:/books";
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public String book(@PathVariable int id, ModelMap model) throws SQLException {
        Book book = bookMan.findOne(id);
        model.addAttribute("book", book);
        return "book";
    }

}
