package ohtu;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import ohtu.db.sqlManager;
import ohtu.stubs.StubBookManager;
import ohtu.types.Book;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Stepdefs {

    WebDriver driver;
    String baseUrl;
    WebElement element;

    public Stepdefs() {
        File file;
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
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Given("^user is at the main page$")
    public void user_is_at_the_main_page() throws Throwable {
        driver.get(baseUrl);
        Thread.sleep(1000);
    }

    @When("^link \"([^\"]*)\" is clicked$")
    public void a_link_is_clicked(String link) throws Throwable {
        Thread.sleep(1000);
        clickLinkWithText(link);
        Thread.sleep(1000);
    }
    
    @When("^book fields title \"([^\"]*)\" and others are filled and submitted$")
    public void book_fields_are_submitted(String title) throws Throwable {
        Thread.sleep(1000);
        findElementAndFill("title", title);
        findElementAndFill("isbn", "98765432100");
        findElementAndFill("author", "Cucumber Testaaja");
        findElementAndFill("year", "2011");
        element = driver.findElement(By.name("submit"));
        element.submit();
        Thread.sleep(1000);
        //driver.get(baseUrl + "books");
    }


    @Then("^\"([^\"]*)\" is shown$")
    public void is_shown(String content) throws Throwable {
        assertTrue(driver.getPageSource().contains(content));
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
