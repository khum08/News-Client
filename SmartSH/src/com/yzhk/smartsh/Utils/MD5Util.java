package com.yzhk.smartsh.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public static String encode(String str) {
		MessageDigest digester = null;
		try {
			digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		digester.update(str.getBytes());
		
		byte[] digest = digester.digest();
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int x = digest[i] & 0xff;
			String hexString = Integer.toHexString(x);
			
			if(hexString.length()<2){
				hexString = "0" + hexString;
			}
			sb.append(hexString);
		}
		return sb.toString();
	}
}
