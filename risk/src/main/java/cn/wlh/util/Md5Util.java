package cn.wlh.util;

import java.security.MessageDigest;

/**
 * 	MD5加密工具类
 */
public class Md5Util {

	public static String md5(String source) {
	    StringBuffer sb = new StringBuffer(32);
	    try {  
	        MessageDigest md    = MessageDigest.getInstance("MD5");  
	        byte[] array        = md.digest(source.getBytes("utf-8"));
	        for (int i = 0; i < array.length; i++) {  
	            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));  
	        }  
	    } catch (Exception e) {  
	        return null;  
	    }
	    return sb.toString();  
	}


	public static void main(String[] args){
		System.out.print(md5("123456"));
	}

}
