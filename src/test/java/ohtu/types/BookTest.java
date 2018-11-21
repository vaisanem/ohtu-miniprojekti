package ohtu.types;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class BookTest {
    
    private Book book;
    
    @Before
    public void setUp() {
        int year = 2016;
        book = new Book("isbn", "title", "author", year);
    }
    
    @Test
    public void constructorCreatesABook(){
        assertEquals("isbn", book.getIsbn());
        assertEquals("title", book.getTitle());
        assertEquals("author", book.getAuthor());
        assertEquals(2016, book.getYear());
    }
    
}
