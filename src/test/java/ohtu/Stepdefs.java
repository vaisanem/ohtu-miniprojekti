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
import ohtu.db.ItemTypeManager;
import ohtu.types.Book;
import ohtu.types.Video;
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
    private ItemTypeManager itemMan;

    public Stepdefs() throws ClassNotFoundException {
        File file;
        itemMan = new ItemTypeManager();

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
        itemMan.closeConnection();
        driver.quit();
    }

    // <editor-fold desc="generic methods">
    @Given("^user is at the main page$")
    public void user_is_at_the_main_page() throws Throwable {
        driver.get(baseUrl);
        Thread.sleep(1000);
    }

    @When("^user is redirected to \"([^\"]*)\"$")
    public void user_is_redirected_to(String arg1) throws Throwable {
        Thread.sleep(500);
        boolean isRedirected = driver.getCurrentUrl().contains(arg1);
        assertTrue(isRedirected);
        Thread.sleep(500);
    }

    @When("^link for \"([^\"]*)\" named \"([^\"]*)\" is clicked$")
    public void link_for_named_is_clicked(String arg1, String arg2) throws Throwable {
        Thread.sleep(500);
        clickLinkWithText(arg2, arg1);
        Thread.sleep(500);
    }

    @When("^link \"([^\"]*)\" is clicked$")
    public void link_is_clicked(String link) throws Throwable {
        Thread.sleep(1000);
        clickLinkWithText(link);
        Thread.sleep(1000);
    }

    @Then("^\"([^\"]*)\" is shown$")
    public void is_shown(String content) throws Throwable {
        Thread.sleep(500);
        boolean isShown = false;
        for (int i = 0; i < 5; i++) {
            Thread.sleep(500);
            if (driver.getPageSource().contains(content)) {
                isShown = true;
                break;
            }
        }
        assertTrue(isShown);
        Thread.sleep(500);
    }

    private void clickLinkWithText(String text) {
        int trials = 0;
        while (trials++ < 5) {
            try {
                Thread.sleep(500);
                WebElement element = driver.findElement(By.linkText(text));
                element.click();
                break;
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    }

    private void clickLinkWithText(String text, String secondText) {
        int trials = 0;
        while (trials++ < 5) {
            try {
                Thread.sleep(500);
                List<WebElement> elements = driver.findElements(By.partialLinkText(text));
                WebElement element = elements.stream().filter(elem -> elem.getText().contains(secondText)).findFirst().get();
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

    // </editor-fold>
    //                  spacer
    // <editor-fold desc="Book testing">
    @When("^link to book's page is clicked$")
    public void link_to_book_page_is_clicked() throws Throwable {
        Book one = itemMan.getBookMan().findAll("default").get(0);
        Thread.sleep(1000);
        clickLinkWithText(one.getTitle().trim(), "book");
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

    @Then("^individual book is shown$")
    public void individual_book_is_shown() throws Throwable {
        //driver.get(baseUrl + "books/");
        Book one = itemMan.getBookMan().findAll("default").get(0);
        Thread.sleep(250);
        is_shown(one.getTitle());
        Thread.sleep(250);
        is_shown(one.getIsbn());
        Thread.sleep(250);
        is_shown(one.getAuthor());
        Thread.sleep(250);
        is_shown(Integer.toString(one.getYear()));
    }

    @Then("^List of all books is shown$")
    public void list_of_all_books_is_shown() throws Throwable {
        List<Book> Books = itemMan.getBookMan().findAll("default");

        // Debugging purposes, check which books were gotten
        for (Book book : Books) {
            System.out.println(book.getTitle());
        }

        Boolean EverythingIsThere = true;
        for (Book book : Books) {
            Boolean found = false;
            Thread.sleep(500);
            for (int i = 0; i < 10; i++) {
                Thread.sleep(500);
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

    // </editor-fold>
    //                  spacer
    // <editor-fold desc="video testing">
    @Then("^List of all videos is shown$")
    public void list_of_all_videos_is_shown() throws Throwable {
        List<Video> videos = itemMan.getVideoMan().findAll("default");

        // Debugging purposes, check which videos were gotten
        for (Video video : videos) {
            System.out.println(video.getTitle());
        }

        Boolean EverythingIsThere = true;
        for (Video video : videos) {
            Boolean found = false;
            Thread.sleep(500);
            for (int i = 0; i < 4; i++) {
                Thread.sleep(500);
                if (driver.getPageSource().contains(video.getTitle())) {
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

    @When("^link to video's page is clicked$")
    public void link_to_video_s_page_is_clicked() throws Throwable {
        Video one = itemMan.getVideoMan().findAll("default").get(0);
        Thread.sleep(1000);
        clickLinkWithText(one.getTitle().trim(), "video");
        Thread.sleep(1000);
    }

    @Then("^individual video is shown$")
    public void individual_video_is_shown() throws Throwable {
        Video one = itemMan.getVideoMan().findAll("default").get(0);
        Thread.sleep(500);
        is_shown(one.getTitle());
        Thread.sleep(500);
        is_shown(one.getPoster());
    }

    // </editor-fold>
}
