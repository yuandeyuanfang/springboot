package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    DataService dataService;

    @RequestMapping(value = "test")
    public JSONObject test() {
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @RequestMapping(value = "testData")
    public JSONObject testData() {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            result.put("result", dataService.testData());
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping(value = "testSql")
    public JSONObject testSql(@RequestBody String sql) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            if("sql".equals(sql.substring(0,3))){
                result.put("result", dataService.testSql(sql.substring(3,sql.length())));
            }
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

}
