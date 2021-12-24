package com.example.demo.utils;

import oracle.jdbc.driver.OracleDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据数据库表名生成增删改查各层相关文件
 *
 * @author lb
 */
public class createFileByTableName_dameng {

    private static String driverClassName = "dm.jdbc.driver.DmDriver";//数据库驱动名
    private static String url = "jdbc:dm://127.0.0.1:5236/lishuizzj";//数据库地址
    private static String username = "lishuizzj";//用户名
    private static String password = "lb1234567";//密码

    private static String tableName = "SELF_STATISTICS_MACHINE";//表名
    private static String entityName = "SelfStatisticsMachine";//实体类名
    private static String packageName = "com.insigma";//包名
    private static String filePath = "D:/createFile/";//生成文件路径名
    private static String XMLType = "Mybatis";//Mybatis或者Ibatis或者Hibernate
    private static int idIndex = 0;//表的主键id位于第几列（从0开始）
    private static String packageNameEntity = packageName + "com.insigma.facade.employcode.po";//实体类包名
    private static String packageNameDao = packageName + ".mapper";//包名
    private static String packageNameService = packageName + ".iface";//包名
    private static String packageNameServiceImpl = packageName + ".iface.impl";//包名
    private static String packageNameDaoImpl = packageName + ".dao.impl";//包名
    private static String tableSeq = tableName + "_SEQ";//数据库序列名

    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public static void main(String[] args) {
        createFileByTableName_dameng createFileByTableName = new createFileByTableName_dameng();
        createFileByTableName.createFile();
    }

    /**
     * 创建相关文件
     */
    public void createFile() {
//		JdbcUtil jdbcUtil = new JdbcUtil();
//		con = jdbcUtil.getConnection(con);
        List<TableColumn> list = new ArrayList<TableColumn>();
        String sql = "select t.TABLE_NAME, a.comments, t.COLUMN_NAME, b.comments,t.DATA_TYPE,t.DATA_LENGTH,t.NULLABLE,t.DATA_SCALE from user_tab_columns t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME left join user_col_comments b on t.TABLE_NAME = b.TABLE_NAME and t.COLUMN_NAME = b.column_name where t.TABLE_NAME = ? order by t.TABLE_NAME,t.COLUMN_ID";
        try {
            OracleDriver a = new OracleDriver();
            Class.forName(driverClassName);
            con = DriverManager.getConnection(url, username, password);
            ps = con.prepareStatement(sql);
            ps.setString(1, tableName.toUpperCase());//动态写入表名
            rs = ps.executeQuery();
            while (rs.next()) {
                TableColumn knowledge = new TableColumn();
                knowledge.setTableName(rs.getString(1));//通过列索引取值，列索引从1开始，不是从0开始.一般情况下，使用列索引较为高效
                knowledge.setTableComments(rs.getString(2));
                knowledge.setColumnName(rs.getString(3));
                knowledge.setColumnComments(rs.getString(4));
                knowledge.setDataType(rs.getString(5));
                knowledge.setDataLength(rs.getString(6));
                knowledge.setNullable(rs.getString(7));
                knowledge.setScale(rs.getString(8));
                list.add(knowledge);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
//		createService(list);
//		createServiceImpl(list);
        createDao(list);
        createXml(list);
    }

    /**
     * 创建实体类文件
     *
     * @param list
     */
    private void createEntity(List<TableColumn> list) {
        if (list != null && list.size() > 0) {
            File file = new File(filePath + entityName + ".java");
            try {
                StringBuffer sb = new StringBuffer();
                sb.append("package " + packageNameEntity + ";");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append("import java.io.Serializable;");
                sb.append(System.getProperty("line.separator"));
                sb.append("import lombok.Data;");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append("/**");
                sb.append(System.getProperty("line.separator"));
                sb.append(" *" + list.get(0).getTableComments());
                sb.append(System.getProperty("line.separator"));
                sb.append(" */");
                sb.append(System.getProperty("line.separator"));
                sb.append("@Data");
                sb.append(System.getProperty("line.separator"));
                sb.append("public class " + entityName + " implements Serializable{");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append("    private static final long serialVersionUID = 1L;");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                for (TableColumn tableColumn : list) {
                    //声明变量
					sb.append("    /**");
					sb.append(System.getProperty("line.separator"));
					sb.append("     *" + (tableColumn.getColumnComments()==null?tableColumn.getColumnName().toLowerCase():tableColumn.getColumnComments()));
					sb.append(System.getProperty("line.separator"));
					sb.append("     */");
					sb.append(System.getProperty("line.separator"));
                    sb.append("    private " + getType(tableColumn.getDataType(), false, tableColumn.getScale()) + " " + getName(tableColumn.getColumnName().toLowerCase()) + ";");
                    sb.append(System.getProperty("line.separator"));
                }
//			for (TableColumn tableColumn : list) {
//				//get方法
//				sb.append(System.getProperty("line.separator"));
//				sb.append("public "+getType(tableColumn.getDataType(),false,tableColumn.getScale())+" get"+getName(tableColumn.getColumnName())+"() {");
//				sb.append(System.getProperty("line.separator"));
//				sb.append("    return "+tableColumn.getColumnName().toLowerCase()+";");
//				sb.append(System.getProperty("line.separator"));
//				sb.append("}");
//				sb.append(System.getProperty("line.separator"));
//				//set方法
//				sb.append(System.getProperty("line.separator"));
//				sb.append("public void set"+getName(tableColumn.getColumnName())+"("+getType(tableColumn.getDataType(),false,tableColumn.getScale())+" "+tableColumn.getColumnName().toLowerCase()+") {");
//				sb.append(System.getProperty("line.separator"));
//				sb.append("    this."+tableColumn.getColumnName().toLowerCase()+" = "+tableColumn.getColumnName().toLowerCase()+";");
//				sb.append(System.getProperty("line.separator"));
//				sb.append("}");
//				sb.append(System.getProperty("line.separator"));
//			}
                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建service文件
     *
     * @param list
     */
    private void createService(List<TableColumn> list) {
        if (list != null && list.size() > 0) {
            File file = new File(filePath + entityName + "Service.java");
            try {
                StringBuffer sb = new StringBuffer();
                //包名
                sb.append("package " + packageNameService + ";");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //引入包
                sb.append("import java.util.List;");
                sb.append(System.getProperty("line.separator"));
                sb.append("import " + packageNameEntity + "." + entityName + ";");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append("public interface " + entityName + "Service {");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //增
                sb.append("public int add (" + entityName + " " + entityName.toLowerCase() + ");");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //删
                sb.append("public int remove (Long id);");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //改
                sb.append("public int update (" + entityName + " " + entityName.toLowerCase() + ");");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //查
                sb.append("public List<" + entityName + "> query ();");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));

                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建serviceImpl文件
     *
     * @param list
     */
    private void createServiceImpl(List<TableColumn> list) {
        if (list != null && list.size() > 0) {
            File file = new File(filePath + entityName + "ServiceImpl.java");
            try {
                StringBuffer sb = new StringBuffer();
                //包名
                sb.append("package " + packageNameServiceImpl + ";");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //引入包
                sb.append("import java.util.List;");
                sb.append(System.getProperty("line.separator"));
                sb.append("import " + packageNameEntity + "." + entityName + ";");
                sb.append(System.getProperty("line.separator"));
                sb.append("import " + packageNameService + "." + entityName + "Service;");
                sb.append(System.getProperty("line.separator"));
                sb.append("import " + packageNameDao + "." + entityName + "Dao;");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append("public class " + entityName + "ServiceImpl implements " + entityName + "Service{");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append("private " + entityName + "Dao " + entityName.toLowerCase() + "Dao;");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append("public void set" + entityName + "Dao (" + entityName + "Dao " + entityName.toLowerCase() + "Dao) {");
                sb.append(System.getProperty("line.separator"));
                sb.append("    this." + entityName.toLowerCase() + "Dao = " + entityName.toLowerCase() + "Dao;");
                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                sb.append(System.getProperty("line.separator"));
                //增
                sb.append("public int add (" + entityName + " " + entityName.toLowerCase() + ") {");
                sb.append(System.getProperty("line.separator"));
                sb.append("    return " + entityName.toLowerCase() + "Dao.insert(" + entityName.toLowerCase() + ");");
                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //删
                sb.append("public int remove (Long id) {");
                sb.append(System.getProperty("line.separator"));
                sb.append("    return " + entityName.toLowerCase() + "Dao.delete(id);");
                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //改
                sb.append("public int update (" + entityName + " " + entityName.toLowerCase() + ") {");
                sb.append(System.getProperty("line.separator"));
                sb.append("    return " + entityName.toLowerCase() + "Dao.update(" + entityName.toLowerCase() + ");");
                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //查
                sb.append("public List<" + entityName + "> query () {");
                sb.append(System.getProperty("line.separator"));
                sb.append("    return " + entityName.toLowerCase() + "Dao.query();");
                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));

                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建Dao文件
     *
     * @param list
     */
    private void createDao(List<TableColumn> list) {
        if (list != null && list.size() > 0) {
            File file = new File(filePath + entityName + "Mapper.java");
            try {
                StringBuffer sb = new StringBuffer();
                //包名
                sb.append("package " + packageNameDao + ";");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //引入包
                sb.append("import java.util.List;");
                sb.append(System.getProperty("line.separator"));
                sb.append("import " + packageNameEntity + "." + entityName + ";");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append("@Repository");
                sb.append(System.getProperty("line.separator"));
                sb.append("public interface " + entityName + "Mapper {");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //增
                sb.append("    int insert (" + entityName + " " + entityName.toLowerCase() + ");");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //删
                sb.append("    int delete (Long id);");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //改
                sb.append("    int update (" + entityName + " " + entityName.toLowerCase() + ");");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                //查
                sb.append("    List<" + entityName + "> query ();");
                sb.append(System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));

                sb.append(System.getProperty("line.separator"));
                sb.append("}");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建Mybatis或者Ibatis或者Hibernate的XML文件
     *
     * @param list
     */
    private void createXml(List<TableColumn> list) {
        if ("Mybatis".equals(XMLType)) {
            if (list != null && list.size() > 0) {
                File file = new File(filePath + entityName + "Dao.xml");
                try {
                    StringBuffer sb = new StringBuffer();
                    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<mapper namespace=\"" + packageNameDao + "." + entityName + "Dao\">");
                    sb.append(System.getProperty("line.separator"));
                    //增
                    sb.append("<insert id=\"insert\" parameterType=\"map\" >");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<![CDATA[");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("insert into " + tableName + " (");
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == (list.size() - 1)) {
                            sb.append(tableColumn.getColumnName() + ") values ");
                        } else {
                            sb.append(tableColumn.getColumnName() + ",");
                        }
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append("(");
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == (list.size() - 1)) {
                            if (list.indexOf(tableColumn) == idIndex) {
                                sb.append(tableSeq + ".nextval)");
                            } else {
                                sb.append("#{" + tableColumn.getColumnName().toLowerCase() + "})");
                            }
                        } else {
                            if (list.indexOf(tableColumn) == idIndex) {
                                sb.append(tableSeq + ".nextval,");
                            } else {
                                sb.append("#{" + tableColumn.getColumnName().toLowerCase() + "},");
                            }
                        }
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append("]]>	");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</insert>");
                    sb.append(System.getProperty("line.separator"));
                    //删
                    sb.append("<delete id=\"delete\" parameterType=\"long\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<![CDATA[");
                    sb.append(System.getProperty("line.separator"));
                    sb.append(" delete from " + tableName + " where id =#{id}");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("]]>	");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</delete>");
                    sb.append(System.getProperty("line.separator"));
                    //改
                    sb.append("<update id=\"update\" parameterType=\"map\" >");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<![CDATA[");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("update " + tableName + " set ");
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == (list.size() - 1)) {
                            sb.append(tableColumn.getColumnName() + " = #{" + tableColumn.getColumnName() + "}");
                        } else {
                            sb.append(tableColumn.getColumnName() + " = #{" + tableColumn.getColumnName() + "},");
                        }
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append(" where id =#{id} ]]>	");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</update>");
                    sb.append(System.getProperty("line.separator"));
                    //查
                    sb.append("<select id=\"search\" parameterType=\"map\" resultType=\"" + entityName + "\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<![CDATA[");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("select ");
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == (list.size() - 1)) {
                            sb.append(tableColumn.getColumnName());
                        } else {
                            sb.append(tableColumn.getColumnName() + ",");
                        }
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append(" from " + tableName);
                    sb.append(System.getProperty("line.separator"));
                    sb.append("]]>	");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</select>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</mapper>");
                    sb.append(System.getProperty("line.separator"));
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(sb.toString().getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("Ibatis".equals(XMLType)) {
            if (list != null && list.size() > 0) {
                File file = new File(filePath + entityName + "Dao.xml");
                try {
                    StringBuffer sb = new StringBuffer();
                    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<sqlMap namespace=\"" + packageNameDao + "." + entityName + "Dao\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<typeAlias alias=" + entityName + " type=" + packageNameEntity + "." + entityName + " />");
                    sb.append(System.getProperty("line.separator"));
                    //增
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<insert id=\"insert\" parameterClass=\"" + entityName + "\" >");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("    insert into " + tableName + " (");
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == (list.size() - 1)) {
                            sb.append(tableColumn.getColumnName() + ") values ");
                        } else {
                            sb.append(tableColumn.getColumnName() + ",");
                        }
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append("    (");
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == (list.size() - 1)) {
                            if (list.indexOf(tableColumn) == idIndex) {
                                sb.append(tableSeq + ".nextval)");
                            } else {
                                sb.append("#" + tableColumn.getColumnName().toLowerCase() + "#)");
                            }
                        } else {
                            if (list.indexOf(tableColumn) == idIndex) {
                                sb.append(tableSeq + ".nextval,");
                            } else {
                                sb.append("#" + tableColumn.getColumnName().toLowerCase() + "#,");
                            }
                        }
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</insert>");
                    sb.append(System.getProperty("line.separator"));
                    //删
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<delete id=\"delete\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("    delete from " + tableName);
                    sb.append(System.getProperty("line.separator"));
                    sb.append("  <dynamic prepend=\"where\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("    <isNotNull>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("      ID IN");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("      <iterate conjunction=\",\" open=\"(\" close=\")\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("      #[]#");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("      </iterate>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("    </isNotNull>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("    <isNull>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("      1 = 0");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("    </isNull>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("  </dynamic>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</delete>");
                    sb.append(System.getProperty("line.separator"));
                    //改
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<update id=\"update\" parameterClass=\"map\" >");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("  update " + tableName + " set ");
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == (list.size() - 1)) {
                            sb.append(tableColumn.getColumnName() + " = #" + tableColumn.getColumnName() + "#");
                        } else {
                            sb.append(tableColumn.getColumnName() + " = #" + tableColumn.getColumnName() + "#,");
                        }
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append(" where id =#id#	");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</update>");
                    sb.append(System.getProperty("line.separator"));
                    //查
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<select id=\"search\" parameterClass=\"map\" resultType=\"" + entityName + "\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("  select ");
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == (list.size() - 1)) {
                            sb.append(tableColumn.getColumnName());
                        } else {
                            sb.append(tableColumn.getColumnName() + ",");
                        }
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append(" from " + tableName);
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</select>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</sqlMap>");
                    sb.append(System.getProperty("line.separator"));
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(sb.toString().getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("Hibernate".equals(XMLType)) {
            if (list != null && list.size() > 0) {
                File file = new File(filePath + entityName + ".hbm.xml");
                try {
                    StringBuffer sb = new StringBuffer();
                    sb.append("<?xml version=\"1.0\"?>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD//EN\" \"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("<hibernate-mapping package=\"" + packageNameEntity + "\">");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("  <class name=\"" + entityName + "\" table=\"" + tableName + "\">");
                    sb.append(System.getProperty("line.separator"));
                    for (TableColumn tableColumn : list) {
                        if (list.indexOf(tableColumn) == idIndex) {
                            sb.append("    <id name=\"" + tableColumn.getColumnName().toLowerCase() + "\" type=\"" + getType(tableColumn.getDataType(), true, tableColumn.getScale()) + "\" column=\"" + tableColumn.getColumnName() + "\">");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("      <generator class=\"native\"><param name=\"sequence\">" + tableSeq + "</param></generator>");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("    </id>");
                            sb.append(System.getProperty("line.separator"));
                        } else {
                            if ("N".equals(tableColumn.getNullable())) {
                                sb.append("    <property name=\"" + tableColumn.getColumnName().toLowerCase() + "\" type=\"" + getType(tableColumn.getDataType(), true, tableColumn.getScale()) + "\"><column name=\"" + tableColumn.getColumnName() + "\" not-null=\"true\"  /></property>");
                            } else {
                                sb.append("    <property name=\"" + tableColumn.getColumnName().toLowerCase() + "\" type=\"" + getType(tableColumn.getDataType(), true, tableColumn.getScale()) + "\"><column name=\"" + tableColumn.getColumnName() + "\"  /></property>");
                            }
                            sb.append(System.getProperty("line.separator"));
                        }
                    }
                    sb.append("  </class>");
                    sb.append(System.getProperty("line.separator"));
                    sb.append("</hibernate-mapping>");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(sb.toString().getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取数据库类型对应的java类型
     *
     * @param type,isPacName(返回值是否带包名)
     * @return
     */
    public String getType(String type, boolean isPacName, String scale) {
        if (type != null) {
            type = type.toUpperCase();
            if ("VARCHAR2".equals(type) || "CHAR".equals(type) || "VARCHAR".equals(type)) {
                if (isPacName) {
                    return "java.lang.String";
                } else {
                    return "String";
                }
            } else if ("NUMBER".equals(type) || "BIGINT".equals(type)) {
                if (scale != null && scale.length() > 0 && !scale.equals("0")) {
                    if (isPacName) {
                        return "java.lang.Double";
                    } else {
                        return "Double";
                    }
                } else {
                    if (isPacName) {
                        return "java.lang.Long";
                    } else {
                        return "Long";
                    }
                }
            } else if ("DATE".equals(type)) {
                return "java.util.Date";
            } else if ("BLOB".equals(type)) {
                if (isPacName) {
                    return "byte[]";
                } else {
                    return "byte[]";
                }
            } else if ("CLOB".equals(type)) {
                if (isPacName) {
                    return "java.lang.String";
                } else {
                    return "String";
                }
            } else if (type.startsWith("TIMESTAMP")) {
                if (isPacName) {
                    return "java.util.Date";
                } else {
                    return "Date";
                }
            } else if ("INT".equals(type)) {
                if (isPacName) {
                    return "java.lang.Integer";
                } else {
                    return "Integer";
                }
            }else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 获取表和字段对应的实体变量名（首字母大写）
     *
     * @param name
     */
    public String getName(String name) {
        if (name != null && name.length() > 0) {
            if (name.indexOf("_")>0) {
                return name.substring(0,name.indexOf("_"))+name.substring(name.indexOf("_")+1,name.indexOf("_")+2).toUpperCase()+name.substring(name.indexOf("_")+2);
            } else {
                return name;
            }
        } else {
            return "";
        }
    }

}
