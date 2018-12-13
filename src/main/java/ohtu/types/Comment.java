/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.types;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ColdFish
 */
public class Comment implements Comparable{

    private String text;
    private String timeStamp;
    private String userName;
    private int itemID;

    public Comment(ResultSet rs) throws SQLException {
        this.text = rs.getString("TextContent");
        this.timeStamp = rs.getString("TimeStamp");
        this.userName = rs.getString("UserName");
        this.itemID = rs.getInt("ID");
    }

    public int getItemID() {
        return itemID;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int compareTo(Object t) {
        Comment other = (Comment) t;
        return other.itemID - this.itemID;
    }


}
