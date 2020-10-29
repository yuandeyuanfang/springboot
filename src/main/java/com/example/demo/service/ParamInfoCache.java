package com.example.demo.service;

import com.example.demo.vo.ParamInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 参数表缓存类
 */
@Slf4j
@Service
public class ParamInfoCache {
	@Autowired
	DataService dataService;

	private ConcurrentHashMap<String, String> maps = new ConcurrentHashMap<String, String>();

	/**
	 * 从数据库取出最新数据放入内存
	 */
	public synchronized void init()  {
		log.info("[ParamInfo][init][start]");
		List<ParamInfoVO> countryList = dataService.selectParamInfo(null);
		if( countryList != null ) {
			ConcurrentHashMap<String, String> tempMaps = new ConcurrentHashMap<String, String>();
			for( ParamInfoVO paramInfo : countryList ) {
				tempMaps.put(paramInfo.getParamName(),paramInfo.getParamValue());
				log.info("[ParamInfo]"+paramInfo.getParamName()+"-"+paramInfo.getParamValue());
			}
			this.maps = tempMaps;
		}
		log.info("[ParamInfo][init][end]");
	}

	/**
	 * 根据参数名从内存中取出参数值
	 * @param name
	 * @return
	 */
	public String getParamInfoByName(String name) {
		String paramValue = maps.get(name);
		return paramValue;
	}

}