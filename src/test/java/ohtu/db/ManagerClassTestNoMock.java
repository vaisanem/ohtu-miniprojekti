/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ohtu.types.Blog;
import ohtu.types.Book;
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
}
