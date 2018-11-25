/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ohtu.types.Blog;

/**
 *
 * @author ColdFish
 */
public class BlogManager implements sqlManager<Blog, Integer> {

    private Database database;

    public BlogManager(Database database) {
        this.database = database;
    }
    
        public boolean add(Blog blog, String user) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call AddBlogAndLink(?,?,?,?)}");

        stmt.setObject(1, blog.getTitle());
        stmt.setObject(2, blog.getURL());
        stmt.setObject(3, blog.getPoster());
        stmt.setObject(4, user);

        int diu = stmt.executeUpdate();

        stmt.close();
        connection.close();

        return diu == 1;
    }

    @Override
    public Blog findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT WebBlog.id, title, URL, Author.name as Author FROM WebBlog, Author WHERE WebBlog.id = ?  AND WebBlog.fk_Writer_id = Author.id");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Blog o = new Blog(rs);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Blog> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT WebBlog.id, title, URL, Author.name as Author FROM WebBlog, Author AND WebBlog.fk_Writer_id = Author.id");
        ResultSet rs = stmt.executeQuery();
        List<Blog> videos = new ArrayList<>();
        while (rs.next()) {
            videos.add(new Blog(rs));
        }

        rs.close();
        stmt.close();
        connection.close();

        return videos;
    }

    public List<Blog> findAll(String user) throws SQLException {
        Connection connection = database.getConnection();
        CallableStatement stmt = connection.prepareCall("{call getBlogsForID(?)}");
        stmt.setObject(1, user);

        ResultSet rs = stmt.executeQuery();
        List<Blog> books = new ArrayList<>();
        while (rs.next()) {
            books.add(new Blog(rs));
        }

        rs.close();
        stmt.close();
        connection.close();

        return books;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
