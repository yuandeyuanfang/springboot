package com.example.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件相关操作类
 * @author lb
 *
 */
public class FileHandle {
	/**
	 * 判断文件或目录是否存在
	 * @param 文件路径
	 * @return
	 */
	public boolean isFileExist(String filePath){
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	 * 判断文件或目录是否存在
	 * @return
	 */
	public boolean isFileExist(File file){
		return file.exists();
	}
	
	/**
	 * 创建文件，若文已存在则返回已有文件
	 * @param 文件路径
	 * @return
	 */
	public File createFile(String filePath){
		File file = new File(filePath);
		if(file.exists()){
		}else{
			try {
				file.createNewFile();
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
		return file;
	} 
	
	/**
	 * 创建文件，若文已存在则返回已有文件
	 * @return
	 */
	public File createFile(File file){
		if(file.exists()){
		}else{
			try {
				file.createNewFile();
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
		return file;
	} 
	
	/**
	 * 删除文件，若文件不存在则不进行操作
	 * @param 文件路径
	 * @return
	 */
	public void deleteFile(String filePath){
		File file = new File(filePath);
		file.deleteOnExit();
	} 
	
	/**
	 * 删除文件，若文件不存在则不进行操作
	 * @return
	 */
	public void deleteFile(File file){
		file.deleteOnExit();
	} 
	
	/**
	 * 获取文件路径下的所有文件（不包括子路径下文件）
	 * @param 文件路径、是否将子路径当做文件
	 * @return 若无结果，返回null
	 */
	public List<File> selectFile(String filePath, boolean isIncludeFolder){
		File file = new File(filePath);
		List<File> fileListReturn = new ArrayList<File>();
		File[] fileList = file.listFiles();
		if(fileList!=null && fileList.length>0){
			for (int i = 0; i < fileList.length; i++) {
				if(fileList[i].isDirectory() && !isIncludeFolder){
				}else{
					fileListReturn.add(fileList[i]);
				}
			}
		}
		return fileListReturn;
	} 
	
	/**
	 * 获取文件路径下的所有文件（不包括子路径下文件）
	 * @param 文件、是否将子路径当做文件
	 * @return 若无结果，返回null
	 */
	public List<File> selectFile(File file, boolean isIncludeFolder){
		List<File> fileListReturn = new ArrayList<File>();
		File[] fileList = file.listFiles();
		if(fileList!=null && fileList.length>0){
			for (int i = 0; i < fileList.length; i++) {
				if(fileList[i].isDirectory() && !isIncludeFolder){
				}else{
					fileListReturn.add(fileList[i]);
				}
			}
		}
		return fileListReturn;
	} 
	
	/**
	 * 获取文件路径下的所有文件（包括子路径下文件）
	 * @param 文件路径
	 * @return 若无结果，返回null
	 */
	public static List<File> selectAllFile(String filePath){
		File file = new File(filePath);
		List<File> fileListReturn = new ArrayList<File>();
		File[] fileList = file.listFiles();
		if(fileList!=null && fileList.length>0){
			for (int i = 0; i < fileList.length; i++) {
				if(fileList[i].isDirectory()){
					fileListReturn.addAll(selectAllFile(fileList[i]));
				}else{
					fileListReturn.add(fileList[i]);
				}
			}
		}
		return fileListReturn;
	} 
	
	/**
	 * 获取文件路径下的所有文件（包括子路径下文件）
	 * @param 文件
	 * @return 若无结果，返回null
	 */
	public static List<File> selectAllFile(File file){
		List<File> fileListReturn = new ArrayList<File>();
		File[] fileList = file.listFiles();
		if(fileList!=null && fileList.length>0){
			for (int i = 0; i < fileList.length; i++) {
				if(fileList[i].isDirectory()){
					fileListReturn.addAll(selectAllFile(fileList[i]));
				}else{
					fileListReturn.add(fileList[i]);
				}
			}
		}
		return fileListReturn;
	} 
	
	public static void compareFolder(String filePathA, String filePathB){
		List<File> fileListA = selectAllFile(filePathA);
		List<File> fileListB = selectAllFile(filePathB);
		StringBuffer onlyInFileA = new StringBuffer("");
		StringBuffer onlyInFileB = new StringBuffer("");
		StringBuffer differentFile = new StringBuffer("");
		int numA=0;
		int numB=0;
		for (File fileA : fileListA) {
			numA++;
			String fileNameA = fileA.getAbsolutePath().substring(fileA.getAbsolutePath().indexOf(":")+1);
			boolean isInB = false; 
			for (File fileB : fileListB) {
				String fileNameB = fileB.getAbsolutePath().substring(fileB.getAbsolutePath().indexOf(":")+1);
				if(fileNameA.equals(fileNameB)){
					isInB = true;
					if(fileA.length() != fileB.length()){
						differentFile.append(fileA.getAbsolutePath());
						differentFile.append(System.getProperty("line.separator"));
					}
				}
			}
			if(!isInB){
				onlyInFileA.append(fileA.getAbsolutePath());
				onlyInFileA.append(System.getProperty("line.separator"));
			}
		}
		for (File fileB : fileListB) {
			numB++;
			String fileNameB = fileB.getAbsolutePath().substring(fileB.getAbsolutePath().indexOf(":")+1);
			boolean isInA = false; 
			for (File fileA : fileListA) {
				String fileNameA = fileA.getAbsolutePath().substring(fileA.getAbsolutePath().indexOf(":")+1);
				if(fileNameA.equals(fileNameB)){
					isInA = true;
				}
			}
			if(!isInA){
				onlyInFileB.append(fileB.getAbsolutePath());
				onlyInFileB.append(System.getProperty("line.separator"));
			}
		}
		try {
			System.out.println(numA+"--"+numB);
			File onlyFileA = new File("D:\\COMPAIR-onlyInFileA.txt");
			FileOutputStream fos = new FileOutputStream(onlyFileA);
			fos.write(onlyInFileA.toString().getBytes());
			fos.flush();
			fos.close();
			
			File onlyFileB = new File("D:\\COMPAIR-onlyInFileB.txt");
			fos = new FileOutputStream(onlyFileB);
			fos.write(onlyInFileB.toString().getBytes());
			fos.flush();
			fos.close();
			
			File difFile = new File("D:\\COMPAIR-differentFile.txt");
			fos = new FileOutputStream(difFile);
			fos.write(differentFile.toString().getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void fileInputStreamDemo() throws IOException {
		try(FileInputStream fileInputStream = new FileInputStream("D:1.txt");){
			byte[] byteArr = new byte[32];
			int readResult = fileInputStream.read(byteArr);
			System.out.println(readResult);
			System.out.println(new String(byteArr));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void fileOutputStreamDemo(boolean isAppend) throws IOException {
		try(FileOutputStream fileOutputStream = new FileOutputStream("D:1.txt",isAppend)){
			String txt = "fileOutputStreamDemo";
			fileOutputStream.write(txt.getBytes());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		//比较两个文件夹下文件数量
//		compareFolder("D:\\gits-system","E:\\gits-system");
		//FileInputStreamDemo
		fileInputStreamDemo();
//		fileOutputStreamDemo(false);
	}
}
