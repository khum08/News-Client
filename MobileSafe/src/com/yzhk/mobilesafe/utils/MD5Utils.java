package com.yzhk.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
	public static String encode(String target){
		
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		digest.update(target.getBytes());
		byte[] bs = digest.digest();
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bs.length; i++) {
			int x = bs[i] & 0xff;
			String hexString = Integer.toHexString(x);
			
			if(hexString.length()<2){
				hexString = "0" + hexString;
			}
			sb.append(hexString);
		}
		
		return sb.toString();
		
	}

}
