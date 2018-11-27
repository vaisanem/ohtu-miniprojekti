package ohtu;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ohtu.db.ItemTypeManager;
import ohtu.types.Book;
import ohtu.types.ItemType;
import ohtu.types.Video;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Stepdefs {

    private WebDriver driver;
    private String baseUrl;
    private WebElement element;
    private Random random;
    private ItemTypeManager itemMan;
    private final int SleepTime;

    public Stepdefs() throws ClassNotFoundException {
        File file;
        itemMan = new ItemTypeManager();
        SleepTime = 150;
        if (System.getProperty("os.name").matches("Mac OS X")) {
            file = new File("lib/macgeckodriver");
        } else {
            file = new File("lib/geckodriver");
        }
        String absolutePath = file.getAbsolutePath();
        System.setProperty("webdriver.gecko.driver", absolutePath);

        if (System.getProperty("os.name").matches("Windows 10")) {
            //this.driver = new ChromeDriver();
            this.driver = new HtmlUnitDriver(true);
        } else {
            //this.driver = new ChromeDriver();
            //this.driver = new FirefoxDriver();
            this.driver = new HtmlUnitDriver(true);
        }
        baseUrl = "http://localhost:" + 8080 + "/";
        random = new Random();
    }

    @After
    public void tearDown() throws SQLException {
        itemMan.getVideoMan().deleteALLTestGeneratedVideos();
        itemMan.closeConnection();
        driver.quit();
    }

    // <editor-fold desc="generic methods">
    @Given("^user is at the main page$")
    public void user_is_at_the_main_page() throws Throwable {
        driver.get(baseUrl);
        Thread.sleep(SleepTime);
    }

    @When("^user is redirected to \"([^\"]*)\"$")
    public void user_is_redirected_to(String arg1) throws Throwable {
        Thread.sleep(SleepTime);
        System.out.println("Current url : " + driver.getCurrentUrl() + ", expected to find string :" + arg1);
        boolean isRedirected = driver.getCurrentUrl().contains(arg1);
        assertTrue(isRedirected);
        Thread.sleep(SleepTime);
    }

    @When("^link for \"([^\"]*)\" named \"([^\"]*)\" is clicked$")
    public void link_for_named_is_clicked(String arg1, String arg2) throws Throwable {
        Thread.sleep(SleepTime);
        clickLinkWithText(arg2, arg1);
        Thread.sleep(SleepTime);
    }

    @When("^link \"([^\"]*)\" is clicked$")
    public void link_is_clicked(String link) throws Throwable {
        Thread.sleep(SleepTime);
        clickLinkWithText(link);
        Thread.sleep(SleepTime);
    }

    @Then("^\"([^\"]*)\" is shown$")
    public void is_shown(String content) throws Throwable {
        Thread.sleep(SleepTime);
        boolean isShown = false;
        for (int i = 0; i < 5; i++) {
            Thread.sleep(SleepTime);
            System.out.println("Expected element : " + content);
            //System.out.println("Page source : " + driver.getPageSource().toString());
            if (driver.getPageSource().contains(content)) {
                isShown = true;
                break;
            }
        }
        assertTrue(isShown);
        Thread.sleep(SleepTime);
    }

    private void listOfAllItemsIsShown(List<ItemType> items) throws Throwable {

        Boolean EverythingIsThere = true;
        for (ItemType item : items) {
            System.out.println(item.getTitle()); // Debugging purposes, check which videos were gotten
            Boolean found = false;
            Thread.sleep(SleepTime);
            for (int i = 0; i < 4; i++) {
                Thread.sleep(SleepTime);
                if (driver.getPageSource().contains(item.getTitle())) {
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

    private void clickLinkWithText(String text) {
        boolean found = false;
        int trials = 0;
        while (trials++ < 10) {
            try {
                Thread.sleep(SleepTime);
                WebElement element = driver.findElement(By.linkText(text));
                if (element != null) {
                    element.click();
                    found = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }

        if (!found) {
            System.out.println("Link " + text + " was never found....");
        }
    }

    private void clickLinkWithText(String text, String secondText) {
        int trials = 0;
        while (trials++ < 10) {
            try {
                Thread.sleep(SleepTime);
                List<WebElement> elements = driver.findElements(By.partialLinkText(text));
                WebElement element = elements.stream().filter(elem -> elem.getText().contains(secondText)).findFirst().get();
                if (element != null) {
                    element.click();
                    break;
                }
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
        Thread.sleep(SleepTime);
        clickLinkWithText(one.getTitle().trim(), "book");
        Thread.sleep(SleepTime);
    }

    @When("^book fields title \"([^\"]*)\", isbn \"([^\"]*)\", author and year \"([^\"]*)\" are filled and submitted$")
    public void book_fields_are_submitted(String title, String isbn, String year) throws Throwable {
        Thread.sleep(SleepTime);
        if (isbn.isEmpty()) {
            isbn = Integer.toString(Math.abs(random.nextInt()));
        }
        findElementAndFill("bookTitle", title);
        findElementAndFill("isbn", isbn);
        findElementAndFill("author", "Testaaja");
        findElementAndFill("year", year);

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBook').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new book"));
        element.submit();
        Thread.sleep(SleepTime);
        //driver.get(baseUrl + "books");
    }

    @Then("^individual book is shown$")
    public void individual_book_is_shown() throws Throwable {
        //driver.get(baseUrl + "books/");
        Book one = itemMan.getBookMan().findAll("default").get(0);
        Thread.sleep(SleepTime);
        is_shown(one.getTitle());
        Thread.sleep(SleepTime);
        is_shown(one.getIsbn());
        Thread.sleep(SleepTime);
        is_shown(one.getAuthor());
        Thread.sleep(SleepTime);
        is_shown(Integer.toString(one.getYear()));
    }

    @Then("^List of all books is shown$")
    public void list_of_all_books_is_shown() throws Throwable {
        List<ItemType> books = new ArrayList<>();
        books.addAll(itemMan.getBookMan().findAll("default"));
        listOfAllItemsIsShown(books);
    }

    @When("^book fields isbn, author and year are filled and submitted$")
    public void book_fields_isbn_author_and_year_are_filled_and_submitted() throws Throwable {
        Thread.sleep(SleepTime);
        findElementAndFill("isbn", Integer.toString(Math.abs(random.nextInt())));
        findElementAndFill("author", "Testaaja");
        findElementAndFill("year", "2008");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBook').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new book"));
        element.submit();
        Thread.sleep(SleepTime);
        //driver.get(baseUrl + "books");
    }

    @When("^book fields Title, author and year are filled and submitted$")
    public void book_fields_Title_author_and_year_are_filled_and_submitted() throws Throwable {
        Thread.sleep(SleepTime);
        findElementAndFill("bookTitle", "Test");
        findElementAndFill("author", "Testaaja");
        findElementAndFill("year", "2008");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBook').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new book"));
        element.submit();
        Thread.sleep(SleepTime);
        //driver.get(baseUrl + "books");
    }

    @When("^book fields isbn, title and year are filled and submitted$")
    public void book_fields_isbn_title_and_year_are_filled_and_submitted() throws Throwable {
        Thread.sleep(SleepTime);
        findElementAndFill("bookTitle", "Test");
        findElementAndFill("isbn", Integer.toString(Math.abs(random.nextInt())));
        findElementAndFill("year", "2008");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBook').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new book"));
        element.submit();
        Thread.sleep(SleepTime);
        //driver.get(baseUrl + "books");
    }

    @When("^book fields isbn, author and Title are filled and submitted$")
    public void book_fields_isbn_author_and_Title_are_filled_and_submitted() throws Throwable {
        Thread.sleep(SleepTime);
        findElementAndFill("bookTitle", "Test");
        findElementAndFill("isbn", Integer.toString(Math.abs(random.nextInt())));
        findElementAndFill("author", "Testaaja");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBook').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new book"));
        element.submit();
        Thread.sleep(SleepTime);
        //driver.get(baseUrl + "books");
    }

    // </editor-fold>
    //                  spacer
    // <editor-fold desc="video testing">
    @When("^link to video's page is clicked$")
    public void link_to_video_s_page_is_clicked() throws Throwable {
        Video one = itemMan.getVideoMan().findAll("default").get(0);
        Thread.sleep(SleepTime);
        clickLinkWithText(one.getTitle().trim(), "video");
        Thread.sleep(SleepTime);
    }

    @Then("^individual video is shown$")
    public void individual_video_is_shown() throws Throwable {
        Video one = itemMan.getVideoMan().findAll("default").get(0);
        Thread.sleep(SleepTime);
        is_shown(one.getTitle().trim());
        Thread.sleep(SleepTime);
        is_shown(one.getPoster().trim());
    }

    @Then("^List of all videos is shown$")
    public void list_of_all_videos_is_shown() throws Throwable {
        List<ItemType> videos = new ArrayList<>();
        videos.addAll(itemMan.getVideoMan().findAll("default"));
        listOfAllItemsIsShown(videos);
    }

    @When("^video fields are filled correctly and submitted$")
    public void video_fields_are_filled_correctly_and_submitted() throws Throwable {
        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "https://www.youtube.com/watch?v=WPvGqX-TXP0");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
    }

    @When("^video fields are filled correctly with short URL and submitted$")
    public void video_fields_are_filled_correctly_with_short_URL_and_submitted() throws Throwable {
        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber2");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "https://youtu.be/XKu_SEDAykw");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
        Thread.sleep(SleepTime);
    }

    @When("^video fields are filled correctly with video ID and submitted$")
    public void video_fields_are_filled_correctly_with_video_ID_and_submitted() throws Throwable {
        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber3");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "uWzPe_S-RVE");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
        Thread.sleep(SleepTime);
    }

    @When("^video fields title and poster are filled correctly and submitted\\.$")
    public void video_fields_title_and_poster_are_filled_correctly_and_submitted() throws Throwable {
        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber3");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
        Thread.sleep(SleepTime);
    }

    @When("^video fields URL and Poster are filled and submitted$")
    public void video_fields_URL_and_Poster_are_filled_and_submitted() throws Throwable {
        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "WPvGqX-TXP0");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
    }

    @When("^video fields URL and Title are filled and submitted$")
    public void video_fields_URL_and_Title_are_filled_and_submitted() throws Throwable {
        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber3");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "WPvGqX-TXP0");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
    }

    // </editor-fold>
    //                  spacer
    // <editor-fold desc="blog testing">
    @When("^blog fields title \"([^\"]*)\" and others are filled and submitted$")
    public void blog_fields_are_submitted(String title) throws Throwable {
        driver.findElement(By.id("blog")).click();
        System.out.println("Attempted to click blog radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("blogTitle", title);
        Thread.sleep(SleepTime);
        findElementAndFill("blogPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("blogURL", "https://protesters.com/blogs/1");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBlog').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new blog"));
        element.submit();
        //driver.get(baseUrl + "books");
    }

    @Then("^list of all blogs is shown$")
    public void list_of_all_blogs_is_shown() throws Throwable {
        List<ItemType> blogs = new ArrayList<>();
        blogs.addAll(itemMan.getBlogMan().findAll("default"));
        listOfAllItemsIsShown(blogs);
    }

    @When("^blog fields title and poster are filled correctly and submitted\\.$")
    public void blog_fields_title_and_poster_are_filled_correctly_and_submitted() throws Throwable {
        driver.findElement(By.id("blog")).click();
        System.out.println("Attempted to click blog radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("blogTitle", "TestTitle");
        Thread.sleep(SleepTime);
        findElementAndFill("blogPoster", "Testaaja");
        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBlog').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new blog"));
        element.submit();
        //driver.get(baseUrl + "books");
    }

    @When("^blog fields URL and Poster are filled and submitted$")
    public void blog_fields_URL_and_Poster_are_filled_and_submitted() throws Throwable {
        driver.findElement(By.id("blog")).click();
        System.out.println("Attempted to click blog radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("blogPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("blogURL", "https://protesters.com/blogs/1");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBlog').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new blog"));
        element.submit();
        //driver.get(baseUrl + "books");
    }

    @When("^blog fields URL and Title are filled and submitted$")
    public void blog_fields_URL_and_Title_are_filled_and_submitted() throws Throwable {
        driver.findElement(By.id("blog")).click();
        System.out.println("Attempted to click blog radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("blogTitle", "TestTitle");
        Thread.sleep(SleepTime);
        findElementAndFill("blogURL", "https://protesters.com/blogs/1");

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBlog').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new blog"));
        element.submit();
        //driver.get(baseUrl + "books");
    }

    // </editor-fold>
}
