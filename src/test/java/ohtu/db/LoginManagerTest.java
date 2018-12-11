/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.sql.SQLException;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ColdFish
 */
public class LoginManagerTest {

    private LoginManager loginMan;

    @Before
    public void setUp() throws ClassNotFoundException {
        this.loginMan = new LoginManager();
    }

    @Test
    public void loggingWorksForExistingUser() throws SQLException {
        assertTrue(loginMan.login("hakimi", "passwordgeneration"));
    }
    
    /*
    Tests for creating user not implemented yet, because it would again fill the database with crud, which we don't want. Yet. First a way to remove users after generating them..
    */
}
