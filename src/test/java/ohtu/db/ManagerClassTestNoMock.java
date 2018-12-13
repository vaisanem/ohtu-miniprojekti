/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ohtu.types.Blog;
import ohtu.types.Book;
import ohtu.types.Comment;
import ohtu.types.ItemType;
import ohtu.types.Video;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ColdFish
 */
public class ManagerClassTestNoMock {

    private ItemTypeManager itemMan;
    private List<ItemType> items;

    @Before
    public void setUp() throws ClassNotFoundException {
        itemMan = new ItemTypeManager();
        try {
            items = itemMan.findAll();
        } catch (SQLException ex) {
            System.out.println("sql error :" + ex.getMessage());
        }
    }

    @Test
    public void bookFindOneFindsOne() throws SQLException {
        List<ItemType> data = itemMan.findAll();
        int id = data.stream().filter(item -> item.getType() == ItemType.typeIdentifier.book).findAny().get().getId();
        Book book = itemMan.getBookMan().findOne(id);
        assertTrue(book != null);
    }

    @Test
    public void videoFindOneFindsOne() throws SQLException {
        List<ItemType> data = itemMan.findAll();
        int id = data.stream().filter(item -> item.getType() == ItemType.typeIdentifier.video).findAny().get().getId();
        Video vid = itemMan.getVideoMan().findOne(id);
        assertTrue(vid != null);
    }

    @Test
    public void blogFindOneFindsOne() throws SQLException {
        List<ItemType> data = itemMan.findAll();
        int id = data.stream().filter(item -> item.getType() == ItemType.typeIdentifier.blog).findAny().get().getId();
        Blog blog = itemMan.getBlogMan().findOne(id);
        assertTrue(blog != null);
    }

    @Test
    public void bookFindOneIncorrectInput() throws SQLException {
        int id = -1;
        Book book = itemMan.getBookMan().findOne(id);
        assertTrue(book == null);
    }

    @Test
    public void videoFindOneIncorrectInput() throws SQLException {
        int id = -1;
        Video vid = itemMan.getVideoMan().findOne(id);
        assertTrue(vid == null);
    }

    @Test
    public void blogFindOneIncorrectInput() throws SQLException {
        int id = -1;
        Blog blog = itemMan.getBlogMan().findOne(id);
        assertTrue(blog == null);
    }

    @Test
    public void bookfindAllFindsAll() throws SQLException {
        List<Book> allBooks = itemMan.getBookMan().findAll();
        assertTrue(items.containsAll(allBooks));
    }

    @Test
    public void blogfindAllFindsAll() throws SQLException {
        List<Blog> allBlogs = itemMan.getBlogMan().findAll();
        assertTrue(items.containsAll(allBlogs));
    }

    @Test
    public void videofindAllFindsAll() throws SQLException {
        List<Video> allVideos = itemMan.getVideoMan().findAll();
        assertTrue(items.containsAll(allVideos));
    }

    @Test
    public void filtersWork() throws SQLException {
        List<ItemType> all = (ArrayList) itemMan.findAll();
        ArrayList<String> filters = new ArrayList();

        //Filter nothing
        filters.add("book");
        filters.add("video");
        filters.add("blog");
        List<ItemType> filterTest = itemMan.filterByTags(all, filters);
        assertTrue(filterTest.containsAll(all));

        //Filter books
        filters = new ArrayList();
        filters.add("book");
        filterTest = itemMan.filterByTags(all, filters);
        assertTrue(filterTest.stream().noneMatch(item -> item.getType() == ItemType.typeIdentifier.blog || item.getType() == ItemType.typeIdentifier.video));

        //Filter blogs
        filters = new ArrayList();
        filters.add("blog");
        filterTest = itemMan.filterByTags(all, filters);
        assertTrue(filterTest.stream().noneMatch(item -> item.getType() == ItemType.typeIdentifier.book || item.getType() == ItemType.typeIdentifier.video));

        //Filter videos
        filters = new ArrayList();
        filters.add("video");
        filterTest = itemMan.filterByTags(all, filters);
        assertTrue(filterTest.stream().noneMatch(item -> item.getType() == ItemType.typeIdentifier.blog || item.getType() == ItemType.typeIdentifier.book));

        //Filter videos and books
        filters = new ArrayList();
        filters.add("video");
        filters.add("book");
        filterTest = itemMan.filterByTags(all, filters);
        assertTrue(filterTest.stream().noneMatch(item -> item.getType() == ItemType.typeIdentifier.blog));
    }

    @Test
    public void canAddTag() throws SQLException {
        assertTrue(itemMan.addTagToItem(2, "book"));
    }

    @Test
    public void cannotAddEmptyTag() throws SQLException {
        assertFalse(itemMan.addTagToItem(1, ""));
    }

    @Test
    public void canMarkAsRead() throws SQLException {
        List<ItemType> data = itemMan.findAll("testUser");
        ItemType oitem = data.stream().findAny().get();
        switch (oitem.getIsRead()) {
            case 0: {
                itemMan.markAsRead(oitem.getId(), "testUser");
                break;
            }

            case 1: {
                // If element is already read, mark it as unread, and then as read.
                itemMan.markAsUnRead(oitem.getId(), "testUser");
                itemMan.markAsRead(oitem.getId(), "testUser");
                break;
            }

            default: {
                System.out.println("Something went wrong, item : " + oitem.getTitle() + " isread property value is : " + oitem.getIsRead());
                break;
            }
        }
        // Finally, get the same Item from the DB and check that it is now read.
        data = itemMan.findAll("testUser");
        ItemType sameItem = data.stream().filter(item -> item.getId() == oitem.getId()).findFirst().get();
        assertTrue(sameItem.getIsRead() == 1);
    }

    @Test
    public void canMarkAsUnread() throws SQLException {
        List<ItemType> data = itemMan.findAll("testUser");
        ItemType oitem = data.stream().findAny().get();
        switch (oitem.getIsRead()) {
            case 0: {
                // If element is already unread, mark it as read, and then as unread.
                itemMan.markAsRead(oitem.getId(), "testUser");
                itemMan.markAsUnRead(oitem.getId(), "testUser");
                break;
            }

            case 1: {
                itemMan.markAsUnRead(oitem.getId(), "testUser");
                break;
            }

            default: {
                System.out.println("Something went wrong, item : " + oitem.getTitle() + " isread property value is : " + oitem.getIsRead());
                break;
            }

        }

        // Finally, get the same Item from the DB and check that it is now unread.
        data = itemMan.findAll("testUser");
        ItemType sameItem = data.stream().filter(item -> item.getId() == oitem.getId()).findFirst().get();
        assertTrue(sameItem.getIsRead() == 0);
    }

    @Test
    public void commentsAreAppliedCorrectly() throws SQLException {
        List<ItemType> items = itemMan.findAll("testUser");
        itemMan.getAndApplyComments(items);
        assertTrue(items.stream().anyMatch(item -> item.getComments() != null && item.getComments().size() >= 2));
    }
}
