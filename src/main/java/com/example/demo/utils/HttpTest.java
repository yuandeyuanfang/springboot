package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 农信加密解密工具类
 */
public class HttpTest {

    private static String httpUrl= "";

    public static void main(String[] args) {
//        com.example.demo.utils.knowledge.ArrayList<String> list = new com.example.demo.utils.knowledge.ArrayList<>();
//        list.add("1");
//        System.out.println(list.size());
        getFace();
    }

    public static final RestTemplate restTemplate = new RestTemplate(generateHttpsRequestFactory());

    /**
     * restTemplate配置,可以访问https
     */
    private static HttpComponentsClientHttpRequestFactory generateHttpsRequestFactory() {
        try {

            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory connectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setConnectionManager(poolingConnectionManager(connectionSocketFactory));
            CloseableHttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(5 * 1000);
            factory.setReadTimeout(30 * 1000);
            return factory;
        } catch (Exception e) {
            throw new RuntimeException("创建HttpsRestTemplate失败", e);
        }
    }

    /**
     * 链接线程池管理,可以keep-alive不断开链接请求,这样速度会更快 MaxTotal 连接池最大连接数 DefaultMaxPerRoute
     * 每个主机的并发 ValidateAfterInactivity
     * 可用空闲连接过期时间,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
     *
     * @return
     */
    public static HttpClientConnectionManager poolingConnectionManager(SSLConnectionSocketFactory connectionSocketFactory) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", connectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(registry);

        //最大连接数
        poolingConnectionManager.setMaxTotal(1000);
        //并发数
        poolingConnectionManager.setDefaultMaxPerRoute(200);
        //关闭空闲30s连接
        poolingConnectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
        //关闭过期连接
        poolingConnectionManager.closeExpiredConnections();

        return poolingConnectionManager;
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
//            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//            requestFactory.setConnectTimeout(60000);//设置超时
//            requestFactory.setReadTimeout(60000);//设置超时
//            RestTemplate restTemplate = new RestTemplate(requestFactory);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));//解决响应中文乱码的问题
            //报文头
            HttpHeaders requestHeaders = new HttpHeaders();

            //发送请求
            HttpEntity<MultiValueMap> entity = new HttpEntity(paramObj, requestHeaders);
            String httpResult = restTemplate.postForObject(httpUrl, entity, String.class);
            JSONObject jsonObject = JSONObject.parseObject(httpResult);
            String base64Code = jsonObject.getString("result");
            File file = new File(nameList.get(i)+idList.get(i)+".jpg");
//            ImageUtils.downloadFile(base64Code, file);
            try {
                Thread.sleep(100);
            }catch (Exception e){}

        }


//        System.out.println(httpResult);
        return null;

    }

}
