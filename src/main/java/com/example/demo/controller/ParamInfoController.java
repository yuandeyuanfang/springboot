package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.DataService;
import com.example.demo.vo.ParamInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "paramInfo")
public class ParamInfoController {
    @Autowired
    DataService dataService;

    @RequestMapping(value = "select")
    public JSONObject selectParamInfo(ParamInfoVO paramInfo) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            result.put("result", dataService.selectParamInfo(paramInfo));
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping(value = "insert")
    public JSONObject insertParamInfo(ParamInfoVO paramInfo) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            dataService.insertParamInfo(paramInfo);
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping(value = "update")
    public JSONObject updateParamInfo(ParamInfoVO paramInfo) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            dataService.updateParamInfo(paramInfo);
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping(value = "delete")
    public JSONObject deleteParamInfo(ParamInfoVO paramInfo) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        try {
            dataService.deleteParamInfo(paramInfo);
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
        }
        return result;
    }

}
