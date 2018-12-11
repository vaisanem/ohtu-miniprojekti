/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.*;

/**
 *
 * @author ColdFish
 */
public class Database {

    private String databaseAddress;

    public Database() {
        String addr = "ohmipro.ddns.net";
        String url = "jdbc:sqlserver://" + addr + ":34200;databaseName=OhtuMPv2;user=ohtuadm;password=hakimi1337";
        this.databaseAddress = url;
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

}
