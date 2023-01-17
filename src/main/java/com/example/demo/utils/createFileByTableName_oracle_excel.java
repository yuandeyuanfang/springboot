package com.example.demo.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据数据库表名生成增删改查各层相关文件
 *
 * @author lb
 */
public class createFileByTableName_oracle_excel {

    private static String driverClassName = "oracle.jdbc.driver.OracleDriver";//数据库驱动名
    private static String url = "jdbc:oracle:thin:@192.168.206.237:1521:xe";//数据库地址
    private static String username = "ls58";//用户名
    private static String password = "Lishui#2022";//密码

    private static String filePath = "D:/createFile/";//生成文件路径名

    public static void main(String[] args) {
        Connection connection=getConnection();
        List<TableVo> tlist = getAllTable(connection);
        List<TableVo> list = getTableColumn(connection,tlist);
        setHSSFSheet2(list);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<TableVo> getTableColumn(Connection connection,List<TableVo> tlist){

        try {
            for (int i = 0; i < tlist.size(); i++) {
                StringBuffer sql = new StringBuffer();
                sql.append("select id, name,descrip, reftype, length,decode(nullable, 'Y', '是', 'N', '否') nullable from ( ");
                sql.append(" select distinct A.COLUMN_ID as id,A.column_name name,A.data_type reftype,A.data_length as length,A.nullable,B.comments descrip ");
                sql.append(" from user_tab_columns A  ");
                sql.append(" left join user_col_comments B on B.Table_Name = A.Table_Name AND B.column_name=A.COLUMN_NAME ");
                sql.append(" left join all_cons_columns C on C.Table_Name = A.Table_Name ");
                sql.append(" where A.Table_Name='"+tlist.get(i).getTableName()+"' ORDER BY id ) aa");
                System.out.println(sql.toString());
                Statement stmt= connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql.toString());
                List<ColumnsVo> list = new ArrayList<ColumnsVo>();
                while (rs.next()) {
                    ColumnsVo vo = new ColumnsVo();
                    vo.setColumnName(rs.getString("name"));
                    vo.setColumnExplain(rs.getString("descrip"));
                    vo.setColumnType(rs.getString("reftype"));
                    vo.setLength(rs.getString("length"));
                    vo.setNullable(rs.getString("nullable"));
                    list.add(vo);
                }
                tlist.get(i).setClist(list);
                rs.close();
                stmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tlist;
    }

    public static List<TableVo> getAllTable(Connection connection){
        List<TableVo> list = new ArrayList<TableVo>();
        try {
            //where a.Table_Name ='SYS_USER'
            StringBuffer sql1 = new StringBuffer();
            sql1.append("select t.TABLE_NAME,a.comments from user_tables t left join user_tab_comments a on t.TABLE_NAME = a.TABLE_NAME");
            sql1.append(" where  a.comments is not null ");
            Statement stmt = connection.createStatement();
            System.out.println(sql1);
            ResultSet rs = stmt.executeQuery(sql1.toString());
            while (rs.next()) {
                TableVo vo = new TableVo();
                vo.setTableName(rs.getString("TABLE_NAME"));
                vo.setTableExplain(rs.getString("comments"));
//                vo.setPk(rs.getString("column_name"));
                list.add(vo);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName(driverClassName);
            connection=DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * �ڶ�������ҳ
     */
    public static void setHSSFSheet2(List<TableVo> list){
        int coloumnSize = 0;
        for (TableVo tableVo:list) {
            coloumnSize = coloumnSize + tableVo.getClist().size() + 5;
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet s = wb.createSheet();
        wb.setSheetName(0, "表结构");
        ExcelUtils.createSheet(wb, s, coloumnSize, 5, 0, 0, 0,20);

        //�����п� 14��
        for (int i = 0; i < 5; i++) {
            ExcelUtils.setColumnWidth(s,i,15);
        }

        //������ʽ
        HSSFCellStyle style11=ExcelUtils.setStyleBorder(wb,(short)0x1, (short)0x1,true);
        style11.setFont(ExcelUtils.setFont(wb, false, 11));
        CellRangeAddress cra = null;
        int indexRow = 0;
        for (int i = 0; i < list.size(); i++) {
            String tableName=list.get(i).getTableName();
            System.out.println(list.get(i).getTableName()+"----"+list.get(i).getTableExplain());
            if(list.get(i).getTableExplain()!=null){
                tableName=list.get(i).getTableExplain()+"("+list.get(i).getTableName()+")";
            }
            s.getRow(indexRow).getCell(0).setCellValue("表名");
            s.getRow(indexRow).getCell(1).setCellValue(tableName);
            cra=new CellRangeAddress(indexRow, indexRow, 1, 4);
            s.addMergedRegion(cra);

            s.getRow(indexRow+1).getCell(0).setCellValue("功能描述");
            s.getRow(indexRow+1).getCell(1).setCellValue(list.get(i).getTableExplain());
            cra=new CellRangeAddress(indexRow+1, indexRow+1, 1, 4);
            s.addMergedRegion(cra);

            s.getRow(indexRow+2).getCell(0).setCellValue("字段说明");
            cra=new CellRangeAddress(indexRow+2, indexRow+list.get(i).getClist().size()+2, 0,0);
            s.addMergedRegion(cra);


            String[] col = {"字段名","说明","类型","可空"};
            for (int k = 0; k < col.length; k++) {
                s.getRow(indexRow+2).getCell(k+1).setCellValue(col[k]);
            }
            List<ColumnsVo> clist = list.get(i).getClist();
            for (int k = 0; k < clist.size(); k++) {

                HSSFRow r = s.getRow(indexRow+3+k);
                r.getCell(1).setCellStyle(style11);
                r.getCell(2).setCellStyle(style11);
                r.getCell(3).setCellStyle(style11);
                r.getCell(4).setCellStyle(style11);

                r.getCell(1).setCellValue(clist.get(k).getColumnName());
                r.getCell(2).setCellValue(clist.get(k).getColumnExplain());
                String type = clist.get(k).getColumnType();
                if(!"".equals(clist.get(k).getLength())){
                    type=type+"("+clist.get(k).getLength()+")";
                }
                r.getCell(3).setCellValue(type);
                r.getCell(4).setCellValue(clist.get(k).getNullable());
                System.out.println(indexRow+3+k);
            }


//            s.getRow(indexRow+list.get(i).getClist().size()+3).getCell(0).setCellValue("主键");
//            s.getRow(indexRow+list.get(i).getClist().size()+3).getCell(1).setCellValue(list.get(i).getPk());
//            cra=new CellRangeAddress(indexRow+list.get(i).getClist().size()+3, indexRow+list.get(i).getClist().size()+3, 1, 5);
//            s.addMergedRegion(cra);
//
//            s.getRow(indexRow+list.get(i).getClist().size()+4).getCell(0).setCellValue("索引");
//            cra=new CellRangeAddress(indexRow+list.get(i).getClist().size()+4, indexRow+list.get(i).getClist().size()+4, 1, 5);
//            s.addMergedRegion(cra);

            indexRow+=list.get(i).getClist().size()+5;
            HSSFRow r1 = s.getRow(indexRow-2);
            r1.getCell(0).setCellStyle(null);
            r1.getCell(1).setCellStyle(null);
            r1.getCell(2).setCellStyle(null);
            r1.getCell(3).setCellStyle(null);
            r1.getCell(4).setCellStyle(null);
            HSSFRow r2 = s.getRow(indexRow-1);
            r2.getCell(0).setCellStyle(null);
            r2.getCell(1).setCellStyle(null);
            r2.getCell(2).setCellStyle(null);
            r2.getCell(3).setCellStyle(null);
            r2.getCell(4).setCellStyle(null);
        }

        FileOutputStream fos = null;
        filePath=filePath+"excel.xls";
        try {
            File excelFile = new File(filePath);
            excelFile.delete();
            // �ж��ļ�·���Ƿ����
            if (!excelFile.exists()) {
                excelFile.createNewFile();
            }
            fos = new FileOutputStream(excelFile);
            wb.write(fos);
            fos.close();
            long end = System.currentTimeMillis();
            System.out.println("����excel���"+end);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            // excelFile.delete();�Դ������ļ�����ɾ��
        }
    }

}
