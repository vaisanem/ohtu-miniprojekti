/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ohtu.types.*;

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
        String url = "jdbc:sqlserver://" + addr + ":34200;databaseName=OhtuMPv2;user=ohtuadm;password=hakimi1337";

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

    public List<String> getTags(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("SELECT Tag.Description as Tag FROM Tag_ItemEntry INNER JOIN Tag ON Tag.id = Tag_ItemEntry.fk_Tag_id WHERE Tag_ItemEntry.fk_ItemEntry_id = ?");
        stmt.setObject(1, key);

        List<String> tags = new ArrayList();

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            tags.add(rs.getString("Tag"));
        }

        rs.close();
        stmt.close();
        connection.close();

        return tags;
    }

    public HashMap<Integer, List<String>> getAllTags() throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("SELECT Tag_ItemEntry.fk_ItemEntry_id as id, Tag.Description as Tag FROM Tag_ItemEntry INNER JOIN Tag ON Tag.id = Tag_ItemEntry.fk_Tag_id");

        HashMap<Integer, List<String>> tags = new HashMap<>();

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String tag = rs.getString("Tag");
            if (tags.containsKey(id)) {
                tags.get(id).add(tag);
            } else {
                tags.put(id, new ArrayList<>());
                tags.get(id).add(tag);
            }
        }

        rs.close();
        stmt.close();
        connection.close();

        return tags;
    }

    public ItemType findOne(Integer key, ItemType.typeIdentifier type) throws SQLException {
        switch (type) {

            case blog: {
                Blog blog = blogMan.findOne(key);
                blog.generateTags(this);
                return blog;
            }

            case book: {
                Book book = bookMan.findOne(key);
                book.generateTags(this);
                return book;
            }

            case video: {
                Video video = videoMan.findOne(key);
                video.generateTags(this);
                return video;
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

        getAndApplyTags(items);

        return items;
    }

    public List<ItemType> findAll(String user) throws SQLException {
        List<ItemType> items = new ArrayList<>();

        items.addAll(bookMan.findAll(user));
        items.addAll(videoMan.findAll(user));
        items.addAll(blogMan.findAll(user));

        getAndApplyTags(items);

        return items;
    }

    public void applyTags(HashMap<Integer, List<String>> tags, List<ItemType> items) {
        items.forEach(item -> {
            if (tags.containsKey(item.getId())) {
                item.setTags(tags.get(item.getId()));
            }
        });
    }

    public void getAndApplyTags(List<ItemType> items) throws SQLException {
        HashMap<Integer, List<String>> tags = getAllTags();
        applyTags(tags, items);
    }

    public List<ItemType> filterByTags(List<ItemType> items, List<String> tags) {
        List<ItemType> filtered = new ArrayList<>();
        items.forEach(item -> {
            if (item.getTags().stream().anyMatch(tag -> tags.contains(tag))) {
                filtered.add(item);
            }
        });

        return filtered;
    }

    public void closeConnection() throws SQLException {
        database.getConnection().close();
    }

    public void markAsRead(int id, String user) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call MarkItemAsReadForUser(?, ?)}");
        stmt.setObject(1, id);
        stmt.setObject(2, user);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public void markAsUnRead(int id, String user) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call MarkItemAsUnReadForUser(?, ?)}");
        stmt.setObject(1, id);
        stmt.setObject(2, user);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public void addTagToItem(int id, String tag) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call AddTagToItem(?, ?)}");
        stmt.setObject(1, id);
        stmt.setObject(2, tag);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
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
