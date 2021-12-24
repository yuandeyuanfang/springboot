package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.DataService;
import com.example.demo.service.RedisUtilService;
import com.example.demo.vo.ParamInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class TestController {
    @Autowired
    DataService dataService;
    @Autowired
    RedisUtilService redisUtilService;

    /**
     * 测试项目后端状态
     * @return
     */
    @RequestMapping(value = "test")
    public JSONObject test() {
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    /**
     * 测试项目后端和数据库状态
     * @return
     */
    @RequestMapping(value = "testData")
    public JSONObject testData() {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            result.put("result", dataService.selectParamInfoPage(new ParamInfoVO(),0,2));
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

    /**
     * 执行测试sql
     * @return
     */
    @RequestMapping(value = "testSql")
    public JSONObject testSql(String sql) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            if("sql".equals(sql.substring(0,3))){
                result.put("result", dataService.testSql(sql.substring(3)));
            }
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

    /**
     * 测试Redis读取
     * @return
     */
    @RequestMapping(value = "testRedis")
    public JSONObject testRedis() {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            redisUtilService.set("testKey","testValue");
            redisUtilService.set("testKeyTime","testValueTime",30l, TimeUnit.MINUTES);
            System.out.println(redisUtilService.get("testKey"));
            System.out.println(redisUtilService.get("testKeyTime"));
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

}
