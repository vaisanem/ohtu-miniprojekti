/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ohtu.types.Book;

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

    /**
     * Edit method requires input parameters of either new values, or THE OLD
     * VALUES entered if values not changed! Input of empty field will edit the
     * field on SQL to empty!!
     *
     * @param id
     * @param newTitle
     * @param newISBN
     * @param newYear
     * @param newAuthor
     * @throws SQLException
     */
    public void edit(int id, String newTitle, String newISBN, String newAuthor, int newYear) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call editBookWithID(?, ?, ?, ?, ?)}");
        stmt.setObject(1, id);
        stmt.setObject(2, newTitle);
        stmt.setObject(3, newISBN);
        stmt.setObject(4, newAuthor);
        stmt.setObject(5, newYear);

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    @Override
    public Book findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call getBookWithID(?)}");
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

    public Book findOne(Integer key, String user) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call getBookWithIDandUser(?, ?)}");
        stmt.setObject(1, key);
        stmt.setObject(2, user);

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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BooksView");
        ResultSet rs = stmt.executeQuery();
        List<Book> books = new ArrayList<>();
        while (rs.next()) {
            books.add(new Book(rs.getInt("id"), rs.getString("ISBN"), rs.getString("Title"), rs.getString("Author"), rs.getInt("releaseYear")));
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
}
