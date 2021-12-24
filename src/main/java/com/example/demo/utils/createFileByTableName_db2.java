package com.example.demo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据数据库表名生成增删改查各层相关文件
 * @author lb
 *
 */
public class createFileByTableName_db2 {
			
	private static String driverClassName="oracle.jdbc.driver.OracleDriver";//数据库驱动名
	private static String url="jdbc:oracle:thin:@192.168.10.181:1521:orclutf";//数据库地址
	private static String username="gits2";//用户名
	private static String password="gits2";//密码
	
	private static String moduleName="base";//模块包名
	private static String menuName="审核流程";//页面功能名
	private static String schemaName="DBOS";//schema名
	private static String tableName="TP_SPOTDEALS";//表名
	private static String entityName="BusinessProduct";//实体类名
	private static String entityNameL="businessProduct";//实体变量名
	private static String filePath="D:/createFile/";//生成文件路径名
//	private static int idIndex=0;//表的主键id位于第几列（从0开始）
	private static String packageNameEntity="com.erayt.dbos.param.domain.param";//包名
	private static String packageNameService="com.erayt.dbos.param.bussniess.iface";//包名
	private static String packageNameServiceImpl="com.erayt.dbos.param.bussniess.service";//包名
	private static String packageNameDao="com.erayt.dbos.param.bussniess.dao";//包名
	private static String pathDaoXml="com/erayt/dbos/param/bussniess/dao/ibatis/sybase/";//包名
//	private static String packageNameDaoImpl=packageName+".dao.impl";//包名
//	private static String tableSeq=tableName+"_SEQ";//数据库序列名
	private static String XMLType="Ibatis";//Mybatis或者Ibatis或者Hibernate
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs =null;
	
	/**
	 * 创建相关文件
	 */
	public void createFile(){
		List<TableColumn> list = new ArrayList<TableColumn>();
		String sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, replace(b.comments,chr(10),'') as comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE,t.COLUMN_ID,t.DATA_PRECISION,t.DATA_SCALE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name  where t.TABLE_NAME=?   order by t.TABLE_NAME,t.COLUMN_ID";
		try {
			Class.forName(driverClassName);
			con = DriverManager.getConnection(url, username, password);
			ps = con.prepareStatement(sql);
			ps.setString(1, tableName.toUpperCase());//动态写入表名
//			ps.setString(2, schemaName.toUpperCase());//动态写入schemaName
//			ps.setString(3, schemaName.toUpperCase());//动态写入schemaName
			rs = ps.executeQuery();
			while(rs.next()){
				TableColumn knowledge = new TableColumn();
				knowledge.setColumnName(rs.getString(2));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
				knowledge.setDataType(rs.getString(4));
				knowledge.setDataLength(rs.getString(5));
				if(!knowledge.getColumnName().equals("ROW_ID")){
					list.add(knowledge);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		createEntity(list);
		createService(list);
		createServiceImpl(list);
		createDao(list);
		createXml(list);
		createHtml(list);
		createHtmlAdd(list);
		createJs(list);
		createConfig(list);
		createRouteXml(list);
	}
	

	/**
	 * 创建实体类文件
	 * @param list
	 */
	private void createEntity(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+".java");
			try {
			StringBuffer sb = new StringBuffer();
			sb.append("package "+packageNameEntity+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("import java.io.Serializable;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.common.SqlParamBase;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public class "+entityName+" extends SqlParamBase implements Serializable{");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("    private static final long serialVersionUID = 1L;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			for (TableColumn tableColumn : list) {
				//声明变量
				sb.append("    private "+tableColumn.getDataType()+" "+tableColumn.getColumnName().toLowerCase()+";");
//				if(list.indexOf(tableColumn)==idIndex){
//					sb.append("（主键）");
//				}
				sb.append(System.getProperty("line.separator"));
			}
			for (TableColumn tableColumn : list) {
				//get方法
				sb.append(System.getProperty("line.separator"));
				sb.append("    public "+getType(tableColumn.getDataType(),false)+" get"+getName(tableColumn.getColumnName())+"() {");
				sb.append(System.getProperty("line.separator"));
				sb.append("        return "+tableColumn.getColumnName().toLowerCase()+";");
				sb.append(System.getProperty("line.separator"));
				sb.append("    }");
				sb.append(System.getProperty("line.separator"));
				//set方法
				sb.append(System.getProperty("line.separator"));
				sb.append("    public void set"+getName(tableColumn.getColumnName())+"("+getType(tableColumn.getDataType(),false)+" "+tableColumn.getColumnName().toLowerCase()+") {");
				sb.append(System.getProperty("line.separator"));
				sb.append("        this."+tableColumn.getColumnName().toLowerCase()+" = "+tableColumn.getColumnName().toLowerCase()+";");
				sb.append(System.getProperty("line.separator"));
				sb.append("    }");
				sb.append(System.getProperty("line.separator"));
			}
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			System.out.println(file.getPath()+"创建成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建service文件
	 * @param list
	 */
	private void createService(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+"Service.java");
			try {
			StringBuffer sb = new StringBuffer();
			//包名
			sb.append("package "+packageNameService+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//引入包
			sb.append("import "+packageNameEntity+"."+entityName+";");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.ecas5.bean.User;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import java.util.Map;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar2.engine.query.QueryParam;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar2.engine.query.Pagination;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public interface "+entityName+"Service {");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("    public Pagination query (QueryParam queryParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("    public void add ("+entityName+" "+entityName.toLowerCase()+",User userParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("    public void update ("+entityName+" "+entityName.toLowerCase()+",User userParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("    public void delete (String id,User userParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//初始化数据
			sb.append("    public Map<String,Object> searchInitData ();");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			System.out.println(file.getPath()+"创建成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建serviceImpl文件
	 * @param list
	 */
	private void createServiceImpl(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+"ServiceImpl.java");
			try {
			StringBuffer sb = new StringBuffer();
			//包名
			sb.append("package "+packageNameServiceImpl+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//引入包
			sb.append("import java.lang.reflect.InvocationTargetException;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import java.util.ArrayList;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import java.util.HashMap;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import java.util.List;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import java.util.Map;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.dbos.param.bussniess.iface.EntityAreaService;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.common.SqlMapDao;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.ecas5.bean.User;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.common.BaseService;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.common.ApplicationException;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar.BasicException;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar2.engine.query.Pagination;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar2.engine.query.QueryParam;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.utils.GenernalDate;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameEntity+"."+entityName+";");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameService+"."+entityName+"Service;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameDao+"."+entityName+"Dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public class "+entityName+"ServiceImpl extends BaseService implements "+entityName+"Service{");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("    private "+entityName+"Dao "+entityNameL+"Dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    private EntityAreaService entityAreaService;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    public void set"+entityName+"Dao ("+entityName+"Dao "+entityNameL+"Dao) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        this."+entityNameL+"Dao = "+entityNameL+"Dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append("    public void setEntityAreaService(EntityAreaService entityAreaService) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        this.entityAreaService = entityAreaService;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("    public Pagination query(QueryParam queryParam) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        //String areaId = entityAreaService.getAreaIdByLoginId(userParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        //queryParam.put(\"areaId\", areaId);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        resetParam(queryParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        Pagination pagination = new Pagination();");
			sb.append(System.getProperty("line.separator"));
			sb.append("        List<"+entityName+"> list = "+entityNameL+"Dao.query(queryParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        pagination = setDatals(list, pagination);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        return pagination;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("    public void add ("+entityName+" "+entityName.toLowerCase()+", User userParam) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("         if("+entityName.toLowerCase()+"!=null && "+entityName.toLowerCase()+".getId()!=null && "+entityName.toLowerCase()+".getId().trim().length()>0){");
			sb.append(System.getProperty("line.separator"));
			sb.append("             int strCount="+entityNameL+"Dao.queryCountById(id);");
			sb.append(System.getProperty("line.separator"));
			sb.append("             if(strCount>0){");
			sb.append(System.getProperty("line.separator"));
			sb.append("                 throw new ApplicationException(\"data.addRepeat\");");
			sb.append(System.getProperty("line.separator"));
			sb.append("             }");
			sb.append(System.getProperty("line.separator"));
			sb.append("         } else{");
			sb.append(System.getProperty("line.separator"));
			sb.append("             throw new ApplicationException(\"data.dataBroken\");");
			sb.append(System.getProperty("line.separator"));
			sb.append("         }");
			sb.append(System.getProperty("line.separator"));
			sb.append("         "+entityNameL+"Dao.insert("+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("    public void update ("+entityName+" "+entityName.toLowerCase()+", User userParam) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("         "+entityNameL+"Dao.update("+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("    public void delete(String id, User userParam) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("         "+entityNameL+"Dao.delete(id);");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//searchInitData
			sb.append("    public Map<String, Object> searchInitData () {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        Map<String,Object> result = new HashMap<String,Object>();");
			sb.append(System.getProperty("line.separator"));
			sb.append("        //result.put(\"basisList\", paramDataCache.getCodeDefByType(Constant.CURRENCY_INTBASIS, \"\"));");
			sb.append(System.getProperty("line.separator"));
			sb.append("        return result;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			System.out.println(file.getPath()+"创建成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建Dao文件
	 * @param list
	 */
	private void createDao(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+"Dao.java");
			try {
			StringBuffer sb = new StringBuffer();
			//包名
			sb.append("package "+packageNameDao+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//引入包
			sb.append("import java.util.List;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameEntity+"."+entityName+";");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar2.engine.query.QueryParam;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public interface "+entityName+"Dao {");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("    public List<"+entityName+"> query (QueryParam queryParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("    public void insert ("+entityName+" "+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("    public void update ("+entityName+" "+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("    public void delete (String id);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			System.out.println(file.getPath()+"创建成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建Mybatis或者Ibatis或者Hibernate的XML文件
	 * @param list
	 */
	private void createXml(List<TableColumn> list) {
		if("Mybatis".equals(XMLType)){}else if("Ibatis".equals(XMLType)){
			if(list!=null && list.size()>0){
				File file = new File(filePath+entityName+"Dao.xml");
				try {
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("<sqlMap namespace=\""+packageNameDao+"."+entityName+"Dao\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <typeAlias alias=\""+entityName+"\" type=\""+packageNameEntity+"."+entityName+"\" />");
				sb.append(System.getProperty("line.separator"));
				//resultMap
				sb.append("    <resultMap id=\""+entityNameL+"MapPage\" class=\""+entityNameL+"\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("        <result column=\"COUNTNUM\" property=\"total\" />");
				sb.append(System.getProperty("line.separator"));
				for (TableColumn tableColumn : list) {
					sb.append("        <result column=\""+tableColumn.getColumnName()+"\" property=\""+tableColumn.getColumnName().toLowerCase()+"\"  />");
					sb.append(System.getProperty("line.separator"));
				}
				sb.append("    </resultMap>");
				sb.append(System.getProperty("line.separator"));
				//查
				sb.append(System.getProperty("line.separator"));
				sb.append("    <select id=\"query\" resultMap=\""+entityNameL+"MapPage\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("		    SELECT A.* FROM ( " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		        SELECT ROW_NUMBER() OVER(order by c.) ROW_ID , count(1) over() COUNTNUM ," );
				sb.append(System.getProperty("line.separator"));
				sb.append("		        " );
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append("c."+tableColumn.getColumnName());
					}else{
						sb.append("c."+tableColumn.getColumnName()+",");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("		        FROM "+tableName+" c " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		        <dynamic prepend=\"where\" > " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		            <isNotEmpty prepend=\"AND\" property=\"\"> " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		                c. = ## " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		            </isNotEmpty> " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		        </dynamic> " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		    )A WHERE 1 = 1  " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		    <isEqual property=\"pageCtrl\" compareValue=\"true\"> " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		        AND ROW_ID BETWEEN #start:INTEGER# AND #limit:INTEGER# " );
				sb.append(System.getProperty("line.separator"));
				sb.append("		    </isEqual> " );
				sb.append(System.getProperty("line.separator"));
				sb.append("    </select>");
				sb.append(System.getProperty("line.separator"));
				//增
				sb.append(System.getProperty("line.separator"));
				sb.append("    <insert id=\"insert\" parameterClass=\""+entityNameL+"\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("        insert into "+tableName+" (");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append(tableColumn.getColumnName()+") values ");
					}else{
						sb.append(tableColumn.getColumnName()+",");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("        (");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						if(tableColumn.getDataType().equals("String")){
							sb.append("#"+tableColumn.getColumnName().toLowerCase()+":VARCHAR#)");
						}else{
							sb.append("#"+tableColumn.getColumnName().toLowerCase()+"#)");
						}
					}else{
						if(tableColumn.getDataType().equals("String")){
							sb.append("#"+tableColumn.getColumnName().toLowerCase()+":VARCHAR#,");
						}else{
							sb.append("#"+tableColumn.getColumnName().toLowerCase()+"#,");
						}
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("    </insert>");
				sb.append(System.getProperty("line.separator"));
				//改
				sb.append(System.getProperty("line.separator"));
				sb.append("    <update id=\"update\" parameterClass=\""+entityNameL+"\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("      update "+tableName+" set ");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						if(tableColumn.getDataType().equals("String")){
							sb.append(tableColumn.getColumnName()+" = #"+tableColumn.getColumnName().toLowerCase()+":VARCHAR#");
						}else{
							sb.append(tableColumn.getColumnName()+" = #"+tableColumn.getColumnName().toLowerCase()+"#");
						}
					}else{
						if(tableColumn.getDataType().equals("String")){
							sb.append(tableColumn.getColumnName()+" = #"+tableColumn.getColumnName().toLowerCase()+":VARCHAR#,");
						}else{
							sb.append(tableColumn.getColumnName()+" = #"+tableColumn.getColumnName().toLowerCase()+"#,");
						}
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("     where id =#id#	");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </update>");
				sb.append(System.getProperty("line.separator"));
				//删
				sb.append(System.getProperty("line.separator"));
				sb.append("    <delete id=\"delete\" parameterClass=\"String\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("            delete from "+tableName+" where ID=#value#");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </delete>");
				sb.append(System.getProperty("line.separator"));
				
				sb.append("</sqlMap>");
				sb.append(System.getProperty("line.separator"));
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				System.out.println(file.getPath()+"创建成功");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if("Hibernate".equals(XMLType)){}
	}
	
	
	private void createConfig(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityName+"Config.txt");
			try {
			StringBuffer sb = new StringBuffer();
			sb.append("//SqlMapConfig.xml");
			sb.append(System.getProperty("line.separator"));
			sb.append("<sqlMap resource=\""+pathDaoXml+entityName+"Dao.xml\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("//route-"+moduleName+"-base.xml");
			sb.append(System.getProperty("line.separator"));
			sb.append("<import resource=\"classpath*:config/"+moduleName+"/route/route-"+entityNameL+".xml\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("//spring-"+moduleName+"-dao.xml");
			sb.append(System.getProperty("line.separator"));
			sb.append("<!-- "+menuName+"DAO -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <bean id=\""+entityNameL+"Dao\" class=\"com.erayt.solar.db.DaoProxyFactoryBean\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <property name=\"daoInterface\" value=\""+packageNameDao+"."+entityName+"Dao\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <property name=\"sqlMapClient\" ref=\"sqlMapClient\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </bean>");
			sb.append(System.getProperty("line.separator"));
			sb.append("//spring-"+moduleName+"-service.xml");
			sb.append(System.getProperty("line.separator"));
			sb.append("<!-- "+menuName+"Service -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <bean id=\""+entityNameL+"Service\" class=\""+packageNameServiceImpl+"."+entityName+"ServiceImpl\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <property name=\""+entityNameL+"Dao\" ref=\""+entityNameL+"Dao\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </bean>");
			sb.append(System.getProperty("line.separator"));
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			System.out.println(file.getPath()+"创建成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createJs(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityNameL+".js");
			try {
			StringBuffer sb = new StringBuffer();
			sb.append("function formAddAction(form){");
			sb.append(System.getProperty("line.separator"));
			sb.append("    form.attr('action',erajs.baseURL() +'"+entityNameL+"/add.action');");
			sb.append(System.getProperty("line.separator"));
			sb.append("    //erajs.Extra.getComboInitData(erajs.baseURL() +'"+entityNameL+"/searchInitData.action',null,'form[name="+entityNameL+"EditForm]');");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("function formEditAction(form){");
			sb.append(System.getProperty("line.separator"));
			sb.append("    form.attr('action',erajs.baseURL() +'"+entityNameL+"/update.action');");
			sb.append(System.getProperty("line.separator"));
			sb.append("    //erajs.Extra.getComboInitData(erajs.baseURL() +'"+entityNameL+"/searchInitData.action',null,'form[name="+entityNameL+"EditForm]');");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("function editFormAfterLoadProcess(){");
			sb.append(System.getProperty("line.separator"));
			sb.append("    //erajs.getCmp('ID').disable();");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			System.out.println(file.getPath()+"创建成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createHtmlAdd(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityNameL+".html");
			try {
			StringBuffer sb = new StringBuffer();
			sb.append("<!DOCTYPE html>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<html>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<head lang=\"en\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <meta charset=\"UTF-8\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <title>"+entityNameL+"</title>");
			sb.append(System.getProperty("line.separator"));
			sb.append("</head>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<body>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<form  id=\""+entityNameL+"EditForm\" name=\""+entityNameL+"EditForm\" class=\"form-css\" >");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <div xtype=\"layout\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <div data-options=\"region:'center',border:false\"  class=\"form-box\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("            <ul class=\"content_ul\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("                <li class=\"title120\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    <span class=\"require\">*</span>");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    <span class=\"LANGS\" langid=\""+entityNameL+".---ID\"></span>:");
			sb.append(System.getProperty("line.separator"));
			sb.append("                </li>");
			sb.append(System.getProperty("line.separator"));
			sb.append("                <li class=\"input180\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    <input xtype=\"textbox\"  name=\""+entityNameL+".---ID\" class=\"input-css\" data-options=\"width:180\"/>");
			sb.append(System.getProperty("line.separator"));
			sb.append("                </li>");
			sb.append(System.getProperty("line.separator"));
			sb.append("            </ul>");
			sb.append(System.getProperty("line.separator"));
			sb.append("        </div>");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <div region=\"south\" border=\"false\" class=\"form-foot-btns\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("            <div id=\"submitBut\" xtype=\"button\" class=\"middleBtn\" iconCls=\"icon-save\" ");
			sb.append(System.getProperty("line.separator"));
			sb.append("                custom-options=\"");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    btnType:'submit',");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    refDgId:'"+entityNameL+"Grid'");
			sb.append(System.getProperty("line.separator"));
			sb.append("            \"></div>");
			sb.append(System.getProperty("line.separator"));
			sb.append("            <div xtype=\"button\" class=\"middleBtn\" iconCls=\"icon-cancel\" ");
			sb.append(System.getProperty("line.separator"));
			sb.append("                custom-options=\"");
			sb.append(System.getProperty("line.separator"));
			sb.append("                btnType:'cancel'");
			sb.append(System.getProperty("line.separator"));
			sb.append("            \"></div>");
			sb.append(System.getProperty("line.separator"));
			sb.append("            ");
			sb.append(System.getProperty("line.separator"));
			sb.append("        </div>");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </div>");
			sb.append(System.getProperty("line.separator"));
			sb.append("</form>");
			sb.append(System.getProperty("line.separator"));
			sb.append("</body>");
			sb.append(System.getProperty("line.separator"));
			sb.append("</html>");
			sb.append(System.getProperty("line.separator"));
			
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			System.out.println(file.getPath()+"创建成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createHtml(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+entityNameL+"List.html");
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("<!DOCTYPE html>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<html>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<head lang=\"en\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <meta charset=\"UTF-8\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <title>"+entityNameL+"</title>");
				sb.append(System.getProperty("line.separator"));
				
				sb.append("    <script type=\"text/javascript\" src=\"../../common/js/bootstrap.js\"></script>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <script type=\"text/javascript\" src=\"../locale/locale.js\"></script>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <script type=\"text/javascript\" src=\"../js/"+entityNameL+".js\"></script>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <script>");
				sb.append(System.getProperty("line.separator"));
				sb.append("        erajs.onReady(function(){");
				sb.append(System.getProperty("line.separator"));
				sb.append("            //erajs.Extra.getComboInitData(erajs.baseURL() +'"+entityNameL+"/searchInitData.action',null,'form[name="+entityNameL+"QueryForm]');");
				sb.append(System.getProperty("line.separator"));
				sb.append("        })");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </script>");
				sb.append(System.getProperty("line.separator"));
				sb.append("</head>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<body xtype=\"layout\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <div class=\"search_div\" data-options=\"region:'north',height:42,border:true\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("        <form target=\"_top\" class=\"query-form\" method=\"POST\" name=\""+entityNameL+"QueryForm\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("            <ul class=\"content_ul\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                <li class=\"title95\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    <span class=\"LANGS\" langid=\""+entityNameL+".----\"></span>:");
				sb.append(System.getProperty("line.separator"));
				sb.append("                </li>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                <li class=\"input120\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    <input xtype=\"textbox\"  name=\"\" data-options=\"width:120\"/>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                </li>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                <li>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    <div xtype=\"button\" data-options=\"iconCls:'icon-search'\" custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    btnType : 'query',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    refDgId : '"+entityNameL+"Grid'");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    \"></div>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                </li>");
				sb.append(System.getProperty("line.separator"));
				sb.append("            </ul>");
				sb.append(System.getProperty("line.separator"));
				sb.append("        </form>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </div>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    <div data-options=\"region:'center',border:false\" class=\"paddingTop5\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("        <table id=\""+entityNameL+"Grid\" xtype=\"datagrid\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("            data-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                singleSelect:true,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                url: erajs.baseURL() +'"+entityNameL+"/query.action',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                toolbar:'#tbutton'");
				sb.append(System.getProperty("line.separator"));
				sb.append("                \"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    identifyField : ['----ID'],");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    queryFormName : '"+entityNameL+"QueryForm'");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    \">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                <thead>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    <tr>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        <th data-options=\"field:'ck',checkbox:true\"></th>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        <th data-options=\"field:'----ID',width:'16%',align:'left',halign:'center'\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                            <span class=\"LANGS\" langid=\""+entityNameL+".----ID\"></span>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        </th>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    </tr>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                </thead>");
				sb.append(System.getProperty("line.separator"));
				sb.append("            </table>");
				sb.append(System.getProperty("line.separator"));
				sb.append("            <div id=\"tbutton\" style=\"padding:5px;\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                <div xtype=\"button\" data-options=\"iconCls:'icon-add'\" ");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        langid: 'icon.add',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        dlgw: 380,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        dlgh: 355,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        mustSelected:false,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        url : '"+entityNameL+".html',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        callback : formAddAction");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        \">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                </div>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                <div xtype=\"button\" data-options=\"iconCls:'icon-edit'\" ");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        langid: 'icon.edit',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        dlgw: 380,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        dlgh: 355,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        isEdit : true,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        paramPrefix:'"+entityNameL+"',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        url : '"+entityNameL+".html',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        callback : formEditAction,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        editEvents:{");
				sb.append(System.getProperty("line.separator"));
				sb.append("                            onAfterLoad:editFormAfterLoadProcess");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        }");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        \">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                </div>");
				sb.append(System.getProperty("line.separator"));
				sb.append("                <div xtype=\"button\" data-options=\"iconCls:'icon-remove'\" ");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        url : erajs.baseURL() +'"+entityNameL+"/delete.action',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        actionType : 'do',");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        autoRefresh : true");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        \">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                </div>");
				sb.append(System.getProperty("line.separator"));
				sb.append("        </div>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </div>");
				sb.append(System.getProperty("line.separator"));
				sb.append("</body>");
				sb.append(System.getProperty("line.separator"));
				sb.append("</html>");
				sb.append(System.getProperty("line.separator"));
				
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				System.out.println(file.getPath()+"创建成功");
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	private void createRouteXml(List<TableColumn> list) {
		if(list!=null && list.size()>0){
			File file = new File(filePath+"route-"+entityNameL+".xml");
			try {
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"");
			sb.append(System.getProperty("line.separator"));
			sb.append("    xmlns:s=\"http://www.erayt.com/schema/solar/core\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			sb.append(System.getProperty("line.separator"));
			sb.append("    xmlns:aop=\"http://www.springframework.org/schema/aop\" xmlns:tx=\"http://www.springframework.org/schema/tx\"");
			sb.append(System.getProperty("line.separator"));
			sb.append("    xsi:schemaLocation=\"http://www.springframework.org/schema/beans");
			sb.append(System.getProperty("line.separator"));
			sb.append("        http://www.springframework.org/schema/beans/spring-beans-2.5.xsd  ");
			sb.append(System.getProperty("line.separator"));
			sb.append("        http://www.erayt.com/schema/solar/core");
			sb.append(System.getProperty("line.separator"));
			sb.append("        http://www.erayt.com/schema/solar/3.0/solar-spring.xsd\"");
			sb.append(System.getProperty("line.separator"));
			sb.append("    default-autowire=\"byName\">");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("    <!-- "+menuName+"查询-->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/query.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:var key=\"queryParam\" value=\"@queryParam\" clazz=\"com.erayt.solar2.engine.query.QueryParam\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"query\" parameters=\"@queryParam\" result=\"result\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </s:service>");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("    <!-- "+menuName+"新增 -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/add.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:var key=\"userParam\" value=\"$session[com.erayt.user_key]\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"add\" parameters=\"@"+entityNameL+",@userParam\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </s:service>");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("    <!-- "+menuName+"修改 -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/update.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:var key=\"userParam\" value=\"$session[com.erayt.user_key]\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"update\" parameters=\"@"+entityNameL+",@userParam\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </s:service>");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("    <!-- "+menuName+"删除 -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/delete.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:var key=\"userParam\" value=\"$session[com.erayt.user_key]\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"delete\" parameters=\"@----ID,@userParam\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </s:service>");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//相关数据获取
			sb.append("    <!-- "+menuName+"相关数据获取 -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/searchInitData.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"searchInitData\" result=\"result\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </s:service>");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			sb.append("</beans>");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			System.out.println(file.getPath()+"创建成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取数据库类型对应的java类型
	 * @param type,isPacName(返回值是否带包名)
	 * @return
	 */
	public String getType(String type,boolean isPacName){
			return type;
	}
	
	/**
	 * 获取表和字段对应的实体变量名（首字母大写）
	 * @param name
	 */
	public String getName(String name){
		if(name!=null && name.length()>0){
			if(name.length()==1){
				return name.toUpperCase();
			}else{
				return name.substring(0, 1).toUpperCase()+name.substring(1).toLowerCase();
			}
		}else{
			return "";
		}
	}
	
	public static void main(String[] args) {
		createFileByTableName_db2 createFileByTableName = new createFileByTableName_db2();
		createFileByTableName.createFile();
		System.out.println("执行结束");
	}
}
