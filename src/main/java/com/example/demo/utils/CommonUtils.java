package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class CommonUtils {

    private final static String charsetName = "UTF-8";

    /**
     *
     * 获取系统时间
     *
     * @return
     */
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        return time;
    }

    /**
     *
     * 获取系统时间
     *
     * @return
     */
    public static String getTimeByDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        return time;
    }

    /**
     *
     * 时间转换
     *
     * @return
     */
    public static Date getDate(String dateStr)  {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            return dateFormat.parse(dateStr);
        }catch (Exception e){
            log.error("getDate",e);
            return null;
        }
    }

    /**
     * 字符串Md5加密 Step1
     *
     * @param s
     * @return
     */
    public static String hash(String s) {
        try {
            byte[] bytes = md5(s);
            String hex = toHex(bytes);
            String result = new String(hex.getBytes(charsetName), charsetName);
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return s;
        }
    }

    /**
     * 字符串Md5加密 Step2
     *
     * @param s
     * @return
     */
    private static byte[] md5(String s) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes(charsetName));
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 字符串Md5加密 Step3
     *
     * @param hash
     * @return
     */
    private static final String toHex(byte hash[]) {
        if (hash == null) {
            return null;
        }
        int length = hash.length;
        int capacity = length * 2;
        StringBuffer buf = new StringBuffer(capacity);
        for (int i = 0; i < length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     *
     * 生成发起方报文ID
     *
     * @return
     */
    public static String getSndMsGid() {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int value = (int) ((Math.random() * 9 + 1) * 100);
        String num = time + value;
        return num;
    }

    /**
     *
     * 转换为String类型
     *
     * @param object
     * @return
     */
    public static String objectToString(Object object){
        String data = null;
        if (object instanceof String) {
            data = object.toString();
        }else {
            data = JSONObject.toJSONString(object);
        }
        return data;
    }

    /**
     *
     * 处理异常信息
     *
     * @param e
     * @return
     */
    public static String getExceptionString(Exception e) {
        String errorMsg = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            errorMsg = sw.toString();
            sw.close();
        } catch (Exception ex) {
            errorMsg = "ExceptionToString is error";
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
        return errorMsg;
    }

    /**
     *
     * @param obj
     * @return
     */
    public static JSONObject dataToJSONObject(Object obj) {
        String data = JSONObject.toJSONString(obj);
        return JSONObject.parseObject(data);
    }

}
