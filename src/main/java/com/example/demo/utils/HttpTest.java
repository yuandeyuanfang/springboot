package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP示例类
 */
@Slf4j
public class HttpTest {

    private static String HTTPURL= "";

    public static void main(String[] args) {
        httpSend();
	}

    /**
     * 发送HTTP请求
     * @return
     */
    public static String httpSend()  {
        String httpResult = null;
        //组装参数
        Map<String,Object> inputMap = new HashMap<String,Object>(){{
            put("busiInsuType","11");
        }};

        //创建http请求
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(50000);//设置超时时间
        requestFactory.setReadTimeout(50000);//设置超时时间
        requestFactory.setOutputStreaming(false);//输出完整信息
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        //报文头
        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.add("Content-Type", "application/json");
//        requestHeaders.set("x-ca-key","bxgs");
//        requestHeaders.set("x-ca-signature","1675126432211:17705F55BEE901FB0F9D881CFBBA2449DF5D1BB7085B9B83518D1F596D87A885");

        //发送请求
        HttpEntity<MultiValueMap> entity = new HttpEntity(inputMap, requestHeaders);
        try {
            //post请求
            httpResult = restTemplate.postForObject(HTTPURL, entity, String.class);
            //get请求
//            httpResult = restTemplate.getForObject("https://zjlszlb.lshrss.cn:8003/dbwy/commercialInsuranceService/saveInsurRegistration", String.class);
            log.info(httpResult);
        }catch (Exception e){
            log.error("httpError",e);
        }

        return httpResult;

    }

}
