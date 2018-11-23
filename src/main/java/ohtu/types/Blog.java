/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.types;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Toothy
 */
public class Blog {

    private int id;
    private String title;
    private String author;
    private String URL;

    public Blog(String URL, String title, String author) {
        this.title = title;
        this.author = author;
        this.URL = URL;
    }

    public Blog(int id, String URL, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.URL = URL;
    }

    public Blog(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        URL = rs.getString("URL");
        title = rs.getString("Title");
        author = rs.getString("Author");
    }

    public int getId() {
        return id;
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

    public String getPoster() {
        return author;
    }

    public void setPoster(String poster) {
        this.author = poster;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

}
