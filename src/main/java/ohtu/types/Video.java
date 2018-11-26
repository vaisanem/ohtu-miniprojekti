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
public class Video extends ItemType {

    private String URL;
    private String poster;

    public Video(String URL, String title, String poster) {
        super.setType(typeIdentifier.video);
        if (URL.contains("watch?v=")) {
            this.URL = URL.substring(URL.indexOf('=') + 1);
        } else if (URL.contains("youtu.be")) {
            this.URL = URL.substring(URL.indexOf('.') + 4);
        } else {
            this.URL = URL;
        }
        this.poster = poster;
        super.setTitle(title);

    }

    public Video(int id, String URL, String title, String poster) {
        super.setType(typeIdentifier.video);
        if (URL.contains("=")) {
            this.URL = URL.substring(URL.indexOf('=') + 1);
        } else if (URL.contains("youtu.be")) {
            this.URL = URL.substring(URL.indexOf('.') + 4);
        } else {
            this.URL = URL;
        }
        this.poster = poster;
        super.setId(id);
        super.setTitle(title);

    }

    public Video(String URL) {
        super.setType(typeIdentifier.video);
        if (URL.contains("=")) {
            this.URL = URL.substring(URL.indexOf('=') + 1);
        } else if (URL.contains("youtu.be")) {
            this.URL = URL.substring(URL.indexOf('.') + 4);
        } else {
            this.URL = URL;
        }
        // TODO Fetch information from YouTubeAPI.
    }
// Waiting for implementation
//    public Video(ResultSet rs) throws SQLException {
//        super.setType(typeIdentifier.video);
//        super.setId(rs.getInt("id"));
//        super.setTitle(rs.getString("title"));
//        URL = rs.getString("URL");
//        poster = rs.getString("Author");
//
//    }

    public String getURL() {
        return URL;
    }

    public String getPoster() {
        return poster;
    }

}
