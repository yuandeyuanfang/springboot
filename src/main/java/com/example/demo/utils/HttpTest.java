package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 农信加密解密工具类
 */
public class HttpTest {

    private static String httpUrl= "";

    public static void main(String[] args) {
//        com.example.demo.utils.knowledge.ArrayList<String> list = new com.example.demo.utils.knowledge.ArrayList<>();
//        list.add("1");
//        System.out.println(list.size());
//        getFace();
    }


    public static String getFace()  {
        List<String> idList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        idList.add("511023199006191111");

        nameList.add("111");



        for (int i=0;i<idList.size();i++){
            //组装参数
            JSONObject paramObj = new JSONObject();
            paramObj.put("idNo",idList.get(i));
            paramObj.put("name",nameList.get(i));

            //创建http请求
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(60000);//设置超时
            requestFactory.setReadTimeout(60000);//设置超时
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));//解决响应中文乱码的问题
            //报文头
            HttpHeaders requestHeaders = new HttpHeaders();

            //发送请求
            HttpEntity<MultiValueMap> entity = new HttpEntity(paramObj, requestHeaders);
            String httpResult = restTemplate.postForObject(httpUrl, entity, String.class);
            JSONObject jsonObject = JSONObject.parseObject(httpResult);
            String base64Code = jsonObject.getString("result");
            File file = new File(nameList.get(i)+idList.get(i)+".jpg");
            ImageUtils.downloadFile(base64Code, file);
            try {
                Thread.sleep(100);
            }catch (Exception e){}

        }


//        System.out.println(httpResult);
        return null;

    }

}
