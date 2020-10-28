package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.ExcelUtils;
import com.example.demo.vo.ExcelVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
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

}