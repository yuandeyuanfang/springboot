package com.example.demo.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PdfUtils {

    public static void main(String[] args) {
        pdfToImg("D:/450a91724e410f365450363c300c5c10.pdf","D:image.png");
//        pdfToImgToPdf("D:\\test\\22222.pdf","D:\\test\\3333.pdf");
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

}