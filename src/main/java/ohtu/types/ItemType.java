/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.types;

/**
 *
 * @author ColdFish
 */
public class ItemType {

    private int id;
    private String title;
    private typeIdentifier type;

    public enum typeIdentifier {
        book,
        video,
        blog
    }

    public ItemType() {
    }
    
    

    public ItemType(int id, String title) {
        this.id = id;
        this.title = title;
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
