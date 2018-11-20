/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ohtu.types.Book;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

/**
 *
 * @author ColdFish
 */
public class BookManager implements sqlManager<Book, Integer> {

    private Database database;

    public BookManager(Database database) {
        this.database = database;
    }

    @Override
    public Book findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT book.id, isbn as ISBN, title as Title, relYear as releaseYear, name as Author FROM Book, Author WHERE Book.id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Book o = new Book(rs);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT book.id, isbn as ISBN, title as Title, relYear as releaseYear, name as Author FROM Book, Author");

        ResultSet rs = stmt.executeQuery();
        List<Book> books = new ArrayList<>();
        while (rs.next()) {
            books.add(new Book(rs));
        }

        rs.close();
        stmt.close();
        connection.close();

        return books;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("DELETE FROM Book WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        con.close();
    }

}