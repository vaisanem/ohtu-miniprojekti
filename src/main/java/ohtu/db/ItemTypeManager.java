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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        database = new Database();
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

    public Set<String> getSetOfAllTags() throws SQLException { //Sraight-forward SQL-implementaton perhaps better
        HashMap<Integer, List<String>> tagsById = getAllTags();
        Set<String> allTags = new HashSet<>();
        for (int key : tagsById.keySet()) {
            allTags.addAll(tagsById.get(key));
        }
        return allTags;
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
        items.addAll(blogMan.findAll());
        items.addAll(bookMan.findAll());
        items.addAll(videoMan.findAll());

        getAndApplyTags(items);

        return items;
    }

    public List<ItemType> findAll(String user) throws SQLException {
        List<ItemType> items = new ArrayList<>();
        items.addAll(blogMan.findAll(user));
        items.addAll(bookMan.findAll(user));
        items.addAll(videoMan.findAll(user));

        getAndApplyTags(items);

        return items;
    }

    public List<ItemType> getItemsByAuthor(String authorName) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call getItemsForAuthor(?)}");
        stmt.setObject(1, authorName);

        List<ItemType> items = new ArrayList();

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            items.add(new ItemType(rs.getInt("ItemID"), rs.getString("Title"), rs.getString("Author"), rs.getString("ItemIdentifier")));
        }

        rs.close();
        stmt.close();
        connection.close();

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
            if (item.getTags().stream().anyMatch(tag -> tags.contains(tag.toLowerCase()))) {
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

    public boolean addTagToItem(int id, String tag) throws SQLException {
        if (tag.length() > 0) {
            Connection connection = database.getConnection();
            String lowerCaseTag = tag.toLowerCase();
            CallableStatement stmt = connection.prepareCall("{call AddTagToItemID(?, ?)}");
            stmt.setObject(1, id);
            stmt.setObject(2, lowerCaseTag);

            stmt.executeUpdate();

            stmt.close();
            connection.close();
            return true;
        } else {
            return false;
        }
    }

    public void delete(Integer id, String user) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call RemoveUserItemLink(?, ?)}");
        stmt.setObject(1, id);
        stmt.setObject(2, user);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
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
