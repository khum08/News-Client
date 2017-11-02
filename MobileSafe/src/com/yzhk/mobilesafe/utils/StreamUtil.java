package com.yzhk.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
	
	public static String getStringFromStream(InputStream in) throws IOException{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[1024];
		int len;
		while((len = in.read(buffer))!=-1){
			out.write(buffer, 0, len);
		}
		String str = out.toString();
		out.close();
		in.close();
		System.out.println(str);
		return str;
	}

}
