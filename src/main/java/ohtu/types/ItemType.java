/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.types;

import java.sql.SQLException;
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
    private List<Comment> comments;
    private typeIdentifier type;
    private int isRead;
    private String author;
    private double rating;

    @Override
    public boolean equals(Object t) {
        ItemType other = (ItemType) t;
        return other.id == this.id;
    }

    public enum typeIdentifier {
        book,
        video,
        blog
    }

    public ItemType() {
    }

    public ItemType(int id, String title, String author, String type) {
        this.id = id;
        this.title = title;
        this.author = author;
        switch (type) {
            case "book": {
                this.type = typeIdentifier.book;
                break;
            }
            case "blog": {
                this.type = typeIdentifier.blog;
                break;
            }
            case "video": {
                this.type = typeIdentifier.video;
                break;
            }
            default: {
                System.out.println("Item type unidentifiable");
            }
        }
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public String getAuthor() {
        return author;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
