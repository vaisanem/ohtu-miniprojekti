/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.*;
import java.util.*;


/**
 *
 * @author ColdFish
 */
public interface sqlManager<T,K> {

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;
}
