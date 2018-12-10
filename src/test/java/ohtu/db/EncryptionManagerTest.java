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
public class EncryptionManagerTest {

    private EncryptionManager encMan;

    @Before
    public void setUp() throws ClassNotFoundException {
        this.encMan = new EncryptionManager();
    }

    @Test
    public void passwordEncryptionIsReproductible() throws SQLException {
        String checkSum = "190d125447c5c0b440f2aa5eeb79aeebc8d34125e44370b74376a8683196867aef0e509e8e182bc780cb2c9af827b76c54f18afd91c259c0a0bd3c5c8669ba95";
        assertTrue(encMan.stringMatchesEncryption("testingGeneration", "supersecurepasswords", checkSum));
    }

}
