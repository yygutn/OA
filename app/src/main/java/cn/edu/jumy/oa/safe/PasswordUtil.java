package cn.edu.jumy.oa.safe;

import javax.crypto.KeyGeneratorSpi;


import java.io.File;

/**
 * PasswordUtil.java
 * 密码加密、解密操作
 *
 * @author zlh
 */
public class PasswordUtil {

	
	private static SimpleCipherDES des = new SimpleCipherDES("cskc_cisencdis-fesafclcsa");;
	
    /**
     * 解密操作
     * 
     * @param  pwd 待解密的字符串
     * @return 解密字符串
     */
	public static String simpleDecrypt(String pwd) {

		return new String(des.simpleStringDecrypt(pwd));
		
	}
	
    /**
     * 加密操作
     * 
     * @param  pwd 待加密的字符串
     * @return 加密结果字符串
     */
	public static String simpleEncpyt(String pwd) {

		return new String(des.simpleStringEncrypt(pwd.getBytes()));
		
	}
	
	
    /**
     * 测试
     * 
     */
	public static void test(String[] args) {
		String acc_pwd="admin_11111";
		//加密
		String encpytStr=simpleEncpyt(acc_pwd);
		System.out.println(acc_pwd+"加密过后的字符串是："+encpytStr);
		
		//解密
		String decpytStr=simpleDecrypt(encpytStr);
		System.out.println(acc_pwd+"解密过后的字符串是："+decpytStr);
		
	}

}
