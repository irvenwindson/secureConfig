package org.secure.config;

public class Decrypt {

	public static void main(String[] args) {
	    String decryptedString = Core.decodeConfig(args[0]);
	    if (decryptedString != null) {
	        System.out.println("Decoded: " + decryptedString);
	    } else {
	        System.err.println("Decode failed.");
	    }
	}
}
