package ohtu;

import ohtu.db.BookManager;
import ohtu.db.Database;
import ohtu.types.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        String addr = "ohmipro.ddns.net";
        String url = "jdbc:sqlserver://" + addr + ":34200;databaseName=OhtuMP;user=ohtuadm;password=hakimi1337";

        Database db = new Database(url);
        BookManager bookMan = new BookManager(db);
        Book diu = bookMan.findOne(1);
        SpringApplication.run(Main.class, args);
    }
}
