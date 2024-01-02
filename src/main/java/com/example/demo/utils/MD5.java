package com.example.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 获取文件、字符串的MD5
 * @author lb
 *
 */
public class MD5 {
	/**
	 * Get MD5 of one file:hex string,test OK!
	 * 获取文件的MD5
	 * @param file(File类型)
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
	/*** 
     * Get MD5 of one file！test ok! 
     *  获取文件的MD5
     * @param filepath(文件路径，String类型)
     * @return 
     */  
    public static String getFileMD5(String filepath) {
        File file = new File(filepath);
        return getFileMD5(file);  
    }  
  
    /*** 
     * compare two file by Md5 
     *  比较两个文件MD5是否相等
     * @param file1 
     * @param file2 
     * @return 
     */  
    public static boolean isSameMd5(File file1, File file2){
        String md5_1=getFileMD5(file1);
        String md5_2=getFileMD5(file2);
        return md5_1.equals(md5_2);  
    }  
    /*** 
     * compare two file by Md5 
     *  比较两个文件MD5是否相等
     * @param filepath1 
     * @param filepath2 
     * @return 
     */  
    public static boolean isSameMd5(String filepath1, String filepath2){
        File file1=new File(filepath1);
        File file2=new File(filepath2);
        return isSameMd5(file1, file2);  
    }
    
  //十六进制下数字到字符的映射数组   
  	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
  			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

  	/**  
  	 * 验证输入的密码是否正确（不区分大小写）  
  	 * @param password    加密后的密码  
  	 * @param inputString    输入的字符串  
  	 * @return    验证结果，TRUE:正确 FALSE:错误  
  	 */
  	public static boolean validatePassword(String password, String inputString) {
  		if (password.toUpperCase().equals(encodeByMD5(inputString).toUpperCase())) {
  			return true;
  		} else {
  			return false;
  		}
  	}

  	/**  
  	 * 对字符串进行MD5加密
  	 * @param originString    输入的字符串  
  	 * @return    加密后的字符串  
  	 */
  	public static String encodeByMD5(String originString) {
  		if (originString != null) {
  			try {
  				//创建具有指定算法名称的信息摘要   
  				MessageDigest md = MessageDigest.getInstance("MD5");
  				//使用指定的字节数组对摘要进行最后更新，然后完成摘要计算   
  				byte[] results = md.digest(originString.getBytes());
  				//将得到的字节数组变成字符串返回   
  				String resultString = byteArrayToHexString(results);
  				return resultString;
  			} catch (Exception ex) {
  			}
  		}
  		return null;
  	}

  	/**   
  	 * 转换字节数组为十六进制字符串  
  	 * @param     字节数组  
  	 * @return    十六进制字符串  
  	 */
  	private static String byteArrayToHexString(byte[] b) {
  		StringBuffer resultSb = new StringBuffer();
  		for (int i = 0; i < b.length; i++) {
  			resultSb.append(byteToHexString(b[i]));
  		}
  		return resultSb.toString();
  	}

  	/** 将一个字节转化成十六进制形式的字符串     */
  	private static String byteToHexString(byte b) {
  		int n = b;
  		if (n < 0)
  			n = 256 + n;
  		int d1 = n / 16;
  		int d2 = n % 16;
  		return hexDigits[d1] + hexDigits[d2];
  	}
  	
    public static void main(String[] args) {
		System.out.println(getFileMD5("D:\\adt.zip"));
		System.out.println(encodeByMD5("123456"));
	}
}
