package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.TradeLogDTO;
import com.example.demo.mapper.TradeLogMapper;
import com.example.demo.utils.CommonUtils;
import com.example.demo.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class TradeLogService {

	@Autowired
	TradeLogMapper tradeLogMapper;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static final SimpleDateFormat dateFormatyMd = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 将异常信息放入附加报文字段
	 * @param tradeLog
	 * @param e
	 */
	public void exceptionToExtra(TradeLogDTO tradeLog, Exception e)  {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			e.printStackTrace(pw);
			String extra = sw.toString();
			if(extra.length() > 3000){
				extra = extra.substring(0,3000);
			}
			tradeLog.setExtra(extra);
		} catch (Exception e1) {
			log.error("[exceptionToExtra]", e1);
		}finally {
			pw.close();
		}
	}

	/**
	 * 方法执行前记录日志信息
	 * @param model
	 * @param tradeType
	 * @param params
	 * @return
	 */
	public TradeLogDTO tradeLoginsert(String model,String tradeType,Object params){
		TradeLogDTO tradeLog = new TradeLogDTO();
		try {
			tradeLog.setTradeId(dateFormat.format(new Date()));
			tradeLog.setStartTime(System.currentTimeMillis());
			tradeLog.setModel(model);
			tradeLog.setTradeType(tradeType);
			tradeLog.setTradeBegain(CommonUtils.getTime());
			tradeLog.setTradeDate(dateFormatyMd.format(new Date()));
			tradeLog.setInput(JSON.toJSONString(params));
//			tradeLogMapper.insert(tradeLog);
		} catch (Exception e) {
			log.error("[tradeLoginsert]", e);
		}
		return tradeLog;
	}

	/**
	 * 方法执行后更新日志信息
	 */
	public void tradeLogupdate(TradeLogDTO tradeLog, ResultVO resultVo){
		try {
			if(tradeLog != null){
				if (resultVo.isSuccess()) {
					tradeLog.setTradeResult(TradeLogDTO.RESULT_TRUE);
				} else {
					tradeLog.setTradeResult(TradeLogDTO.RESULT_FALSE);
				}
				tradeLog.setOutput(JSON.toJSONString(resultVo));
				tradeLog.setTradeEnd(CommonUtils.getTime());
				tradeLog.setTradeCost(System.currentTimeMillis() - tradeLog.getStartTime());
				tradeLogMapper.insert(tradeLog);
			}
		} catch (Exception e) {
			log.error("[tradeLogUpdate]", e);
		}
	}

}
