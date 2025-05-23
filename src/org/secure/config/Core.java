package org.secure.config;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


public class Core {

	  private static final int KEY_LENGTH = 256;
	  private static final int ITERATION_COUNT = 65536;

	  public static String encrypt(String strToEncrypt, String secretKey, String salt) {

	    try {

	        SecureRandom secureRandom = new SecureRandom();
	        byte[] iv = new byte[16];
	        secureRandom.nextBytes(iv);
	        IvParameterSpec ivspec = new IvParameterSpec(iv);

	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

	        byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
	        byte[] encryptedData = new byte[iv.length + cipherText.length];
	        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
	        System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

	        return Base64.getEncoder().encodeToString(encryptedData);
	    } catch (Exception e) {
	        // Handle the exception properly
	        e.printStackTrace();
	        return null;
	    }
	  }

	
	  public static String decrypt(String strToDecrypt, String secretKey, String salt) {

		    try {

		        byte[] encryptedData = Base64.getDecoder().decode(strToDecrypt);
		        byte[] iv = new byte[16];
		        System.arraycopy(encryptedData, 0, iv, 0, iv.length);
		        IvParameterSpec ivspec = new IvParameterSpec(iv);

		        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
		        SecretKey tmp = factory.generateSecret(spec);
		        SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

		        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);

		        byte[] cipherText = new byte[encryptedData.length - 16];
		        System.arraycopy(encryptedData, 16, cipherText, 0, cipherText.length);

		        byte[] decryptedText = cipher.doFinal(cipherText);
		        return new String(decryptedText, "UTF-8");
		    } catch (Exception e) {
		        // Handle the exception properly
		        e.printStackTrace();
		        return null;
		    }
		  }
	  
	  public static String sha256(String strInput) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			  byte[] hash = digest.digest(strInput.getBytes(StandardCharsets.UTF_8));
			    StringBuilder hexString = new StringBuilder(2 * hash.length);
			    for (int i = 0; i < hash.length; i++) {
			        String hex = Integer.toHexString(0xff & hash[i]);
			        if(hex.length() == 1) {
			            hexString.append('0');
			        }
			        hexString.append(hex);
			    }
			    return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	  }
	  
	  public static String encodeConfig(String StrInput) {
		    String salt = Core.sha256("");

		    return encrypt(StrInput, salt, salt);
	  }
	  
	  public static String decodeConfig(String strInput) {
		    String salt = Core.sha256("");

		    return decrypt(strInput, salt, salt);
	  }
	
}
 