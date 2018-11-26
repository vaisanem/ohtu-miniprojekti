package ohtu.db;

import java.sql.SQLException;
import java.util.ArrayList;
import ohtu.types.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;

public class ItemTypeManagerTest {
    
    private ItemTypeManager itemMan;
    private VideoManager videoMan;
    private BookManager bookMan;
    private BlogManager blogMan;
    private ArrayList<Video> videos;
    private ArrayList<Book> books;
    private ArrayList<Blog> blogs;
    private Video video;
    private Book book;
    private Blog blog;
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        itemMan = new ItemTypeManager();
        videoMan = mock(VideoManager.class);
        bookMan = mock(BookManager.class);
        blogMan = mock(BlogManager.class);
        
        videos = new ArrayList<>();
        video = new Video(1, "youtube.com/path/to/nowhere", "Cat video", "Catlover");
        videos.add(video);
        when(videoMan.findAll()).thenReturn(videos);
        when(videoMan.findAll("")).thenReturn(videos);
        when(videoMan.findOne(1)).thenReturn(video);
        
        books = new ArrayList<>();
        book = new Book(1, "1234567891011", "Mockito", "Testaaja", 2018);
        books.add(book);
        when(bookMan.findAll()).thenReturn(books);
        when(bookMan.findAll("")).thenReturn(books);
        when(bookMan.findOne(1)).thenReturn(book);
        
        blogs = new ArrayList<>();
        blog = new Blog(1, "bloggers.com/path/to/nowhere", "Cats", "Catlover");
        blogs.add(blog);
        when(blogMan.findAll()).thenReturn(blogs);
        when(blogMan.findAll("")).thenReturn(blogs);
        when(blogMan.findOne(1)).thenReturn(blog);
        
        itemMan.setBlogMan(blogMan);
        itemMan.setBookMan(bookMan);
        itemMan.setVideoMan(videoMan);
    }
    
    @Test
    public void findOneTypeBookReturnsBook() throws SQLException {
        Book book2 = (Book) itemMan.findOne(1, ItemType.typeIdentifier.book);
        assertEquals(book.getId(), book2.getId());
        assertEquals(book.getTitle(), book2.getTitle());
        assertEquals(book.getIsbn(), book2.getIsbn());
    }
    
    @Test
    public void findOneTypeBlogReturnsBlog() throws SQLException {
        Blog blog2 = (Blog) itemMan.findOne(1, ItemType.typeIdentifier.blog);
        assertEquals(blog.getId(), blog2.getId());
        assertEquals(blog.getTitle(), blog2.getTitle());
        assertEquals(blog.getURL(), blog2.getURL());
    }
    
    @Test
    public void findOneTypeVideoReturnsVideo() throws SQLException {
        Video video2 = (Video) itemMan.findOne(1, ItemType.typeIdentifier.video);
        assertEquals(video.getId(), video2.getId());
        assertEquals(video.getTitle(), video2.getTitle());
        assertEquals(video.getURL(), video2.getURL());
    }
    
    @Test
    public void findAllFindsAll() throws SQLException {
        ArrayList<ItemType> all = (ArrayList) itemMan.findAll();
        assertTrue(all.containsAll(videos));
        assertTrue(all.containsAll(books));
        assertTrue(all.containsAll(blogs));
    }
    @Test
    public void findAllWithSpecifiedUserFindsAll() throws SQLException {
        ArrayList<ItemType> all = (ArrayList) itemMan.findAll("");
        assertTrue(all.containsAll(videos));
        assertTrue(all.containsAll(books));
        assertTrue(all.containsAll(blogs));
    }
    
}
