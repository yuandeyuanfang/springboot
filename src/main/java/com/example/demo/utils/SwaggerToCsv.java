package com.example.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 解析swagger接口信息生成excel
 */
public class SwaggerToCsv {

    public static void main(String[] args) throws IOException {
        //将swagger接口信息文件放到指定目录 如：http://127.0.0.1:8102/v2/api-docs，保存为json文件
        FileInputStream in = new FileInputStream("D:/api-docs.json");
        JsonNode jsonNode = new ObjectMapper().readTree(in);

        /**
         * 取所有数据并存到HashMap中
         */
        String api;
        HashMap<String, List<Root>> hm = new HashMap<>();
        JsonNode node = jsonNode.findValue("paths");
        Iterator<String> stringIterator = node.fieldNames();
        while (stringIterator.hasNext()) {
            //api
            JsonNode tags = node.findValue((api = stringIterator.next()));
            Iterator<String> methodsname = tags.fieldNames();
            while (methodsname.hasNext()) {
                //方法
                String method = methodsname.next();
                JsonNode methods = tags.findValue(method);
                String name = methods.findValue("tags").get(0).asText();
                String description = methods.findValue("summary").asText();
                String parameters = "";
                if(methods.findValue("parameters")!= null && methods.findValue("parameters").get(0) != null  && methods.findValue("parameters").get(0).findValue("name") != null
                        && methods.findValue("parameters").get(0).findValue("description") != null){
                    parameters = methods.findValue("parameters").get(0).findValue("name").asText()+"-"+methods.findValue("parameters").get(0).findValue("description").asText();
                }
                String responses = "";
                if(methods.findValue("responses")!= null && methods.findValue("responses").findValue("0") != null  && methods.findValue("responses").findValue("0").findValue("schema") != null
                        && methods.findValue("responses").findValue("0").findValue("schema").findValue("$ref") != null){
                    responses = methods.findValue("responses").findValue("0").findValue("schema").findValue("$ref").asText();
                }else{
                    if(methods.findValue("responses")!= null && methods.findValue("responses").findValue("200") != null  && methods.findValue("responses").findValue("200").findValue("schema") != null
                            && methods.findValue("responses").findValue("200").findValue("schema").findValue("$ref") != null){
                        responses = methods.findValue("responses").findValue("200").findValue("schema").findValue("$ref").asText();
                    }

                }
                //当前查询到的一个接口数据放到hashmap里管理
                Root root = new Root(name, method, api,description,parameters,responses);
                if (hm.containsKey(root.getName())) {
                    List<Root> roots = hm.get(root.getName());
                    roots.add(root);
                    hm.put(root.getName(), roots);
                } else {
                    ArrayList<Root> roots = new ArrayList<>();
                    roots.add(root);
                    hm.put(root.getName(), roots);
                }
                break;

            }

        }

        /**
         * 获得name的顺序，并按顺序写入csv
         */
        File file = new File("D:/result.csv");
        //excel不能读取utf-8编码的csv文件
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "GBK"));

        Iterator<JsonNode> names = jsonNode.findValue("tags").iterator();
        while (names.hasNext()) {
            String name = names.next().findValue("name").asText();
            if(hm == null || hm.get(name) == null){
                continue;
            }
            Iterator<Root> iterator1 = hm.get(name).iterator();
            bufferedWriter.write(name + ",");
            Boolean isFirst = true;
            //如果是第一行增加name，如果不是填入空白格
            while (iterator1.hasNext()) {
                if (!isFirst) {
                    bufferedWriter.write(",");
                } else {
                    isFirst = false;
                }
                Root next = iterator1.next();
                bufferedWriter.write(next.getApi() + "," + next.getDescription()+ "," + next.getApi()+ "," + next.getApi()+ "," + next.getParameters()+ "," + next.getResponses());
                bufferedWriter.newLine();
            }

        }
        bufferedWriter.close();
        //打开生成的csv文件
        Runtime.getRuntime().exec("cmd /c start D:/result.csv");
        System.out.println("done");


    }


    @Data
    static
    class Root {
        public Root(String name, String method, String api,String description, String parameters,String responses) {
            this.name = name;
            this.method = method;
            this.api = api;
            this.description = description;
            this.parameters = parameters;
            this.responses = responses;
        }

        private String description;//接口名

        private String name;//模块名

        private String method;//请求方式

        private String api;//接口地址

        private String parameters;//入参

        private String responses;//出参
    }

}
