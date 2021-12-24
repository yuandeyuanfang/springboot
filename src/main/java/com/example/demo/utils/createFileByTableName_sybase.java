package com.example.demo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据数据库表名生成增删改查各层相关文件
 * @author lb
 *
 */
public class createFileByTableName_sybase {
	
	private static String moduleName="product520";//"+moduleName+"
	private static String menuName="批处理日志";//页面功能名
	private static String tableName="RS_PRODUCT_520";//表名
	private static String entityName="BatchLog";//实体类名
	private static String entityNameL="batchLog";//实体变量名
	private static String filePath="D:/";//生成文件路径名
	private static String XMLType="Ibatis";//Mybatis或者Ibatis或者Hibernate
//	private static int idIndex=0;//表的主键id位于第几列（从0开始）
	private static String packageNameEntity="com.erayt.dbos.risk.credit.domain";//包名
	private static String packageNameService="com.erayt.dbos.risk.credit.manage.iface";//包名
	private static String packageNameServiceImpl="com.erayt.dbos.risk.credit.manage.impl";//包名
	private static String packageNameDao="com.erayt.dbos.risk.credit.dao.ibatis.iface";//包名
//	private static String packageNameDaoImpl=packageName+".dao.impl";//包名
//	private static String tableSeq=tableName+"_SEQ";//数据库序列名
	
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs =null;
	
	/**
	 * 创建相关文件
	 */
	public void createFile(){
		JdbcUtil jdbcUtil = new JdbcUtil();
		con = jdbcUtil.getConnection(con);
		List<TableColumn> list = new ArrayList<TableColumn>();
		String sql = " select c.colid colID, c.name colName, o.name +'.'+c.name as tableCol,    STR_REPLACE(STR_REPLACE(STR_REPLACE(STR_REPLACE(STR_REPLACE(STR_REPLACE(STR_REPLACE(STR_REPLACE(t.name,'varchar','String'),'char','String'),'smallint','Integer'),'intn','Integer'),'floatn','Float'),'float','Float'),'datetime','String'),'datetimn','String') typeName,    c.length colLength   from syscolumns c, sysobjects o, systypes t   where c.type = t.type and c.id = o.id and t.name not in ('nchar', 'longsysname', 'nvarchar', 'sysname')    and c.name not in ('ROW_ID') and o.name = ?    order by c.colid";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, tableName.toUpperCase());//动态写入表名
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
			sb.append("import com.erayt.ml.param.base.domain.SqlParamBase;");
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
			sb.append("import java.util.List;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameEntity+"."+entityName+";");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.ecas5.bean.User;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar2.engine.query.QueryParam;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar2.engine.query.Pagination;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public interface "+entityName+"Service {");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("    public void add ("+entityName+" "+entityName.toLowerCase()+",User userParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("    public void delete (String[] ids);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("    public void update ("+entityName+" "+entityName.toLowerCase()+",User userParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("    public Pagination query (QueryParam queryParam);");
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
			sb.append("import com.erayt.common.SqlMapDao;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.dbos.param.cache.ParamDataCache;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.dbos.param.domain.StaticDataOperateRecord;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.dbos.param.service.impl.SDOperateRecordServiceImpl;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.dbos.param.util.JsonRes;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.ecas5.bean.User;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.ml.param.base.BaseService;");
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
			sb.append(System.getProperty("line.separator"));
			sb.append("    protected SqlMapDao dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("    private ParamDataCache paramDataCache;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("    private SDOperateRecordServiceImpl sdOperateRecordService;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("    public void set"+entityName+"Dao ("+entityName+"Dao "+entityNameL+"Dao) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        this."+entityNameL+"Dao = "+entityNameL+"Dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append("    public void setDao(SqlMapDao dao) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        this.dao = dao;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append("    public void setSdOperateRecordService(SDOperateRecordServiceImpl sdOperateRecordService) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        this.sdOperateRecordService = sdOperateRecordService;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append("    public void setParamDataCache(ParamDataCache paramDataCache) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        this.paramDataCache = paramDataCache;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("    public void add ("+entityName+" "+entityName.toLowerCase()+", User userParam) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("         if("+entityName.toLowerCase()+"!=null && "+entityName.toLowerCase()+".getId()!=null && "+entityName.toLowerCase()+".getId().trim().length()>0){");
			sb.append(System.getProperty("line.separator"));
			sb.append("             String sqlCount = \"SELECT count(1) FROM "+tableName+" where 主键 = \";");
			sb.append(System.getProperty("line.separator"));
			sb.append("             Object strCount=dao.queryForObject(\"Select.getTableDateCount\", sqlCount);");
			sb.append(System.getProperty("line.separator"));
			sb.append("             if(strCount!=null && Integer.parseInt(strCount.toString())>0){");
			sb.append(System.getProperty("line.separator"));
			sb.append("                 throw new BasicException(Constant.addRepeat);");
			sb.append(System.getProperty("line.separator"));
			sb.append("             }");
			sb.append(System.getProperty("line.separator"));
			sb.append("         } else{");
			sb.append(System.getProperty("line.separator"));
			sb.append("             throw new BasicException(Constant.dataBroken);");
			sb.append(System.getProperty("line.separator"));
			sb.append("         }");
			sb.append(System.getProperty("line.separator"));
			sb.append("         "+entityNameL+"Dao.insert("+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("    public void delete(String[] Ids) {");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("            Map map = new HashMap<String, Object>();");
			sb.append(System.getProperty("line.separator"));
			sb.append("            List<"+entityName+"> "+entityNameL+"List = new ArrayList<"+entityName+">();");
			sb.append(System.getProperty("line.separator"));
			sb.append("            if(Ids!=null && Ids.length>0){");
			sb.append(System.getProperty("line.separator"));
			sb.append("                for (int i = 0; i < Ids.length; i++) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    sdOperateRecordService.insertLogDataBase(\""+tableName+"\",StaticDataOperateRecord.ActionName_Delete, \"id='\"+ Ids[i]  +\"'\",");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    StaticDataOperateRecord.NotSystemAction,GenernalDate.getDateTime(), \"liu\");");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    "+entityName+" "+entityNameL+" = new "+entityName+"();");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    "+entityNameL+".setIds(Ids[i]);");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    "+entityNameL+"List.add("+entityNameL+");");
			sb.append(System.getProperty("line.separator"));
			sb.append("                }");
			sb.append(System.getProperty("line.separator"));
			sb.append("            }");
			sb.append(System.getProperty("line.separator"));
			sb.append("            map.put(\""+entityNameL+"List\", "+entityNameL+"List);");
			sb.append(System.getProperty("line.separator"));
			sb.append("            "+entityNameL+"Dao.delete(map);");
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
			//查
			sb.append("    public Pagination query(QueryParam queryParam) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        Pagination pagination = new Pagination();");
			sb.append(System.getProperty("line.separator"));
			sb.append("        resetParam(queryParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        StringBuffer sqlCount = new StringBuffer();");
			sb.append(System.getProperty("line.separator"));
			sb.append("        sqlCount.append(\"SELECT count(1) FROM "+tableName+" WHERE 1 = 1 \");");
			sb.append(System.getProperty("line.separator"));
			sb.append("        paramSql(queryParam, sqlCount);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        Object strCount = dao.queryForObject(\"Select.getTableDateCount\",sqlCount.toString());");
			sb.append(System.getProperty("line.separator"));
			sb.append("        if (strCount == null) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("            queryParam.setTotal(0);");
			sb.append(System.getProperty("line.separator"));
			sb.append("            pagination.setTotal(0);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        } else {");
			sb.append(System.getProperty("line.separator"));
			sb.append("            queryParam.setTotal(Integer.parseInt(strCount.toString()));");
			sb.append(System.getProperty("line.separator"));
			sb.append("            pagination.setTotal(Integer.parseInt(strCount.toString()));");
			sb.append(System.getProperty("line.separator"));
			sb.append("        }");
			sb.append(System.getProperty("line.separator"));
			sb.append("        if (pagination.getTotal() == 0) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("            pagination = setDatals(new ArrayList(), pagination);");
			sb.append(System.getProperty("line.separator"));
			sb.append("            return pagination;");
			sb.append(System.getProperty("line.separator"));
			sb.append("        }");
			sb.append(System.getProperty("line.separator"));
			sb.append("        StringBuffer sqlPage = new StringBuffer();");
			sb.append(System.getProperty("line.separator"));
			sb.append("        sqlPage.append(\"select ");
			for (TableColumn tableColumn : list) {
				if(list.indexOf(tableColumn)==(list.size()-1)){
					sb.append(tableColumn.getColumnName().toLowerCase()+" ");
				}else{
					sb.append(tableColumn.getColumnName().toLowerCase()+",");
				}
			}
			sb.append(" from "+tableName+" WHERE 1 = 1 \");");
			sb.append(System.getProperty("line.separator"));
			sb.append("        paramSql(queryParam, sqlPage);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        sqlPage.append(\" order by id \");");
			sb.append(System.getProperty("line.separator"));
			sb.append("        queryParam.put(\"sql\", sqlPage.toString());");
			sb.append(System.getProperty("line.separator"));
			sb.append("        int totalPage = queryParam.getTotal()/ Integer.parseInt(queryParam.get(\"pageLimit\").toString());// 计算总页数，调用存储过程时需要用到");
			sb.append(System.getProperty("line.separator"));
			sb.append("        if (queryParam.getTotal()% Integer.parseInt(queryParam.get(\"pageLimit\").toString()) > 0) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("            totalPage++;");
			sb.append(System.getProperty("line.separator"));
			sb.append("        }");
			sb.append(System.getProperty("line.separator"));
			sb.append("        queryParam.put(\"totalPage\", totalPage);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        List list = "+entityNameL+"Dao.query(queryParam);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        pagination = setDatals(list, pagination);");
			sb.append(System.getProperty("line.separator"));
			sb.append("        pagination.setTotal(Integer.parseInt(strCount.toString()));");
			sb.append(System.getProperty("line.separator"));
			sb.append("        return pagination;");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//paramSql
			sb.append("    public void paramSql(QueryParam queryParam, StringBuffer sql) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("        if (queryParam != null) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("            //if (queryParam.get(\"Id\") != null && queryParam.get(\"Id\").toString().trim().length() > 0) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("                //sql.append(\" and id = '\"+ queryParam.get(\"Id\").toString().trim() + \"'\");");
			sb.append(System.getProperty("line.separator"));
			sb.append("            //}");
			sb.append(System.getProperty("line.separator"));
			sb.append("            //if (queryParam.get(\"Id\") != null && queryParam.get(\"Id\").toString().trim().length() > 0) {");
			sb.append(System.getProperty("line.separator"));
			sb.append("                //sql.append(\" and id like '%\"+ queryParam.get(\"Id\").toString().trim() + \"%'\");");
			sb.append(System.getProperty("line.separator"));
			sb.append("            //}");
			sb.append(System.getProperty("line.separator"));
			sb.append("        }");
			sb.append(System.getProperty("line.separator"));
			sb.append("    }");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			//searchQueryData
			sb.append("    public Map<String, Object> searchQueryData () throws IllegalAccessException, InvocationTargetException {");
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
			sb.append("import java.util.Map;");
			sb.append(System.getProperty("line.separator"));
			sb.append("import "+packageNameEntity+"."+entityName+";");
			sb.append(System.getProperty("line.separator"));
			sb.append("import com.erayt.solar2.engine.query.QueryParam;");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("public interface "+entityName+"Dao {");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//增
			sb.append("    public void insert ("+entityName+" "+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("    public void delete (Map map);");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("    public void update ("+entityName+" "+entityName.toLowerCase()+");");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//查
			sb.append("    public List query (QueryParam queryParam);");
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
				sb.append("    <resultMap id=\""+entityNameL+"Map\" class=\""+entityNameL+"\">");
				sb.append(System.getProperty("line.separator"));
				for (TableColumn tableColumn : list) {
					sb.append("        <result column=\""+tableColumn.getColumnName()+"\" property=\""+tableColumn.getColumnName().toLowerCase()+"\"  />");
					sb.append(System.getProperty("line.separator"));
				}
				sb.append("    </resultMap>");
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
							sb.append("#"+tableColumn.getColumnName().toLowerCase()+"#)");
					}else{
							sb.append("#"+tableColumn.getColumnName().toLowerCase()+"#,");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("    </insert>");
				sb.append(System.getProperty("line.separator"));
				//删
				sb.append(System.getProperty("line.separator"));
				sb.append("    <delete id=\"delete\" parameterClass=\"map\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("        <iterate property=\""+entityNameL+"List\" conjunction=\"\" open=\"\" close=\"\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("            delete from "+tableName+" where ID=#"+entityNameL+"List[].ID#");
				sb.append(System.getProperty("line.separator"));
				sb.append("        </iterate>");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </delete>");
				sb.append(System.getProperty("line.separator"));
				//改
				sb.append(System.getProperty("line.separator"));
				sb.append("    <update id=\"update\" parameterClass=\""+entityNameL+"\" >");
				sb.append(System.getProperty("line.separator"));
				sb.append("      update "+tableName+" set ");
				for (TableColumn tableColumn : list) {
					if(list.indexOf(tableColumn)==(list.size()-1)){
						sb.append(tableColumn.getColumnName()+" = #"+tableColumn.getColumnName().toLowerCase()+"#");
					}else{
						sb.append(tableColumn.getColumnName()+" = #"+tableColumn.getColumnName().toLowerCase()+"#,");
					}
				}
				sb.append(System.getProperty("line.separator"));
				sb.append("     where id =#id#	");
				sb.append(System.getProperty("line.separator"));
				sb.append("    </update>");
				sb.append(System.getProperty("line.separator"));
				//查
				sb.append(System.getProperty("line.separator"));
				sb.append("    <select id=\"query\" resultMap=\""+entityNameL+"Map\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("		    {call splitPage(#sql#,#pageStart#,#pageLimit#,#totalPage#)} " );
				sb.append(System.getProperty("line.separator"));
				sb.append("    </select>");
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
			sb.append("<sqlMap resource=\"config/"+moduleName+"/ibatis/sybase/"+entityName+"Dao.xml\" />");
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
			sb.append("    erajs.Extra.getComboInitData(erajs.baseURL() +'"+entityNameL+"/searchQueryData.action',null,'form[name="+entityNameL+"Form2]');");
			sb.append(System.getProperty("line.separator"));
			sb.append("}");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("function formEditAction(form){");
			sb.append(System.getProperty("line.separator"));
			sb.append("    form.attr('action',erajs.baseURL() +'"+entityNameL+"/update.action');");
			sb.append(System.getProperty("line.separator"));
			sb.append("    erajs.Extra.getComboInitData(erajs.baseURL() +'"+entityNameL+"/searchQueryData.action',null,'form[name="+entityNameL+"Form2]');");
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
			sb.append(System.getProperty("line.separator"));
			sb.append("function copyFormAfterLoadProcess(){");
			sb.append(System.getProperty("line.separator"));
			sb.append("    $(\"#"+entityNameL+"Form2\").attr('action',erajs.baseURL() +'"+entityNameL+"/add.action');");
			sb.append(System.getProperty("line.separator"));
			sb.append("    //erajs.getCmp('ID').setValue('');");
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
			sb.append("<form  id=\""+entityNameL+"Form2\" name=\""+entityNameL+"Form2\" class=\"form-css\" >");
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
			sb.append("                    <span class=\"LANGS\" langid=\""+entityNameL+".form.ID\"></span>:");
			sb.append(System.getProperty("line.separator"));
			sb.append("                </li>");
			sb.append(System.getProperty("line.separator"));
			sb.append("                <li class=\"input180\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    <input xtype=\"textbox\"  name=\""+entityNameL+".locationID\" class=\"input-css\" data-options=\"width:120\"/>");
			sb.append(System.getProperty("line.separator"));
			sb.append("                </li>");
			sb.append(System.getProperty("line.separator"));
			sb.append("            </ul>");
			sb.append(System.getProperty("line.separator"));
			sb.append("        </div>");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <div region=\"south\" border=\"false\" class=\"form-foot-btns\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("            <div id=\"submitBut\" xtype=\"button\" class=\"middleBtn\" langid ='"+entityNameL+".form.submit' iconCls=\"icon-save\"");
			sb.append(System.getProperty("line.separator"));
			sb.append("                custom-options=\"");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    btnType:'submit',");
			sb.append(System.getProperty("line.separator"));
			sb.append("                    refDgId:'"+entityNameL+"Grid'");
			sb.append(System.getProperty("line.separator"));
			sb.append("            \"></div>");
			sb.append(System.getProperty("line.separator"));
			sb.append("            <div xtype=\"button\" class=\"middleBtn\" iconCls=\"icon-cancel\" langid ='"+entityNameL+".form.cancel'");
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
				sb.append("            //erajs.Extra.getComboInitData(erajs.baseURL() +'"+entityNameL+"/searchQueryData.action',null,'form[name="+entityNameL+"QueryForm]');");
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
				sb.append("                    <span class=\"LANGS\" langid=\""+entityNameL+".query.----\"></span>:");
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
				sb.append("                    <div xtype=\"button\" data-options=\"iconCls:'icon-search'\" langid ='"+entityNameL+".operation.query' custom-options=\"");
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
				sb.append("                        <th data-options=\"field:'----ID',width:'16%',align:'center',sortable:true\">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                            <span class=\"LANGS\" langid=\""+entityNameL+".fields.----ID\"></span>");
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
				sb.append("                <div xtype=\"button\" data-options=\"iconCls:'icon-add'\" langid ='"+entityNameL+".operation.add' ");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        langid : '"+entityNameL+".window.add',");
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
				sb.append("                <div xtype=\"button\" data-options=\"iconCls:'icon-edit'\" langid ='"+entityNameL+".operation.edit' ");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        langid : '"+entityNameL+".window.edit',");
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
				sb.append("                <div xtype=\"button\" data-options=\"iconCls:'icon-remove'\" langid ='"+entityNameL+".operation.del' ");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        langid : '"+entityNameL+".window.del',");
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
				sb.append("                <div xtype=\"button\" data-options=\"iconCls:'icon-copy'\" langid ='"+entityNameL+".operation.copy' ");
				sb.append(System.getProperty("line.separator"));
				sb.append("                    custom-options=\"");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        langid : '"+entityNameL+".window.copy',");
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
				sb.append("                        callback : formAddAction,");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        editEvents:{");
				sb.append(System.getProperty("line.separator"));
				sb.append("                            onAfterLoad:copyFormAfterLoadProcess");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        }");
				sb.append(System.getProperty("line.separator"));
				sb.append("                        \">");
				sb.append(System.getProperty("line.separator"));
				sb.append("                </div>");
				sb.append(System.getProperty("line.separator"));
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
			//增
			sb.append("    <!-- "+menuName+"新增 -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/add.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"add\" parameters=\"@"+entityNameL+",@userParam\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </s:service>");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//删
			sb.append("    <!-- "+menuName+"删除 -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/delete.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"delete\" parameters=\"@"+entityNameL+"ID\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </s:service>");
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			//改
			sb.append("    <!-- "+menuName+"修改 -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/update.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"update\" parameters=\"@"+entityNameL+",@userParam\" />");
			sb.append(System.getProperty("line.separator"));
			sb.append("    </s:service>");
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
			
			//相关数据获取
			sb.append("    <!-- "+menuName+"相关数据获取 -->");
			sb.append(System.getProperty("line.separator"));
			sb.append("    <s:service id=\"/"+entityNameL+"/searchQueryData.action\">");
			sb.append(System.getProperty("line.separator"));
			sb.append("        <s:bean ref=\""+entityNameL+"Service\" method=\"searchQueryData\" result=\"result\" />");
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
		createFileByTableName_sybase createFileByTableName = new createFileByTableName_sybase();
		createFileByTableName.createFile();
		System.out.println("执行结束");
	}
}
