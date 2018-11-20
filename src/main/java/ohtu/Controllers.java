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

    @GetMapping("/book")
    public String book(@RequestParam(name = "name", required = false, defaultValue = "nerdpunk") String name, Model model) {
        model.addAttribute("name", name);
        return "bookview";
    }

}
