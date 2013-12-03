/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 缩略图处理
 * @author zhaolin
 */
public class ThumbnailUtil {

    /**
     * 缩放png文件
     * @param sourceFile 源文件
     * @param outFile 目标文件
     * @param w 宽度
     * @param h 高度
     */
    public static void handlePng(File sourceFile, File outFile, int w, int h) {
        try {
            double ratioH = 0.0;
            double ratioW = 0.0;
            BufferedImage srcPic = ImageIO.read(sourceFile);
            Image itemp = srcPic.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
            ratioW = ((double) w) / srcPic.getWidth();
            ratioH = ((double) h) / srcPic.getHeight();
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratioW, ratioH), null);
            itemp = op.filter(srcPic, null);
            PngEncoder encoder = new PngEncoder(itemp);
            //质量最高
            encoder.setCompressionLevel(0);
            encoder.setEncodeAlpha(false);
            encoder.pngEncode();
            byte[] result = encoder.pngEncode();
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(result);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放JPG文件
     * @param sourceFile 源文件
     * @param outFile 目标文件
     * @param w 宽度
     * @param h 高度
     */
    public static void handleJpg(File sourceFile, File outFile, int w, int h) {
        if (!sourceFile.exists()) {
            return;
        }
        try {
            Image src = ImageIO.read(sourceFile);
            BufferedImage tag = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
            /*
             * Image.SCALE_SMOOTH 的缩略算法   生成缩略图片的平滑度的
             * 优先级比速度高 生成的图片质量比较好 但速度慢
             */
            tag.getGraphics().drawImage(
                    src.getScaledInstance(w, h,
                    Image.SCALE_SMOOTH), 0, 0, null);

            FileOutputStream out = new FileOutputStream(outFile);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
