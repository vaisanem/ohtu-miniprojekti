/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.types;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ColdFish
 */
public class Book extends ItemType implements Serializable {

    private String isbn;
    private String author;
    private int year;

    public Book(int id, String isbn, String title, String author, int year) {
        this(isbn, title, author, year);
        super.setId(id);
    }

    public Book(String isbn, String title, String author, int year) {
        super.setType(typeIdentifier.book);
        super.setTitle(title);
        this.isbn = isbn;
        this.author = author;
        this.year = year;
    }

    public Book(ResultSet rs) throws SQLException {
        super.setType(typeIdentifier.book);
        super.setId(rs.getInt("id"));
        super.setTitle(rs.getString("title"));
        super.setIsRead(rs.getInt("hasRead"));
        isbn = rs.getString("ISBN");
        author = rs.getString("Author");
        year = rs.getInt("releaseYear");
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
    
    public static boolean checkNumericality(String year) {
        return year.matches("[0-9]+");
    }

}
