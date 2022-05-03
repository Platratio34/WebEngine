package security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	
	public static String Hash1(String input) {
		try {
			MessageDigest msgDst = MessageDigest.getInstance("SHA-256");
			byte[] msgArr = msgDst.digest(input.getBytes());
			BigInteger bi = new BigInteger(1, msgArr);
			String hstxt = bi.toString(16);
			while(hstxt.length() < 32) {
				hstxt = "0" + hstxt;
			}
			return hstxt;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
}
