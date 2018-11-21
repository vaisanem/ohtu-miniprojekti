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
    
        public boolean add(Book book, String user) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call AddBookAndLink(?,?,?,?,?)}");

        stmt.setObject(1, book.getTitle());
        stmt.setObject(2, book.getIsbn());
        stmt.setObject(3, book.getAuthor());
        stmt.setObject(4, book.getYear());
        stmt.setObject(5, user);

        int diu = stmt.executeUpdate();

        stmt.close();
        connection.close();

        return diu == 1;
    }

    @Override
    public Book findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT book.id, isbn as ISBN, title as Title, relYear as releaseYear, name as Author FROM Book, Author WHERE Book.id = ?  AND Book.fk_AuthorID = Author.id");
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
        PreparedStatement stmt = connection.prepareStatement("SELECT book.id, isbn as ISBN, title as Title, relYear as releaseYear, name as Author FROM Book, Author WHERE Author.id = Book.fk_AuthorID");
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
    
        public List<Book> findAll(String user) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call getBooksForId(?)}");
        stmt.setObject(1, user);
        
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
