package com.example.demo.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Slf4j
public class PdfUtils {

    public static void main(String[] args) throws Exception {
        PdfUtils pdfUtils = new PdfUtils();
        //pdf转图片
//        pdfToImg("D:/450a91724e410f365450363c300c5c10.pdf","D:image.png");

        //pdf转图片然后合成pdf
//        pdfToImgToPdf("D:\\test\\22222.pdf","D:\\test\\3333.pdf");

        //itext5生成pdf
//        PdfReportService.main(null);

        //根据pdf模板生成pdf（AcrobatDC-PDF编辑器处理模板，可用wps将word转为pdf）
        pdfUtils.exportPDFByTemplate(null);
    }

    /**
     * pdf转图片
     * @param pdfPath
     * @param pngPath
     */
    public static void pdfToImg(String pdfPath,String pngPath){
        //将pdf装图片 并且自定义图片得格式大小
        File file = new File(pdfPath);
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 240);//可更改清晰度
                BufferedImage srcImage = resize(image, image.getWidth(), image.getHeight());
                ImageIO.write(srcImage, "PNG", new File(pngPath.replace(".",i+".")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * pdf转图片然后合成pdf
     * @param pdfPath
     * @param pdfOutPath
     */
    public static void pdfToImgToPdf(String pdfPath,String pdfOutPath){
        //将pdf装图片 并且自定义图片得格式大小
        File file = new File(pdfPath);
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            List<BufferedImage> images=new ArrayList<BufferedImage>();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 240);
                BufferedImage srcImage = resize(image, image.getWidth(), image.getHeight());
                images.add(srcImage);
            }
            //合成图片转pdf
            createPDFFromImage(pdfOutPath,images);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * pdf转图片然后合成pdf
     * @param input
     */
    public static byte[] pdfToImgToPdf(byte[] input){
        //将pdf装图片 并且自定义图片得格式大小
        byte[] bytes=null;
        PDDocument doc=null;
        try {
            doc = PDDocument.load(input);
            List<BufferedImage> images=new ArrayList<BufferedImage>();
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 240);
                BufferedImage srcImage = resize(image, image.getWidth(), image.getHeight());
                images.add(srcImage);
            }
            bytes=createPDFFromImage(images);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(doc!=null){
            try {
                doc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
    /**
     *图片合成pdf
     * @param images
     * @throws Exception
     */
    public static void createPDFFromImage(String pdfOutPath,List<BufferedImage> images){
        PDDocument doc = new PDDocument();
        try {
            PDPageContentStream contentStream;
            PDPage page;
            for (BufferedImage image : images) {
                page = new PDPage(new PDRectangle(image.getWidth(),image.getHeight()));
                doc.addPage(page);
                contentStream = new PDPageContentStream(doc,page,PDPageContentStream.AppendMode.APPEND, true);
                PDImageXObject pdImageXObject = JPEGFactory.createFromImage(doc,image);
                contentStream.drawXObject(pdImageXObject, 0, 0, image.getWidth(),image.getHeight());
                contentStream.close();
            }
            doc.save(pdfOutPath);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     *图片合成pdf
     * @param images
     * @throws Exception
     */
    public static byte[] createPDFFromImage(List<BufferedImage> images){
        byte[] bytes=null;
        ByteArrayOutputStream baos=null;
        PDDocument doc = new PDDocument();
        try {
            PDPageContentStream contentStream;
            PDPage page;
            for (BufferedImage image : images) {
                page = new PDPage(new PDRectangle(image.getWidth(),image.getHeight()));
                doc.addPage(page);
                contentStream = new PDPageContentStream(doc,page,PDPageContentStream.AppendMode.APPEND, true);
                PDImageXObject pdImageXObject = JPEGFactory.createFromImage(doc,image);
                contentStream.drawXObject(pdImageXObject, 0, 0, image.getWidth(),image.getHeight());
                contentStream.close();
            }
            baos = new ByteArrayOutputStream();
            doc.save(baos);
            bytes=baos.toByteArray();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }
    /**
     * 生成图片
     * @param source
     * @param targetW
     * @param targetH
     * @return
     */
    private static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        int type = source.getType();
        BufferedImage target = null;
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        if (sx > sy) {
            sx = sy;
            targetW = (int) (sx * source.getWidth());
        } else {
            sy = sx;
            targetH = (int) (sy * source.getHeight());
        }
        if (type == BufferedImage.TYPE_CUSTOM) {
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(targetW, targetH, type);
        }
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * 根据pdf模板生成pdf
     * 1.使用word自定义表格，导出为pdf
     * 2.使用AcrobatDC-PDF编辑器打开pdf,添加文本，点击准备表单，添加文本域
     */

    public void exportPDFByTemplate(HttpServletResponse response) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String template = String.valueOf(PdfUtils.class.getClassLoader().getResource("templates/pdfTemplate.pdf"));
        // 生成导出PDF的文件名称
        String fileName = URLEncoder.encode("PDF生成.pdf", "UTF-8");
        // 设置响应头
//        response.setContentType("application/force-download");
//        response.setHeader("Content-Disposition",
//                "attachment;fileName=" + fileName);
        OutputStream out = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper = null;
        PdfReader reader = null;
        try {
            // 保存到本地
            // out = new FileOutputStream(fileName);
            // 输出到浏览器端
//            out = response.getOutputStream();
//            File file = new File("D:PDF生成.pdf");
//            file.createNewFile();
            out = new FileOutputStream("D:\\"+fileName);
            // 读取PDF模板表单
            reader = new PdfReader(template);
            // 字节数组流，用来缓存文件流
            bos = new ByteArrayOutputStream();
            // 根据模板表单生成一个新的PDF
            stamper = new PdfStamper(reader, bos);
            // 获取新生成的PDF表单
            AcroFields form = stamper.getAcroFields();
            // 给表单生成中文字体，这里采用系统字体，不设置的话，中文显示会有问题
//            String simsun = String.valueOf(TswTraineeInfoPcTwController.class.getResource("/"));
//            BaseFont font = BaseFont.createFont(simsun+"/simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont font = BaseFont.createFont(String.valueOf(getClass().getClassLoader().getResource("templates"))+"/simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            form.addSubstitutionFont(font);
            // 装配数据
            Map<String, Object> dataMap = new HashMap<>(15);
            dataMap.put("personName","测试");
//            dataMap.put("personName",tswTraineeInfo.getPersonName());
//            dataMap.put("genderName",tswTraineeInfo.getGender() == 0 ? "女" : "男");
////            dataMap.put("birthDay",tswTraineeInfo.getBirthday());
//            dataMap.put("telehone",tswTraineeInfo.getTelephone());
//            dataMap.put("political",tswTraineeInfo.getPolitical());
//            dataMap.put("idCard",tswTraineeInfo.getIdCard());
//            dataMap.put("school",tswTraineeInfo.getSchool());
//            dataMap.put("major",tswTraineeInfo.getMajor());
//            dataMap.put("address",tswTraineeInfo.getAddress());
//            String bankBranchName = StringUtils.isEmpty(tswTraineeInfo.getBankBranchName()) ? "" : tswTraineeInfo.getBankBranchName();
//            dataMap.put("bankBranchName", bankBranchName);
//            String bankCarNum = StringUtils.isEmpty(tswTraineeInfo.getBankCardNum()) ? "" : tswTraineeInfo.getBankCardNum();
//            dataMap.put("bankCarNum",tswTraineeInfo.getPersonName()+"-"+bankCarNum);
//            dataMap.put("jobName",tswJobQueryVO.getTswEnterpriseInfo().getEnterpriseName());
//            dataMap.put("startAndEnd",simpleDateFormat.format(tswApplicationRecordQueryListVO.getWorkDate())+"—"+simpleDateFormat.format(tswApplicationRecordQueryListVO.getLeaveDate())+"\n 共"+(tswApplicationRecordQueryListVO.getLeaveDate().getTime() - tswApplicationRecordQueryListVO.getWorkDate().getTime()) / (1000 * 60 * 60 * 24)+"天");
//            dataMap.put("summary",tswApplicationRecordQueryListVO.getSummary());
//            dataMap.put("appraiseContentFirst", tswApplicationRecordQueryListVO.getAppraiseStatusFirst() == null ? "" : (tswApplicationRecordQueryListVO.getAppraiseStatusFirst() == 1 ? "不合格" : "合格"));
//            dataMap.put("appraisePersonNameFirst", StringUtils.isEmpty(tswApplicationRecordQueryListVO.getAppraisePersonNameFirst()) ? "" : tswApplicationRecordQueryListVO.getAppraisePersonNameFirst());
//            dataMap.put("appraiseTimeFirst", tswApplicationRecordQueryListVO.getAppraiseTimeFirst() == null ? " " : simpleDateFormat.format(tswApplicationRecordQueryListVO.getAppraiseTimeFirst()));
//            dataMap.put("appraiseContentSecond",tswApplicationRecordQueryListVO.getAppraiseStatusSecond() == null ? "" : (tswApplicationRecordQueryListVO.getAppraiseStatusSecond() == 1 ? "不合格" : "合格"));
//            dataMap.put("appraisePersonNameSecond",StringUtils.isEmpty(tswApplicationRecordQueryListVO.getAppraisePersonNameSecond()) ? "" : tswApplicationRecordQueryListVO.getAppraisePersonNameSecond());
//            dataMap.put("appraiseTimeSecond",tswApplicationRecordQueryListVO.getAppraiseTimeSecond() == null ? "" : simpleDateFormat.format(tswApplicationRecordQueryListVO.getAppraiseTimeSecond()));
//            dataMap.put("appraiseContentSecondCity",tswApplicationRecordQueryListVO.getAppraiseStatusSecond() == null ? "" : (tswApplicationRecordQueryListVO.getAppraiseStatusSecond() == 1 ? "不合格" : "合格"));
//            dataMap.put("appraisePersonNameSecondCity",StringUtils.isEmpty(tswApplicationRecordQueryListVO.getAppraisePersonNameSecond()) ? "" : tswApplicationRecordQueryListVO.getAppraisePersonNameSecond());
//            dataMap.put("appraiseTimeSecondCity",tswApplicationRecordQueryListVO.getAppraiseTimeSecond() == null ? "" : simpleDateFormat.format(tswApplicationRecordQueryListVO.getAppraiseTimeSecond()));
//            dataMap.put("image", StringUtils.isEmpty(tswApplicationRecordQueryListVO.getPaperworkUrl()) ? "" : tswApplicationRecordQueryListVO.getPaperworkUrl());
            dataMap.put("image", "");
//            //判断二次鉴定的团委是否是市本级/开发区
//            if (tswApplicationRecordQueryListVO.getAppraisePersonSecond() != null){
//                SysOrg sysOrg = sysOrgMapper.getByPrimaryKey((long)tswApplicationRecordQueryListVO.getAppraisePersonSecond());
//                if (sysOrg != null && ("331199".equalsIgnoreCase(sysOrg.getOrgenterCode()) || "331191".equalsIgnoreCase(sysOrg.getOrgenterCode()))){
//                    dataMap.put("appraiseContentSecond","");
//                    dataMap.put("appraisePersonNameSecond","");
//                    dataMap.put("appraiseTimeSecond","");
//                }else {
//                    dataMap.put("appraiseContentSecondCity","");
//                    dataMap.put("appraisePersonNameSecondCity","");
//                    dataMap.put("appraiseTimeSecondCity","");
//                }
//            }
            // 遍历data，给pdf表单赋值
            for(String key : dataMap.keySet()){
                // 图片要单独处理
                if("image".equals(key)){
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    com.itextpdf.text.Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
//                    String studentImage = new File("D:").getAbsolutePath()+"/"+dataMap.get(key).toString();
                    String studentImage = "C:\\Users\\lb-pc\\Desktop\\Dingtalk_20220221085942.jpg";
                    //根据路径或Url读取图片
                    com.itextpdf.text.Image image = Image.getInstance(studentImage);
                    //获取图片页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    //图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    //添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
                // 设置普通文本数据
                else {
                    form.setField(key, dataMap.get(key).toString());
                }
            }
            // 表明该PDF不可修改
            stamper.setFormFlattening(true);
            // 关闭资源
            stamper.close();
            // 将ByteArray字节数组中的流输出到out中（即输出到浏览器）
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            log.info("*****************************PDF导出成功*********************************");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}