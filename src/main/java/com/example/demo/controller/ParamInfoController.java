package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.DataService;
import com.example.demo.vo.ParamInfoVO;
import com.example.demo.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "paramInfo")
public class ParamInfoController {
    @Autowired
    DataService dataService;

    @RequestMapping(value = "select")
    public ResultVO selectParamInfo(ParamInfoVO paramInfo) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        try {
            resultVO.setResult(dataService.selectParamInfo(paramInfo));
        } catch (Exception e) {
            resultVO.setResultDes(e.getMessage());
            resultVO.setSuccess(false);
        }
        return resultVO;
    }

    @RequestMapping(value = "insert")
    public ResultVO insertParamInfo(ParamInfoVO paramInfo) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        try {
            //参数校验
            if(paramInfo == null){
                resultVO.setResultDes("参数不能为空");
                resultVO.setSuccess(false);
            }else{
                if (StringUtils.isBlank(paramInfo.getParamName())) {
                    resultVO.setResultDes("参数名称不能为空");
                    resultVO.setSuccess(false);
                }
                if (StringUtils.isBlank(paramInfo.getParamValue())) {
                    resultVO.setResultDes("参数值不能为空");
                    resultVO.setSuccess(false);
                }
            }

            //业务处理
            if (resultVO.isSuccess()) {
                dataService.insertParamInfo(paramInfo);
            }
        } catch (Exception e) {
            resultVO.setResultDes(e.getMessage());
            resultVO.setSuccess(false);
        }
        return resultVO;
    }

    @RequestMapping(value = "update")
    public ResultVO updateParamInfo(ParamInfoVO paramInfo) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        try {
            //参数校验
            if(paramInfo == null){
                resultVO.setResultDes("参数不能为空");
                resultVO.setSuccess(false);
            }else{
                if (StringUtils.isBlank(paramInfo.getParamName())) {
                    resultVO.setResultDes("参数名称不能为空");
                    resultVO.setSuccess(false);
                }
                if (StringUtils.isBlank(paramInfo.getParamValue())) {
                    resultVO.setResultDes("参数值不能为空");
                    resultVO.setSuccess(false);
                }
            }

            //业务处理
            if (resultVO.isSuccess()) {
                dataService.updateParamInfo(paramInfo);
            }
        } catch (Exception e) {
            resultVO.setResultDes(e.getMessage());
            resultVO.setSuccess(false);
        }
        return resultVO;
    }

    @RequestMapping(value = "delete")
    public ResultVO deleteParamInfo(ParamInfoVO paramInfo) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        try {
            //参数校验
            if(paramInfo == null){
                resultVO.setResultDes("参数不能为空");
                resultVO.setSuccess(false);
            }else{
                if (StringUtils.isBlank(paramInfo.getParamName())) {
                    resultVO.setResultDes("参数名称不能为空");
                    resultVO.setSuccess(false);
                }
            }

            //业务处理
            if (resultVO.isSuccess()) {
                dataService.deleteParamInfo(paramInfo);
            }
        } catch (Exception e) {
            resultVO.setResultDes(e.getMessage());
            resultVO.setSuccess(false);
        }
        return resultVO;
    }

}
