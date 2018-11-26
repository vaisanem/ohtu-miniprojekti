/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ohtu.types.ItemType;

/**
 *
 * @author ColdFish
 */
public class ItemTypeManager {

    private Database database;
    private VideoManager videoMan;
    private BookManager bookMan;
    private BlogManager blogMan;

    public ItemTypeManager() throws ClassNotFoundException {
        this.database = database;
        String addr = "ohmipro.ddns.net";
        String url = "jdbc:sqlserver://" + addr + ":34200;databaseName=OhtuMP;user=ohtuadm;password=hakimi1337";

        database = new Database(url);
        bookMan = new BookManager(database);
        videoMan = new VideoManager(database);
        blogMan = new BlogManager(database);
    }

    public BlogManager getBlogMan() {
        return blogMan;
    }

    public BookManager getBookMan() {
        return bookMan;
    }

    public VideoManager getVideoMan() {
        return videoMan;
    }

    public ItemType findOne(Integer key, ItemType.typeIdentifier type) throws SQLException {
        switch (type) {

            case blog: {
                return blogMan.findOne(key);
            }

            case book: {
                return bookMan.findOne(key);
            }

            case video: {
                return videoMan.findOne(key);
            }

            default:
                return null;
        }
    }

    public List<ItemType> findAll() throws SQLException {
        List<ItemType> items = new ArrayList<>();

        items.addAll(bookMan.findAll());
        items.addAll(videoMan.findAll());
        items.addAll(blogMan.findAll());

        return items;
    }

    public List<ItemType> findAll(String user) throws SQLException {
        List<ItemType> items = new ArrayList<>();

        items.addAll(bookMan.findAll(user));
        items.addAll(videoMan.findAll(user));
        items.addAll(blogMan.findAll(user));

        return items;
    }

    public void closeConnection() throws SQLException {
        database.getConnection().close();
    }

    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // <editor-fold desc="For JUnit testing">
    public void setVideoMan(VideoManager videoMan) {
        this.videoMan = videoMan;
    }

    public void setBookMan(BookManager bookMan) {
        this.bookMan = bookMan;
    }

    public void setBlogMan(BlogManager blogMan) {
        this.blogMan = blogMan;
    }
    // </editor-fold>
    

}
