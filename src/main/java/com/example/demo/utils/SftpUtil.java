package com.example.demo.utils;

import com.example.demo.entity.DWD_PTPI_TABLE_1_M;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

@Slf4j
/**
 * SFTP工具类
 */
public class SftpUtil {

    //服务器信息
    static String sftp_ip = "192.168.206.203";
    static String sftp_username = "root";
    static String sftp_password = "";
    static int sftp_port = 22;

    public static void main (String[] args) {
        try {
            //遍历文件夹下的文件名称
//            System.out.println(listFile("/root"));
            //下载文件到指定目录
//            downloadSftpFile("D:/", "/3.txt");
            //解压tar文件
//            uncompressTarGz("D:/zookeeper-3.4.11.tar.gz", "D:/tar");
            //读取TXT文件
//            txtToList("D:/dw_ptpi_table_1_331100_m_202312.txt");
            //读取后的txt文件转为对象
            listToObject("D:/dw_ptpi_table_1_331100_m_202312.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压tar文件
     * @param inputFilePath 待解压文件名-带路径
     * @param outputFolderPath 解压后路径
     * @throws IOException
     */
    public static List<String> uncompressTarGz(String inputFilePath, String outputFolderPath) throws IOException {
        List<String> fileNameList = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(inputFilePath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             GzipCompressorInputStream gzipInputStream = new GzipCompressorInputStream(bufferedInputStream);
             TarArchiveInputStream tarInputStream = new TarArchiveInputStream(gzipInputStream)) {

            TarArchiveEntry entry;
            while ((entry = (TarArchiveEntry) tarInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    File outputFile = new File(outputFolderPath, entry.getName());
                    createParentDirectory(outputFile);
                    fileNameList.add(entry.getName());
                    log.info(entry.getName());
                    try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = tarInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
        return fileNameList;
    }

    /***
     * 读取TXT文件
     * @param filePathName 完整文件名
     * @return
     */
    public static List<String> txtToList(String filePathName){
        List<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(new File(filePathName)))){
            String s = null;
            while((s = br.readLine())!=null){
                result.add(s);
            }
        }catch(Exception e){
            log.error("txtToList",e);
        }
//        System.out.println(result.toString());
        return result;
    }

    /***
     * 读取后的txt文件转为对象
     * @param filePathName 完整文件名
     * @return
     */
    public static void listToObject(String filePathName) throws IllegalAccessException {
        List<String> result = txtToList(filePathName);
        List<DWD_PTPI_TABLE_1_M> list = new ArrayList<>();
        for (String dwd_ptpi_table_1_mString:result) {
            if(StringUtils.isBlank(dwd_ptpi_table_1_mString)){
                continue;
            }
            String[] array = dwd_ptpi_table_1_mString.split("\t");

            int i=0;
            DWD_PTPI_TABLE_1_M dwd_ptpi_table_1_m = new DWD_PTPI_TABLE_1_M();
            Field[] fields = dwd_ptpi_table_1_m.getClass().getDeclaredFields();
            for (Field field : fields) {
                if(array.length <= i){
                    break;
                }
                field.set(dwd_ptpi_table_1_m, array[i]);
                i++;
            }
            list.add(dwd_ptpi_table_1_m);
        }
        System.out.println(list.size());

    }

    private static void createParentDirectory(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    /**
     * 遍历文件夹下的文件名称
     */
    public static String listFile(String sftp_path) throws JSchException {
        StringBuffer stringBufferFile = new StringBuffer("文件：");
        stringBufferFile.append("  ");
        StringBuffer stringBufferDir = new StringBuffer("目录：");
        stringBufferDir.append("  ");
        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch();
        session = jsch.getSession(sftp_username, sftp_ip, sftp_port);
        session.setPassword(sftp_password);
        session.setTimeout(10000);
        Properties config = new Properties();
        //设置不用检查HostKey，设成yes，一旦计算机的密匙发生变化，就拒绝连接。
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp chSftp = (ChannelSftp) channel;

        try {
            //遍历文件
            chSftp.cd(sftp_path);
            Vector<ChannelSftp.LsEntry> files = chSftp.ls(".");
            for (ChannelSftp.LsEntry file : files) {

                if (!file.getAttrs().isDir()) {
                    stringBufferFile.append(file.getFilename());
                    stringBufferFile.append(" ");
                }else{
                    stringBufferDir.append(file.getFilename());
                    stringBufferDir.append("  ");
                }
            }
        } catch (Exception e) {
            log.error("download error.",e);
        } finally {
            chSftp.quit();
            channel.disconnect();
            session.disconnect();
        }
        return stringBufferDir.toString()+"  "+stringBufferFile.toString();
    }

    /**
     * 遍历文件夹下的文件名称
     */
    public static Vector<ChannelSftp.LsEntry> listFileArray(String sftp_path) throws JSchException {
        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch();
        session = jsch.getSession(sftp_username, sftp_ip, sftp_port);
        session.setPassword(sftp_password);
        session.setTimeout(10000);
        Properties config = new Properties();
        //设置不用检查HostKey，设成yes，一旦计算机的密匙发生变化，就拒绝连接。
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp chSftp = (ChannelSftp) channel;

        try {
            //遍历文件
            chSftp.cd(sftp_path);
            Vector<ChannelSftp.LsEntry> files = chSftp.ls(".");
            return files;
        } catch (Exception e) {
            log.error("download error.",e);
        } finally {
            chSftp.quit();
            channel.disconnect();
            session.disconnect();
        }
        return null;
    }

    /**
     * 下载文件到指定目录
     * @param localPath
     * @param fileName
     * @throws JSchException
     */
    public static boolean downloadSftpFile(String localPath, String fileName) throws JSchException {

        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch();
        session = jsch.getSession(sftp_username, sftp_ip, sftp_port);
        session.setPassword(sftp_password);
        session.setTimeout(10000);
        Properties config = new Properties();
        //设置不用检查HostKey，设成yes，一旦计算机的密匙发生变化，就拒绝连接。
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp chSftp = (ChannelSftp) channel;

        String localFilePath = localPath + File.separator;

        try {
            //使用ChannelSftp的get(文件名，本地路径{包含文件名})方法下载文件
            chSftp.get(fileName, localFilePath);
            return true;
        } catch (Exception e) {
            log.error("download error.",e);
        } finally {
            chSftp.quit();
            channel.disconnect();
            session.disconnect();
        }
        return false;
    }
}