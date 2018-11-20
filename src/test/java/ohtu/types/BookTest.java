package ohtu.types;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class BookTest {
    
    @Before
    public void setUp() {
        int year = 2016;
        Book book = new Book("isbn", "title", "author", year);
    }
    
}
