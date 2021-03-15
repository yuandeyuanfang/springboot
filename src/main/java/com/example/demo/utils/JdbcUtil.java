package com.example.demo.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JdbcUtil {

	private String driverClassName = "";
	private String url = "";
	private String username = "";
	private String password = "";
	
	public Connection getConnection(Connection con){
		try {
			getProperties();
			Class.forName(driverClassName);
			con = DriverManager.getConnection(url, username, password);
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return con;
		}
	}
	
	public void getProperties(){
		Properties pro = new Properties();
		try {
			String classPath = this.getClass().getResource("/").getPath();//获取classpath /C:/lbwork/apache-tomcat-8080-fmms/webapps/lb-base/WEB-INF/classes/
//			 classPath = this.getClass().getResource("").getPath();//不加’/'时，默认是从此类所在的包下取资源 /C:/lbwork/apache-tomcat-8080-fmms/webapps/lb-base/WEB-INF/classes/com/lb/base/system/
			InputStream in = new BufferedInputStream(new FileInputStream(new File(classPath+"jdbc.properties")));
			pro.load(in);
			this.driverClassName = pro.getProperty("jdbc.driverClassName");
			this.url = pro.getProperty("jdbc.url");
			this.username = pro.getProperty("jdbc.username");
			this.password = pro.getProperty("jdbc.password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
