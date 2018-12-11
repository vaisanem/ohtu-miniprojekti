/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.db;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ColdFish
 */
public class EncryptionManager {
    
    public String generateSHAPassword(String user, String password) {
        String encryptedUser = getSHA512(user);
        String encryptedPW = getSHA512(password);
        return getSHA512(encryptedUser + encryptedPW);
    }
    
    private String getSHA512(String input) {
        String toReturn = null;
        String salt = getSalt();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(salt.getBytes("utf8"));
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            System.out.println(e.toString());
        }
        
        return toReturn;
    }
    
    private String getSalt() {
        String saltString = "SaltySaltsieses";
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(saltString.getBytes("utf8"));
            toReturn = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            System.out.println(e.toString());
        }
        
        return toReturn;
    }
    
    public boolean stringMatchesEncryption(String username, String password, String encrypted) {
        String encrypt = generateSHAPassword(username, password);
        return encrypt.equals(encrypted.trim());
    }
    
}
