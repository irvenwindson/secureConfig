package org.secure.config;

public class Encrypt {

	public static void main(String[] args) {
	    String encryptedString = Core.encodeConfig(args[0]);
	    if (encryptedString != null) {
	        System.out.println("Encoded: " + encryptedString);
	    } else {
	        System.err.println("Encode failed.");
	        return;
	    }
	}

}
