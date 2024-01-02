package com.example.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class filemd5 {
	public static String byteArrayToHex(byte[] byteArray) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		char[] resultCharArray = new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		return new String(resultCharArray);
	}

	public static String fileMD5(File file) throws IOException {
		int bufferSize = 256 * 1024;
		FileInputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			fileInputStream = new FileInputStream(file);
			digestInputStream = new DigestInputStream(fileInputStream,
					messageDigest);
			byte[] buffer = new byte[bufferSize];
			while (digestInputStream.read(buffer) > 0)
				;
			messageDigest = digestInputStream.getMessageDigest();
			byte[] resultByteArray = messageDigest.digest();
			return byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
			return null;
		} finally {
			try {
				digestInputStream.close();
			} catch (Exception e) {
			}
			try {
				fileInputStream.close();
			} catch (Exception e) {
			}
		}
	}

	public static void listDirectory(File path, List<File> myfile) {
		if (!path.exists()) {
			System.out.println("文件名称不存在!");
		} else {
			if (path.isFile()) {
				myfile.add(path);
			} else {
				File[] files = path.listFiles();
				for (int i = 0; i < files.length; i++) {
					listDirectory(files[i], myfile);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String s = "D:\\林博\\图片";
		File path = new File(s);
		if (path.isDirectory()) {
			List<File> myfile = new ArrayList<File>();
			listDirectory(path, myfile);
			List<File> myfile2 = new ArrayList<File>();
			List<File> myfile3 = new ArrayList<File>();
			for (int i = 0; i < myfile.size(); i++) {
				for (int x = 0; x < i; x++) {
					if (myfile.get(x).getParentFile()
							.equals(myfile.get(i).getParentFile())
							&& fileMD5(myfile.get(x)).equals(
									fileMD5(myfile.get(i)))) {
						myfile2.add(myfile.get(x));
						myfile2.add(myfile.get(i));
					}
				}
			}
			for (File file : myfile) {
				if (!myfile2.contains(file)) {
					myfile3.add(file);
				}
			}
			myfile2.clear();
			for (File file : myfile) {
				if (!myfile3.contains(file)) {
					myfile2.add(file);
				}
			}
			myfile3.clear();
			for (int i = 0; i < myfile2.size(); i++) {
				String pree = myfile2.get(i).getParentFile().toString();
				String md52 = fileMD5(myfile2.get(i));
				for (File file : myfile2) {
					if (md52.equals(fileMD5(file))
							&& pree.equals(file.getParentFile().toString())) {
						if (!myfile3.contains(file)) {
							myfile3.add(file);
						}
					}
				}
			}
			List<Object> list1 = new ArrayList<Object>();
			List<Object> list2 = new ArrayList<Object>();
			for (File file : myfile3) {
				list1.add(file.getParentFile());
			}
			for (File file : myfile3) {
				list2.add(fileMD5(file));
			}
			int y = 1;
			String pfile = list1.get(0).toString();
			String md52 = list2.get(0).toString();
			for (int i = 0; i < myfile3.size(); i++) {
				File file = myfile3.get(i);
				String pre = myfile3.get(i).getName();
				String md5 = fileMD5(file);
				if (!pre.contains(md5)) {
					for (int x = 0; x < myfile3.size(); x++) {
						if (i != x) {
							if (list1.get(x).equals(list1.get(i))
									&& list2.get(i).equals(list2.get(x))) {
								if (!pfile.equals(list1.get(i).toString())
										|| !md52.equals(md5)) {
									pfile = list1.get(i).toString();
									md52 = md5;
									y = 1;
								}
								File dest = new File(list1.get(i) + "/" + "重复"
										+ md5 + "-" + y + "-" + pre);
								file.renameTo(dest);
							}
						}
					}
				}
				if (pfile.equals(list1.get(i).toString()) && md52.equals(md5)) {
					y++;
				}
			}
			System.out.println("\n重命名完成！");
		}
		List<File> myfile = new ArrayList<File>();
		listDirectory(path, myfile);
		for (File file : myfile) {
			System.out.println(file);
		}
	}
}