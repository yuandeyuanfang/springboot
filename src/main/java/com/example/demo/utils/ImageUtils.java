package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class ImageUtils {

    public static void main(String[] args) throws Exception {
        ImageUtils.cutDownImage(new File("E:\\logs\\file\\1e20f93e8e564b9392346d3c3c2fc5b0.jpg"),1024*1024,5);
        System.out.println(ImageUtils.fileToBase64(new File("E:\\logs\\file\\1e20f93e8e564b9392346d3c3c2fc5b0.jpg")));//报错的话把pom.xml中<scope>provided</scope>注释掉
    }

    /**
     * 图片压缩
     * @param file 被压缩文件
     * @param size 目标大小，达到后停止压缩
     * @param maxCount 最大压缩次数
     */
    public static void cutDownImage(File file, int size,int maxCount) {
        try {
            fileSizeChange(file,size,maxCount);
        } catch (Exception e) {
            log.info("图片压缩出错：" + e.getMessage());
        }
    }

    /**
     *
     * 改变图片大小
     *
     * @param inputFile
     * @return
     */
    public static void fileSizeChange(File inputFile, int size,int maxCount) {
        int count = 0;
        while (inputFile.length() > size && count < maxCount) {
            if(count == 0){
                log.info("[fileSizeChange]压缩前大小" + inputFile.length());
            }
            try {
                Thumbnails.of(inputFile.getPath()).scale(1f)
                        .outputQuality(0.8f).toFile(inputFile.getPath());
                inputFile = new File(inputFile.getPath());
                count++;
            } catch (IOException e) {
                count++;
                log.info("图片压缩失败：" + e.getMessage());
            }
        }
        if(count > 0){
            log.info("[fileSizeChange]压缩后大小" + inputFile.length()+"[压缩次数]"+count);
        }
    }

    /**
     *
     * 将图片转换为base64
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String fileToBase64(File file) throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] byt = new byte[fis.available()];
            fis.read(byt);
            return Base64.encodeBase64String(byt);
        } catch (Exception e) {
            log.info("转base64码失败：" + e.getMessage());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return null;
    }

    /**
     *
     * 将base64码转换成文件
     *
     * @param base64Code
     * @param file
     */
    public static void downloadFile(String base64Code, File file) {
        FileOutputStream fos = null;
        try {
            byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64Code);
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
