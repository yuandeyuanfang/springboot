package com.example.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.StaffSalaryEntity;
import com.example.demo.utils.ExcelUtils;
import com.example.demo.vo.ExcelVO;
import com.example.demo.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "Excel处理")
public class ExcelController {
    private static final Logger log = LogManager.getLogger(ExcelController.class);

    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response)  throws IOException {
        List<ExcelVO> resultList = new ArrayList<ExcelVO>();
        ExcelVO busClick = new ExcelVO();
        busClick.setCityCode("a1");
        busClick.setClientVer("a2");
        busClick.setDate("a3");
        busClick.setMarkId("a4");
        busClick.setToaluv("a5");
        resultList.add(busClick);
        busClick = new ExcelVO();
        busClick.setCityCode("b1");
        busClick.setClientVer("b2");
        busClick.setDate("b3");
        busClick.setMarkId("b4");
        busClick.setToaluv("b5");
        resultList.add(busClick);

        long t1 = System.currentTimeMillis();
        ExcelUtils.writeExcel(response, resultList, ExcelVO.class, "excel.xlsx");
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
    }

    @RequestMapping(value = "/easyExcelExport", method = RequestMethod.GET)
    @ApiOperation("easyExcel按模板导出")
    public JSONObject easyExcelExport(HttpServletResponse response) throws IOException {
        salaryList(response);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @RequestMapping(value = "/readExcel", method = RequestMethod.POST)
    public JSONObject readExcel(@RequestParam(value="uploadFile", required = false) MultipartFile file){
        long t1 = System.currentTimeMillis();
        List<ExcelVO> list = ExcelUtils.readExcel("", ExcelVO.class, file);
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("read over! cost:%sms", (t2 - t1)));
        list.forEach(
                b -> System.out.println(JSON.toJSONString(b))
        );
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    /**
     * excel模板文件下载
     * @param response
     */
    @RequestMapping(value = "/getTemplate", method = RequestMethod.GET)
    public void getTemplate(HttpServletResponse response) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mobileTemplate.xls");
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String diskfilename = "ExcelTemplate.xls";
//            response.setContentType("video/avi");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"");
//            System.out.println("data.length " + data.length);
            response.setContentLength(data.length);
//            response.setHeader("Content-Range", "" + Integer.valueOf(data.length - 1));
//            response.setHeader("Accept-Ranges", "bytes");
//            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            OutputStream os = response.getOutputStream();

            os.write(data);
            //先声明的流后关掉！
            os.flush();
            os.close();
            inputStream.close();

        } catch (Exception e) {
            log.error("getTemplate",e);
        }
    }

    /**
     * Excel文件导入
     */
    @RequestMapping(value = "import")
    public ResultVO importExcel(@RequestParam("file") MultipartFile file) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        try {
            //参数检验
            if (file == null || file.isEmpty() ) {
                resultVO.setResultDes("文件不能为空！");
                resultVO.setSuccess(false);
            }
            //业务处理
            if (resultVO.isSuccess()) {
                //解析excel
//                List<SelfSmsTaskInfo> selfSmsTaskInfoList = new ArrayList<>();
                boolean isImpxlsx = false;
                Workbook workbookx = new HSSFWorkbook();
//                Workbook workbookx = null;
                try {
                    //尝试以xls格式解析
                    workbookx = new HSSFWorkbook(file.getInputStream());
                } catch (Exception e) {
                    isImpxlsx=true;
                }
                if(isImpxlsx){
                    try {
                        //以xlsx格式解析
                        workbookx = new XSSFWorkbook(file.getInputStream());
                    } catch (Exception e) {
                        log.error("workbookxError",e);
                    }
                }

                Sheet sheet = workbookx.getSheetAt(0);
                int rowCount = sheet.getPhysicalNumberOfRows();//excel行数
                if (rowCount > 1) {
                    for (int r = 1; r < rowCount; r++) {//循环行
                        Row row = sheet.getRow(r);
                        String peopleCode =  ExcelUtils.getCellValueFormula(row.getCell(0));
                        String peopleName =  ExcelUtils.getCellValueFormula(row.getCell(1));
                        String mobile =  ExcelUtils.getCellValueFormula(row.getCell(2));
//                        if(StringUtils.isNotBlank(mobile) && mobile.length()==11 && StringUtils.isNotBlank(smsContent) && smsContent.length() > 10){
//                            SelfSmsTaskInfo selfSmsTaskInfo = new SelfSmsTaskInfo();
//                            selfSmsTaskInfo.setAreaCode(String.valueOf(selfSmsTask.getAreaCode()));
//                            selfSmsTaskInfo.setPeopleCode(peopleCode);
//                            selfSmsTaskInfo.setPeopleName(peopleName);
//                            selfSmsTaskInfo.setMobile(mobile);
//                            selfSmsTaskInfoList.add(selfSmsTaskInfo);
//                        }

                    }
                }else{
                    resultVO.setResultDes("excel解析数据为空");
                    resultVO.setSuccess(false);
                }

//                if(selfSmsTaskInfoList.size() == 0){
//                    resultVO.setResultDes("excel解析数据为空");
//                    resultVO.setSuccess(false);
//                }

//                if (resultVO.isSuccess()) {
//                    //组装任务对象
//                    selfSmsTask.setSendingState(-1);
//                    selfSmsTask.setNumberPeople(String.valueOf(selfSmsTaskInfoList.size()));
//
//                    //保存任务表
//                    selfSmsTask.setSendingType(0);
//                    selfSmsTask.setAddType("IMPORT");
//                    saveSelfSmsTask(selfSmsTask);
//                    //保存任务短信表
//                    saveSelfSmsTaskInfo(selfSmsTaskInfoList,selfSmsTask);
//                    resultVO.setResult(selfSmsTask);
//                    resultVO.setResultDes("导入成功，本次共导入"+selfSmsTaskInfoList.size()+"条数据，请仔细核对后保存");
//                }

            }
        } catch (Exception e) {
            resultVO.setResultDes(e.getMessage());
            resultVO.setSuccess(false);
            log.error("[importExcelError]", e);
        }
        return resultVO;
    }

    public static void main(String[] args) {

    }
    /**
     * 案例一：工资表
     */
    @Test
    public void salaryList(HttpServletResponse response) throws IOException {
        // 模板文件路径
        String templateFilePath = "D:\\gzb.xlsx";
        // 输出文件路径
        String outFilePath = "D:\\gzb2.xlsx";

        // 创建 ExcelWriter 实例
        ExcelWriter writer = EasyExcel
                // 写入到
                .write(outFilePath)
                // 指定模板
                .withTemplate(templateFilePath)
                .build();

        WriteSheet sheet = EasyExcel.writerSheet().build();

        // 获取员工工资数据
        List<StaffSalaryEntity> staffSalaryEntities = getStaffSalaryEntities();

        FillConfig fillConfig = FillConfig.builder()
                // 开启填充换行
                .forceNewRow(true)
                .build();

        // 执行填充操作
        writer.fill(staffSalaryEntities, fillConfig, sheet);

        // 结束
        writer.finish();

        OutputStream os = response.getOutputStream();
        InputStream inputStream = new FileInputStream(outFilePath);
        byte[] data = new byte[inputStream.available()];
        String diskfilename = "ExcelTemplate.xls";
//            response.setContentType("video/avi");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"");
        response.setContentLength(data.length);

        inputStream.read(data);
        os.write(data);
        //先声明的流后关掉！
        os.flush();
        os.close();
        inputStream.close();
    }

    public List<StaffSalaryEntity> getStaffSalaryEntities() {
        List<StaffSalaryEntity> list = new ArrayList<>();

        list.add(StaffSalaryEntity.builder()
                .name("米大傻")
                .post("开发")
                .mouthSalary(new BigDecimal(1320))
                .hourSalary(new BigDecimal("7.59"))
                .shouldAttend(21.0)
                .actualAttend(21.0)
                .overtime(21.0)
                .weekOvertime(8.0)
                .holiday(0.0)
                .normalSalary(new BigDecimal(1320))
                .overtimeSalary(new BigDecimal("238.97"))
                .weekOvertimeSalary(new BigDecimal("242.76"))
                .holidaySalary(new BigDecimal(0))
                .postSubsidy(new BigDecimal(0))
                .award(new BigDecimal(20))
                .deduction(new BigDecimal(0))
                .social(new BigDecimal("113.6"))
                .shouldSalary(new BigDecimal("1688.12"))
                .selfTax(new BigDecimal(0))
                .actualSalary(new BigDecimal("1688.1"))
                .build());


        list.add(StaffSalaryEntity.builder()
                .name("曹大力")
                .post("店长")
                .mouthSalary(new BigDecimal(13200))
                .hourSalary(new BigDecimal("7.59"))
                .shouldAttend(21.0)
                .actualAttend(21.0)
                .overtime(21.0)
                .weekOvertime(8.0)
                .holiday(0.0)
                .normalSalary(new BigDecimal(1320))
                .overtimeSalary(new BigDecimal("238.97"))
                .weekOvertimeSalary(new BigDecimal("242.76"))
                .holidaySalary(new BigDecimal(0))
                .postSubsidy(new BigDecimal(0))
                .award(new BigDecimal(20))
                .deduction(new BigDecimal(0))
                .social(new BigDecimal("113.6"))
                .shouldSalary(new BigDecimal("13200.12"))
                .selfTax(new BigDecimal(0))
                .actualSalary(new BigDecimal("13200.1"))
                .build());

        list.add(StaffSalaryEntity.builder()
                .name("张大仙")
                .post("经理")
                .mouthSalary(new BigDecimal(13200))
                .hourSalary(new BigDecimal("7.59"))
                .shouldAttend(21.0)
                .actualAttend(21.0)
                .overtime(21.0)
                .weekOvertime(8.0)
                .holiday(0.0)
                .normalSalary(new BigDecimal(1320))
                .overtimeSalary(new BigDecimal("238.97"))
                .weekOvertimeSalary(new BigDecimal("242.76"))
                .holidaySalary(new BigDecimal(0))
                .postSubsidy(new BigDecimal(0))
                .deduction(new BigDecimal(0))
                .social(new BigDecimal("113.6"))
                .shouldSalary(new BigDecimal("13200.12"))
                .selfTax(new BigDecimal(0))
                .actualSalary(new BigDecimal("13200.1"))
                .build());

        return list;
    }

}