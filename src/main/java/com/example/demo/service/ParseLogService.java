package com.example.demo.service;

import com.example.demo.entity.ParseLog;
import com.example.demo.mapper.DataMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ParseLogService {
    @Autowired
    DataMapper dataMapper;

    @Test
    /**
     * 通过关键字解析日志文件，并生成sql语句
     */
    public void parseLogAndCreateSql() {
        //需要解析的日志文件夹路径
        File file = new File("D://log");
        File[] fileList = file.listFiles();
        //需要解析的关键字
        List<String> keyWordList = new ArrayList<>();
        keyWordList.add("332521196210236519");

        for (int i = 0; i < fileList.length; i++) {
            log.info("parseLogFileName-"+fileList[i].getName());
            parseLogAndCreateSql(fileList[i], keyWordList);
//          break;
        }
    }

    public void parseLogAndCreateSql(File file, List<String> keyWordList){
        file = new File("D://log//running.2024-05-30.0.log");
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line0 = null;//当前行
            String line1 = null;//上1行
            String line2 = null;//上2行
            String line3 = null;//上3行
            String line4 = null;//上4行
            String line5 = null;//上5行
            int i = 0;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while((line0 = br.readLine())!=null) {
                for (String keyWord : keyWordList) {
                    if (line0.indexOf(keyWord) >= 0 && (line0.indexOf("BaseUserMapper.updateByPrimaryKeySelective") >= 0 || line0.indexOf("BaseUserMapper.chongxinshoujianByCardNo") >= 0)) {
                        ParseLog parseLog = new ParseLog();
                        String line0Thread = line0.substring(line0.indexOf("] [")+3);
                        line0Thread = line0Thread.substring(0,line0Thread.indexOf("]"));
                        String lineUse = line1;
                        String lineUseThread = lineUse.substring(lineUse.indexOf("] [")+3);
                        lineUseThread = lineUseThread.substring(0,lineUseThread.indexOf("]"));
                        //寻找同一线程的上一条日志
                        log.info("上5行" + line5);
                        log.info("上4行" + line4);
                        log.info("当3行" + line3);
                        log.info("上2行" + line2);
                        log.info("上1行" + line1);
                        log.info("当前行" + line0);
                        if(!line0Thread.equals(lineUseThread)){
                            lineUse = line2;
                            lineUseThread = lineUse.substring(lineUse.indexOf("] [")+3);
                            lineUseThread = lineUseThread.substring(0,lineUseThread.indexOf("]"));
                            if(!line0Thread.equals(lineUseThread)){
                                lineUse = line3;
                                lineUseThread = lineUse.substring(lineUse.indexOf("] [")+3);
                                lineUseThread = lineUseThread.substring(0,lineUseThread.indexOf("]"));
                                if(!line0Thread.equals(lineUseThread)){
                                    lineUse = line4;
                                    lineUseThread = lineUse.substring(lineUse.indexOf("] [")+3);
                                    lineUseThread = lineUseThread.substring(0,lineUseThread.indexOf("]"));
                                    if(!line0Thread.equals(lineUseThread)){
                                        lineUse = line5;
                                        lineUseThread = lineUse.substring(lineUse.indexOf("] [")+3);
                                        lineUseThread = lineUseThread.substring(0,lineUseThread.indexOf("]"));
                                    }if(!line0Thread.equals(lineUseThread)){
                                        lineUse = null;
                                    }
                                }
                            }
                        }
                        if(lineUse != null){
                            parseLog.setKeyWord(keyWord);
                            String aTime = line0.split(",")[0];
                            parseLog.setSqlTime(dateFormat.parse(aTime));
                            String sql1 = lineUse.substring(lineUse.indexOf("Preparing: ") + 11);//解析规律
                            String sql2 = line0.substring(line0.indexOf("Parameters: ") + 11);//解析规律
                            if (sql1.split(", ").length != sql2.split(", ").length) {
                                for (int t = 0; t < sql2.split(", ").length; t++) {
                                    String temp = sql2.split(", ")[t];
                                    temp = temp.replace("(String)", "");
                                    temp = temp.replace("(Long)", "");
                                    if (temp.indexOf("(Timestamp)") >= 0) {
                                        temp = temp.replace("(Timestamp)", "");
                                        temp = temp.substring(0, temp.indexOf("."));
                                        temp = "to_date('" + temp + "','yyyy-mm-dd hh24:mi:ss')";
                                    } else {
                                        temp = "'" + temp + "'";
                                    }
                                    sql1 = sql1.replaceFirst("\\?", temp);
                                }
                                sql1 = sql1 + ";";
                                parseLog.setSqlStr(sql1);

                            } else {
                                parseLog.setSqlStr("参数解析失败");
                                log.info("上一行" + line1);
                                log.info("当前行" + line0);
                            }
                        }else {
                            parseLog.setSqlStr("上下文解析失败");
                            log.info("上一行" + line1);
                            log.info("当前行" + line0);
                        }

                        dataMapper.insertParseLog(parseLog);
                    }

                }
                line5 = line4;
                line4 = line3;
                line3 = line2;
                line2 = line1;
                line1 = line0;

            }
        }catch(Exception e){
            log.error("parseLogAndCreateSqlError",e);
        }
    }

}
