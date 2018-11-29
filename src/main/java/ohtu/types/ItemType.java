/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.types;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ohtu.db.ItemTypeManager;

/**
 *
 * @author ColdFish
 */
public class ItemType {

    private int id;
    private String title;
    private List<String> tags;
    private typeIdentifier type;
    private int isRead;

    public enum typeIdentifier {
        book,
        video,
        blog
    }

    public ItemType() {
    }

    public List<String> generateTags(ItemTypeManager manager) throws SQLException {
        this.tags = manager.getTags(id);
        return tags;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsRead() {
        return isRead;
    }
    

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> newTags) {
        this.tags = newTags;
    }

    public typeIdentifier getType() {
        return type;
    }

    public void setType(typeIdentifier type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

}
