package ohtu;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import ohtu.db.BookManager;
import ohtu.db.Database;
import ohtu.types.Book;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Stepdefs {

    private WebDriver driver;
    private String baseUrl;
    private WebElement element;
    private Random random;
    private Database db;
    private BookManager bookMan;

    public Stepdefs() throws ClassNotFoundException {
        File file;
        String addr = "ohmipro.ddns.net";
        String url = "jdbc:sqlserver://" + addr + ":34200;databaseName=OhtuMP;user=ohtuadm;password=hakimi1337";
        db = new Database(url);
        bookMan = new BookManager(db);
        
        if (System.getProperty("os.name").matches("Mac OS X")) {
            file = new File("lib/macgeckodriver");
        } else {
            file = new File("lib/geckodriver");
        }
        String absolutePath = file.getAbsolutePath();
        System.setProperty("webdriver.gecko.driver", absolutePath);

        if (System.getProperty("os.name").matches("Windows 10")) {
            this.driver = new ChromeDriver();
        } else {
            //this.driver = new ChromeDriver();
            this.driver = new FirefoxDriver();
        }
        baseUrl = "http://localhost:" + 8080 + "/";
        random = new Random();
    }

    @After
    public void tearDown() throws SQLException {
        db.getConnection().close();
        driver.quit();
    }

    @Given("^user is at the main page$")
    public void user_is_at_the_main_page() throws Throwable {
        driver.get(baseUrl);
        Thread.sleep(1000);
    }

    @When("^link \"([^\"]*)\" is clicked$")
    public void link_is_clicked(String link) throws Throwable {
        Thread.sleep(1000);
        clickLinkWithText(link);
        Thread.sleep(1000);
    }
    
    @When("^link to book's page is clicked$")
    public void link_to_book_page_is_clicked() throws Throwable {
        Book one = bookMan.findAll("default").get(0);
        Thread.sleep(1000);
        clickLinkWithText(one.getTitle().trim());
        Thread.sleep(1000);
    }

    @When("^book fields title \"([^\"]*)\", isbn \"([^\"]*)\", author and year \"([^\"]*)\" are filled and submitted$")
    public void book_fields_are_submitted(String title, String isbn, String year) throws Throwable {
        Thread.sleep(1000);
        if (isbn.isEmpty()) {
            isbn = Integer.toString(Math.abs(random.nextInt()));
        }
        findElementAndFill("title", title);
        findElementAndFill("isbn", isbn);
        findElementAndFill("author", "Testaaja");
        findElementAndFill("year", year);

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('user').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new book"));
        element.submit();
        Thread.sleep(1000);
        //driver.get(baseUrl + "books");
    }
    
    @Then("^\"([^\"]*)\" is shown$")
    public void is_shown(String content) throws Throwable {
        boolean isShown = false;
        for(int i =0;i<5;i++){
            if(driver.getPageSource().contains(content)){
                isShown = true;
                break;
            }
        }
        assertTrue(isShown);
    }

    @Then("^List of all books is shown$")
    public void list_of_all_books_is_shown() throws Throwable {
        List<Book> Books = bookMan.findAll("default");

        // Debugging purposes, check which books were gotten
        for (Book book : Books) {
            System.out.println(book.getTitle());
        }

        Boolean EverythingIsThere = true;
        for (Book book : Books) {
            Boolean found = false;
            Thread.sleep(150);
            for (int i = 0; i < 4; i++) {
                Thread.sleep(150);
                if (driver.getPageSource().contains(book.getTitle())) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                EverythingIsThere = false;
            }

        }
        assertTrue(EverythingIsThere);
    }
    
    @Then("^individual book is shown$")
    public void individual_book_is_shown() throws Throwable {
        //driver.get(baseUrl + "books/");
        Book one = bookMan.findAll("default").get(0);    
        Thread.sleep(150);
        is_shown(one.getTitle());
        Thread.sleep(150);
        is_shown(one.getIsbn());
        Thread.sleep(150);
        is_shown(one.getAuthor());
        Thread.sleep(150);
        is_shown(Integer.toString(one.getYear()));
    }

    private void clickLinkWithText(String text) {
        int trials = 0;
        while (trials++ < 5) {
            try {
                WebElement element = driver.findElement(By.linkText(text));
                element.click();
                break;
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    }

    private void findElementAndFill(String name, String value) {
        element = driver.findElement(By.name(name));
        element.sendKeys(value);
    }

}
