package ohtu;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import ohtu.db.ItemTypeManager;
import ohtu.types.*;
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
        SleepTime = 100;
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
        itemMan.closeConnection();
        driver.manage().deleteAllCookies();
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

    @When("^field \"([^\"]*)\" is filled with \"([^\"]*)\"$")
    public void field_is_filled_with(String fieldName, String fieldInput) throws Throwable {
        findElementAndFill(fieldName, fieldInput);
    }

    @When("^user clicks button \"([^\"]*)\"$")
    public void user_clicks_button(String buttonID) throws Throwable {
        boolean found = false;
        int trials = 0;
        while (trials++ < 10) {
            try {
                Thread.sleep(SleepTime);
                WebElement element = driver.findElement(By.id(buttonID));
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
            System.out.println("Link " + buttonID + " was never found....");
        }
    }
    
    @Then("^user can successfully remove an item$")
    public void successful_remove() throws Throwable {
        int before = itemMan.findAll("testUser").size();
        if (before > 0) {
            user_clicks_button("remove");
            int after = itemMan.findAll("testUser").size();
            assertTrue(before > after);
        }
    }

    private void is_not_shown(String content) throws Throwable {
        Thread.sleep(SleepTime);
        boolean isShown = false;
        for (int i = 0; i < 5; i++) {
            Thread.sleep(SleepTime);
            System.out.println("Unwanted element : " + content);
            //System.out.println("Page source : " + driver.getPageSource().toString());
            if (driver.getPageSource().contains(content)) {
                isShown = true;
                break;
            }
        }
        assertTrue(!isShown);
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
                    if (driver.getPageSource().contains(item.getAuthor().trim())) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                EverythingIsThere = false;
            }

        }
        System.out.println("Everything found.);
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
                System.out.println("COULD NOT FIND LINK");
                System.out.println(e.getStackTrace());
            }
        }
    }

    private void findElementAndFill(String name, String value) {
        int retryCount = 0;
        while (retryCount < 10) {
            try {
                element = driver.findElement(By.name(name));
                if (element != null) {
                    element.sendKeys(value);
                    break;
                }
                Thread.sleep(SleepTime);
            } catch (InterruptedException e) {
                System.out.println("Couldnt find element :" + name);
            }
            retryCount++;
        }
    }
    
    private Book getBook(int index) throws Throwable {
        return itemMan.getBookMan().findAll("default").get(index);
    }
    
    private Blog getBlog(int index) throws Throwable {
        return itemMan.getBlogMan().findAll("default").get(index);
    }
    
    private Video getVideo(int index) throws Throwable {
        return itemMan.getVideoMan().findAll("default").get(index);
    }

    @Then("^List of all \"([^\"]*)\" is shown$")
    public void list_of_all_is_shown(String WhatIsListed) throws Throwable {
        List<ItemType> unread = new ArrayList<>();
        unread.addAll(itemMan.findAll("default"));
        unread.removeIf(u -> u.getIsRead() == 1);
        List<ItemType> read = new ArrayList<>();
        read.addAll(itemMan.findAll("default"));
        read.removeIf(r -> r.getIsRead() == 0);

        if (WhatIsListed.contains("books")) {
            List<ItemType> books = new ArrayList<>();
            books.addAll(itemMan.getBookMan().findAll("default"));
            listOfAllItemsIsShown(books);

            is_not_shown(getBlog(0).getTitle());
        } else if (WhatIsListed.contains("blogs")) {
            List<ItemType> blogs = new ArrayList<>();
            blogs.addAll(itemMan.getBlogMan().findAll("default"));
            listOfAllItemsIsShown(blogs);

            is_not_shown(getBook(0).getTitle());
        } else if (WhatIsListed.contains("videos")) {
            List<ItemType> videos = new ArrayList<>();
            videos.addAll(itemMan.getVideoMan().findAll("default"));
            listOfAllItemsIsShown(videos);

            is_not_shown(getBlog(0).getTitle());
        } else if (WhatIsListed.contains("unread")) {
            listOfAllItemsIsShown(unread);

            is_not_shown(read.get(0).getTitle());
        } else if (WhatIsListed.contains("read")) {
            listOfAllItemsIsShown(read);

            is_not_shown(unread.get(0).getTitle());
        } else {
            List<ItemType> items = new ArrayList<>();
            items.addAll(itemMan.findAll("default"));
            listOfAllItemsIsShown(items);
        }
    }

    @When("^user chooses \"([^\"]*)\" and clicks Show$")
    public void user_chooses_and_clicks_Show(String choise) throws Throwable {
        if (choise.equals("ViewBooks")) {
            driver.findElement(By.id("vBooks")).click();
        }
        if (choise.equals("ViewBlogs")) {
            driver.findElement(By.id("vBlogs")).click();
        }
        if (choise.equals("ViewVideos")) {
            driver.findElement(By.id("vVideos")).click();
        }
        if (choise.equals("ViewRead")) {
            driver.findElement(By.id("vRead")).click();
        }
        if (choise.equals("ViewUnread")) {
            driver.findElement(By.id("vUnread")).click();
        }
        findElementAndFill("tags", "blog, book, video");
        driver.findElement(By.id("Show")).click();
        Thread.sleep(SleepTime);
    }

    @When("^user chooses \"([^\"]*)\" and \"([^\"]*)\" and clicks Show$")
    public void user_chooses_and_and_clicks_Show(String choiseOne, String choiseTwo) throws Throwable {
        if (choiseOne.equals("ViewBooks") || choiseTwo.equals("ViewBooks")) {
            driver.findElement(By.id("vBooks")).click();
        }
        if (choiseOne.equals("ViewBlogs") || choiseTwo.equals("ViewBlogs")) {
            driver.findElement(By.id("vBlogs")).click();
        }
        if (choiseOne.equals("ViewVideos") || choiseTwo.equals("ViewVideos")) {
            driver.findElement(By.id("vVideos")).click();
        }
        findElementAndFill("tags", "blog, book, video");
        driver.findElement(By.id("Show")).click();
        Thread.sleep(SleepTime);
    }

    @Then("^List of all \"([^\"]*)\" and \"([^\"]*)\" is shown$")
    public void list_of_all_and_is_shown(String WhatIsListed, String WhatIsListed2) throws Throwable {
        List<ItemType> items = itemMan.findAll("default");
        if (WhatIsListed.contains("books") || WhatIsListed2.contains("books")) {
            List<ItemType> books = new ArrayList<>();
            books.addAll(itemMan.getBookMan().findAll("default"));
            listOfAllItemsIsShown(books);
            items.removeAll(books);
        }
        if (WhatIsListed.contains("blogs") || WhatIsListed2.contains("blogs")) {
            List<ItemType> blogs = new ArrayList<>();
            blogs.addAll(itemMan.getBlogMan().findAll("default"));
            listOfAllItemsIsShown(blogs);
            items.removeAll(blogs);
        }
        if (WhatIsListed.contains("videos") || WhatIsListed2.contains("videos")) {
            List<ItemType> videos = new ArrayList<>();
            videos.addAll(itemMan.getVideoMan().findAll("default"));
            listOfAllItemsIsShown(videos);
            items.removeAll(videos);
        }
        is_not_shown(items.get(0).getTitle());
    }

    // </editor-fold>
    //                  spacer
    // <editor-fold desc="Book testing">
    @When("^link to book's page is clicked$")
    public void link_to_book_page_is_clicked() throws Throwable {
        Book one = getBook(0);
        Thread.sleep(SleepTime);
        clickLinkWithText(one.getTitle().trim(), "book");
        Thread.sleep(SleepTime);
    }
    
    @When("^link to book's author is clicked$")
    public void link_to_book_author_is_clicked() throws Throwable {
        Book one = getBook(0);
        Thread.sleep(SleepTime);
        clickLinkWithText(one.getAuthor().trim());
        Thread.sleep(SleepTime);
    }
    
    @Then("^book's author's works are shown$")
    public void book_authors_works_are_shown() throws Throwable {
        String author = getBook(0).getAuthor();
        List<ItemType> by_author = itemMan.getItemsByAuthor(author);
        listOfAllItemsIsShown(by_author);
        String other = getBook(1).getAuthor();
        ItemType unwanted = itemMan.getItemsByAuthor(other).get(0);
        is_not_shown(unwanted.getTitle());
    }

    @When("^book fields title \"([^\"]*)\", isbn \"([^\"]*)\", author and year \"([^\"]*)\" are filled and submitted$")
    public void book_fields_are_submitted(String title, String isbn, String year) throws Throwable {
        Thread.sleep(SleepTime);
        if (isbn.isEmpty()) {
            isbn = Integer.toString(Math.abs(random.nextInt()));
        } else if (isbn.equals("Already-in-use")) {
            isbn = getBook(0).getIsbn();
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
        Book one = getBook(0);
        Thread.sleep(SleepTime);
        is_shown(one.getTitle());
        Thread.sleep(SleepTime);
        is_shown(one.getIsbn());
        Thread.sleep(SleepTime);
        is_shown(one.getAuthor());
        Thread.sleep(SleepTime);
        is_shown(Integer.toString(one.getYear()));
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

        driver.findElement(By.name("Add new book")).click();

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

        driver.findElement(By.name("Add new book")).click();

        Thread.sleep(SleepTime);
        //driver.get(baseUrl + "books");
    }

    // </editor-fold>
    //                  spacer
    // <editor-fold desc="video testing">
    @When("^link to video's page is clicked$")
    public void link_to_video_s_page_is_clicked() throws Throwable {
        Video one = getVideo(0);
        Thread.sleep(SleepTime);
        clickLinkWithText(one.getTitle().trim(), "video");
        Thread.sleep(SleepTime);
    }

    @Then("^individual video is shown$")
    public void individual_video_is_shown() throws Throwable {
        Video one = getVideo(0);
        Thread.sleep(SleepTime);
        is_shown(one.getTitle().trim());
        Thread.sleep(SleepTime);
        is_shown(one.getPoster().trim());
    }

    @When("^video fields are filled correctly and submitted$")
    public void video_fields_are_filled_correctly_and_submitted() throws Throwable {
        int n = 100000 + random.nextInt(900000);

        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "https://www.youtube.com/watch?v=WPvGqX-TXP0" + n);

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
    }

    @When("^video fields are filled correctly with short URL and submitted$")
    public void video_fields_are_filled_correctly_with_short_URL_and_submitted() throws Throwable {
        int n = 100000 + random.nextInt(900000);

        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber2");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "https://youtu.be/XKu_SEDAykw" + n);

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
        int n = 100000 + random.nextInt(900000);

        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber3");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "uWzPe_S-RVE" + n);

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
        Thread.sleep(SleepTime);
    }

    @When("^video fields title and poster are filled correctly and submitted\\.$")
    public void video_field_URL_is_missing() throws Throwable {
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
    public void video_field_Title_is_missing() throws Throwable {

        int n = 100000 + random.nextInt(900000);

        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoPoster", "Testaaja");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "WPvGqX-TXP0" + n);

        // Sets user to "testUser"
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userVideo').value='testUser'";
        jse.executeScript(strJS);

        element = driver.findElement(By.name("Add new video"));
        element.submit();
    }

    @When("^video fields URL and Title are filled and submitted$")
    public void video_field_poster_is_missing() throws Throwable {
        int n = 100000 + random.nextInt(900000);

        driver.findElement(By.id("video")).click();
        System.out.println("Attempted to click video radiobutton..");
        Thread.sleep(SleepTime);
        findElementAndFill("videoTitle", "videoCucumber3");
        Thread.sleep(SleepTime);
        findElementAndFill("videoURL", "WPvGqX-TXP0" + n);

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
        System.out.println("Setting user to testuser");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String strJS = "document.getElementById('userBlog').value='testUser'";
        jse.executeScript(strJS);
        System.out.println("User has been set to testUser");

        System.out.println("Finding element for add new blog button");
        driver.findElement(By.name("Add new blog")).click();
        System.out.println("button clicked");
        //driver.get(baseUrl + "books");
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
    //                  spacer
    // <editor-fold desc="sorting testing">
    @Then("^List of items is in \"([^\"]*)\" order$")
    public void list_of_items_is_in_order(String orderedBy) throws Throwable {
        List<ItemType> things = itemMan.findAll("default");
        switch (orderedBy) {
            case "author": {
                things = things.stream()
                        .sorted(Comparator.comparing(ItemType::getAuthor).thenComparing(ItemType::getTitle))
                        .collect(Collectors.toList());
                break;
            }
            case "title": {
                things = things.stream()
                        .sorted(Comparator.comparing(ItemType::getTitle).thenComparing(ItemType::getType))
                        .collect(Collectors.toList());
                break;
            }
            case "rating": {
                things = things.stream()
                        .sorted(Comparator.comparing(ItemType::getRating).reversed().thenComparing(ItemType::getType))
                        .collect(Collectors.toList());
            }
            default: {
                break;
            }

        }
        Boolean doesMatch = true;
        List<WebElement> elements = driver.findElements(By.xpath("//*[@id][@class='items']"));
        for (int i = 0; i < elements.size() - 1; i++) {
            //debugging prints.
            System.out.println("///");
            System.out.println(elements.get(i + 1).getText().trim());
            System.out.println(things.get(i).getTitle().trim());
            System.out.println("///");
            if (!elements.get(i + 1).getText().trim().contains(things.get(i).getTitle().trim())) {
                doesMatch = false;
            }
        }
        assertTrue(doesMatch);
    }

    @When("^Sorting by \"([^\"]*)\" is chosen$")
    public void sorting_by_is_chosen(String option) throws Throwable {
        driver.findElement(By.id(option)).click();
        Thread.sleep(SleepTime);
    }

    @When("^Button \"([^\"]*)\" is clicked$")
    public void button_is_clicked(String buttonName) throws Throwable {
        driver.findElement(By.id(buttonName)).click();
        Thread.sleep(SleepTime);
    }

    // </editor-fold>
    //                  spacer
    //<editor-fold desc="marking item as read/unread">
    @When("^user clicks \"([^\"]*)\" item$")
    public void user_clicks_item(String tag) throws Throwable {
        String title = "NOT FOUND";
        List<ItemType> items = itemMan.findAll("default");
        switch (tag) {
            case "read": {
                itemMan.markAsRead(items.get(0).getId(), "default");
                title = items.get(0).getTitle().trim();
                break;
            }
            //case "unread": {   <-- is default for now, waiting for more options.
            default: {
                itemMan.markAsUnRead(items.get(0).getId(), "default");
                title = items.get(0).getTitle().trim();
                break;
            }
        }
        clickLinkWithText(title, title);
    }

    @When("^user marks item \"([^\"]*)\"$")
    public void user_marks_item(String tag) throws Throwable {
        Thread.sleep(SleepTime);
        switch (tag) {
            case "Mark as unread": {
                driver.findElement(By.id("unread")).click();
                break;
            }
            //case "Mark as read": {   <-- is default for now, waiting for more options.
            default: {
                driver.findElement(By.id("read")).click();
                break;
            }
        }
        Thread.sleep(SleepTime);
    }

    @Then("^item is \"([^\"]*)\"$")
    public void item_is(String tag) throws Throwable {
        List<ItemType> list = itemMan.findAll("default");

        switch (tag) {
            case "unread": {
                assertTrue(list.get(0).getIsRead() == 0);
                break;
            }
            //case "read": {   <-- is default for now, waiting for more options.
            default: {
                assertTrue(list.get(0).getIsRead() == 1);
                break;
            }
        }

    }
    //</editor-fold>

    // <editor-fold desc="tag testing">
    @Given("^user is at book's page$")
    public void user_is_at_book_page() throws Throwable {
        Book book = getBook(0);
        String new_url = baseUrl + "book/" + book.getId();
        System.out.println(new_url);
        driver.get(baseUrl + "book/" + book.getId());
    }

    @Given("^user is at blog's page$")
    public void user_is_at_blog_page() throws Throwable {
        Blog blog = getBlog(0);
        driver.get(baseUrl + "blog/" + blog.getId());
    }

    @Given("^user is at video's page$")
    public void user_is_at_video_page() throws Throwable {
        Video video = getVideo(0);
        driver.get(baseUrl + "video/" + video.getId());
    }

    @When("^tag field is filled with \"([^\"]*)\" and submitted$")
    public void tag_field_filled_and_submitted(String tag) throws Throwable {
        findElementAndFill("tag", tag);
        element = driver.findElement(By.name("Add tag"));
        element.submit();
    }

    @When("^tags field is filled with \"([^\"]*)\" and submitted$")
    public void tags_field_filled_and_submitted(String tags) throws Throwable {
        findElementAndFill("tags", tags);
        element = driver.findElement(By.id("Show"));
        element.click();
    }
    // </editor-fold>
    
    //Comment testing
    @When("^comment field is filled with \"([^\"]*)\" and submitted$")
    public void comment_field_filled_and_submitted(String comment) throws Throwable {
        Thread.sleep(SleepTime);
        element = driver.findElement(By.id("comment"));
        if (comment.isEmpty()) element.clear();
        else element.sendKeys(comment);
        element = driver.findElement(By.name("Add comment"));
        element.submit();
    }

}
