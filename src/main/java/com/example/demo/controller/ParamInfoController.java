package com.example.demo.controller;

import com.example.demo.dto.TradeLogDTO;
import com.example.demo.service.DataService;
import com.example.demo.service.TradeLogService;
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
    @Autowired
    TradeLogService tradeLogService;

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
        //记录日志
        TradeLogDTO tradeLog = tradeLogService.tradeLoginsert(TradeLogDTO.MODEL_OA, TradeLogDTO.TYPE_OA_insertParamInfo, paramInfo);

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
            tradeLogService.exceptionToExtra(tradeLog, e);
        }

        //记录日志
        tradeLogService.tradeLogupdate(tradeLog, resultVO);

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
