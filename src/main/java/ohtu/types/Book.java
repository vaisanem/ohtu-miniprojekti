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
 * @author ColdFish
 */
public class Book {

    private int id;
    private String isbn;
    private String title;
    private String author;
    private int year;

    public Book(int id, String isbn, String title, String author, int year) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Book(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        isbn = rs.getString("ISBN");
        title = rs.getString("Title");
        author = rs.getString("Author");
        year = rs.getInt("releaseYear");
    }

    public int getId() {
        return id;
    }
    
    

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

}
