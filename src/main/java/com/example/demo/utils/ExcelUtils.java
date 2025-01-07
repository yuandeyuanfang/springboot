package com.example.demo.utils;

import com.example.demo.annotation.ExcelColumn;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ExcelUtils {

    private final static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";

    public static <T> List<T> readExcel(String path, Class<T> cls, MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }
        List<T> dataList = new ArrayList<>();
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2007)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2003)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new HSSFWorkbook(is);
            }
            if (workbook != null) {
                //类映射  注解 value-->bean columns
                Map<String, List<Field>> classMap = new HashMap<>();
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                fields.forEach(
                        field -> {
                            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                            if (annotation != null) {
                                String value = annotation.value();
                                if (StringUtils.isBlank(value)) {
                                    return;//return起到的作用和continue是相同的 语法
                                }
                                if (!classMap.containsKey(value)) {
                                    classMap.put(value, new ArrayList<>());
                                }
                                field.setAccessible(true);
                                classMap.get(value).add(field);
                            }
                        }
                );
                //索引-->columns
                Map<Integer, List<Field>> reflectionMap = new HashMap<>(16);
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);

                boolean firstRow = true;
                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    //首行  提取注解
                    if (firstRow) {
                        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            if (classMap.containsKey(cellValue)) {
                                reflectionMap.put(j, classMap.get(cellValue));
                            }
                        }
                        firstRow = false;
                    } else {
                        //忽略空白行
                        if (row == null) {
                            continue;
                        }
                        try {
                            T t = cls.newInstance();
                            //判断是否为空白行
                            boolean allBlank = true;
                            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                if (reflectionMap.containsKey(j)) {
                                    Cell cell = row.getCell(j);
                                    String cellValue = getCellValue(cell);
                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }
                                    List<Field> fieldList = reflectionMap.get(j);
                                    fieldList.forEach(
                                            x -> {
                                                try {
                                                    handleField(t, cellValue, x);
                                                } catch (Exception e) {
                                                    log.error(String.format("reflect field:%s value:%s exception!", x.getName(), cellValue), e);
                                                }
                                            }
                                    );
                                }
                            }
                            if (!allBlank) {
                                dataList.add(t);
                            } else {
                                log.warn(String.format("row:%s is blank ignore!", i));
                            }
                        } catch (Exception e) {
                            log.error(String.format("parse row:%s exception!", i), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(String.format("parse excel exception!"), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error(String.format("parse excel exception!"), e);
                }
            }
        }
        return dataList;
    }

    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
            //数字类型
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            //
            field.set(t, value);
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
                return new BigDecimal(cell.getNumericCellValue()).toString();
            }
        } else if (cell.getCellType() == CellType.STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }

    }

    public static <T> void writeExcel(HttpServletResponse response, List<T> dataList, Class<T> cls, String fileName){
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.toList());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            //写入头部
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);

                Font font = wb.createFont();
//                font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellValue(value.toString());
                    }
                });
            });
        }
        //冻结窗格
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        //浏览器下载excel
        buildExcelDocument(fileName,wb,response);
        //生成excel文件
//        buildExcelFile(".\\default.xlsx",wb);
    }

    /**
     * 浏览器下载excel
     * @param fileName
     * @param wb
     * @param response
     */

    private static  void  buildExcelDocument(String fileName, Workbook wb, HttpServletResponse response){
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成excel文件
     * @param path 生成excel路径
     * @param wb
     */
    private static  void  buildExcelFile(String path, Workbook wb){

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            wb.write(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析单元格内容（支持表达式）
     * @param cell
     * @return
     */
    public static String getCellValueFormula(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
                String cellstr = cell.toString();
                cell.setCellType(CellType.STRING);
                String cellValue = cell.getStringCellValue();
                if (cellValue.indexOf(".") > -1) {
                    cellValue = cellstr;
                }
                return cellValue;
            }
        } else if (cell.getCellType() == CellType.STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            try {
                return StringUtils.trimToEmpty(String.valueOf(cell.getNumericCellValue()));
            } catch (IllegalStateException e) {
                return StringUtils.trimToEmpty(String.valueOf(cell.getRichStringCellValue()));
            }
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }

    }

    /**
     * 新建�?个空白页
     * @param wb
     * @param sheet
     * @param row 行数
     * @param col 列数
     * @param colSplit 锁定表头�?
     * @param rowSplit 锁定表头�?
     * @param hideRow 无边框行�?
     */
    public static void createSheet(HSSFWorkbook wb, HSSFSheet sheet, int row,
                                   int col, int colSplit, int rowSplit,int hideRow,int height) {
        sheet.createFreezePane(colSplit, rowSplit);// 锁定表头
        //设置样式
        HSSFCellStyle style11_1=ExcelUtils.setStyleBorder(wb,(short)0x2, (short)0x1,true);
        style11_1.setFont(ExcelUtils.setFont(wb, false, 11));
        for (int i = 0; i < row; i++) {
            HSSFRow r = sheet.createRow(i);
            setHeight(sheet, i, height);
            for (int j = 0; j < col; j++) {
                HSSFCell cell = r.createCell(j);
                cell.setCellStyle(style11_1);
                cell.setCellType(CellType.STRING);
                if (i >= hideRow) {
                    setBorder(wb, sheet, new CellRangeAddress(i, i, j, j));
                }
            }
        }
    }

    /**
     * 设置单元格边框线
     * @param wb
     * @param s
     * @param row
     */
    public static void setBorder(HSSFWorkbook wb,HSSFSheet s,CellRangeAddress row){
//        RegionUtil.setBorderBottom(1, row, s,wb); // 下边�?
//        RegionUtil.setBorderLeft(1, row, s,wb); // 左边�?
//        RegionUtil.setBorderRight(1, row, s,wb); // 有边�?
//        RegionUtil.setBorderTop(1, row, s,wb); // 上边�?
    }
    /**
     * 设置列宽
     * 列宽基数�?0.72
     * @param s
     * @param columnIndex 第几�?
     * @param width 列宽
     */
    public static void setColumnWidth(HSSFSheet s,int columnIndex, int width){
        s.setColumnWidth(columnIndex,  (int)((width + 0.72) * 256));
    }

    /**
     * 设置行高
     * @param s
     * @param row 第几�?
     * @param height 高度
     */
    public static void setHeight(HSSFSheet s,int row,int height){
        s.getRow(row).setHeight((short)  (20*height));
    }

    /**
     * 合并单元�?
     */
    public static void mergeRowCol(HSSFSheet sheet,CellRangeAddress cra){
//		CellRangeAddress cra= new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(cra);
//		setBorder(wb,sheet,cra);
    }

    /**
     * 设置字体样式
     * @param wb
     * @param boldweight 字体是否加粗
     * @param fontHeightInPoints 字体大小
     * @return
     */
    public static HSSFFont setFont(HSSFWorkbook wb,boolean boldweight,int fontHeightInPoints){
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        if(boldweight){
//            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        }
        font.setFontHeightInPoints((short) fontHeightInPoints);
        return font;
    }

    /**
     * 设置单元格样�?
     * @param wb
     * @param alignment 文本位置 0x2 居中 �?0x1 居左
     * @param verticalAlignment 水平位置 0x0 居顶 �?0x1 居中对齐
     * @return
     */
    public static HSSFCellStyle setStyleBorder(HSSFWorkbook wb,short alignment,short verticalAlignment,boolean flag) {
        //ALIGN_CENTER =
        //ALIGN_LEFT =
        //VERTICAL_TOP =
        //VERTICAL_CENTER =

        // 带上下左右边界�?�居中�?�自动换�?
        HSSFCellStyle style = wb.createCellStyle();
        if(flag){
//            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        }
//        style.setAlignment((short)alignment);// 设置居中对齐;
//        style.setVerticalAlignment((short)verticalAlignment);// 设置垂直对齐的样式为居中对齐;
        style.setWrapText(true);// 自动换行
        return style;
    }

    public static HSSFRichTextString setRichText(HSSFFont font, String content){
        if(content.endsWith("(0)")){
            content=content.replace("(0)", "");
            HSSFRichTextString richString = new HSSFRichTextString(content);
            return richString;
        }else{
            HSSFRichTextString richString = new HSSFRichTextString(content);
            int n = content.indexOf("(");
            richString.applyFont(n,content.length() , font);
            return richString;
        }
    }

}