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

    public Video(String URL, String title, String poster) {
        if (URL.contains("watch?v=")) {
            this.URL = URL.substring(URL.indexOf('=') + 1);
        } else if (URL.contains("youtu.be")) {
            this.URL = URL.substring(URL.indexOf('/') + 1);
        } else {
            URL = URL;
        }
        this.poster = poster;
        this.title = title;

    }

    public Video(int id, String URL, String title, String poster) {
        if (URL.contains("=")) {
            this.URL = URL.substring(URL.indexOf('=') + 1);
        } else if (URL.contains("youtu.be")) {
            this.URL = URL.substring(URL.indexOf('.') + 4);
        } else {
            URL = URL;
        }
        this.poster = poster;
        this.title = title;
        this.id = id;

    }

    public Video(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        URL = rs.getString("URL");
        title = rs.getString("Title");
        poster = rs.getString("Poster");

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
}
