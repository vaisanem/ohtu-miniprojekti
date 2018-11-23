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
public class Video {
    private String URL;
    private String poster;
    private String title;
    private int id;
    private String EmbeddedURL;

    public Video(String URL, String title, String poster) {
        this.URL = URL;
        this.poster = poster;
        this.title = title;       
        this.EmbeddedURL = "https://www.youtube.com/embed/" + URL.substring(URL.indexOf('=')+1);
    }

    public Video(int id, String URL, String title, String poster) {
        this.URL = URL;
        this.poster = poster;
        this.title = title;
        this.id = id;
        this.EmbeddedURL = "https://www.youtube.com/embed/" + URL.substring(URL.indexOf('=')+1);
    }
    
        public Video(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        URL = rs.getString("URL");
        title = rs.getString("Title");
        poster = rs.getString("Poster");  
        EmbeddedURL = rs.getString("EmbeddedURL");
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getEmbeddedURL() {
        return EmbeddedURL;
    }

    public void setEmbeddedURL(String EmbeddedURL) {
        this.EmbeddedURL = EmbeddedURL;
    }
    
          
}
