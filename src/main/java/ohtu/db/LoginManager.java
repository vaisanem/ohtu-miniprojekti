/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ColdFish
 */
public class LoginManager {

    private final Database database;
    private final EncryptionManager encMan;

    public LoginManager() throws ClassNotFoundException {
        encMan = new EncryptionManager();
        database = new Database();
    }

    public void createUser(String user, String password) {
        String encrypted = encMan.generateSHAPassword(user, password);
        try {
            Connection connection = database.getConnection();
            CallableStatement stmt = connection.prepareCall("INSERT INTO EndUser (name, password) VALUES (?, ?)");
            stmt.setObject(1, user);
            stmt.setObject(2, encrypted);

            stmt.executeUpdate();

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }

    public boolean login(String user, String password) {
        String encryptedPW = "";
        try {
            Connection connection = database.getConnection();
            CallableStatement stmt = connection.prepareCall("SELECT password FROM EndUser WHERE EndUser.name = ?");
            stmt.setObject(1, user);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                encryptedPW = rs.getString("password");
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return encMan.stringMatchesEncryption(user, password, encryptedPW);
    }
}
