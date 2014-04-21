package utils;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.apache.commons.lang.StringUtils;

public class CompressPic {
	public static void main(String[] args) {
		if (compressPic("D:/ssss.jpg", "D:/ssss1.jpg")) {
			System.out.println("压缩成功");
		} else {
			System.out.println("压缩失败");
		}
	}

	public static boolean compressPic(String srcFilePath, String descFilePath) {
		File file = null;
		BufferedImage src = null;
		FileOutputStream out = null;
		ImageWriter imgWrier;
		ImageWriteParam imgWriteParams;

		//制定为jpg
		imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
		imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
				null);
		

		try {
			if (StringUtils.isBlank(srcFilePath)) {
				return false;
			} else {
				file = new File(srcFilePath);
				if(file.length() > 50000){
					
					float rate = (float) 0.1;
					if(file.length() < 100000){
						rate=(float)0.6;
					}else if(file.length() < 200000){
						rate=(float)0.5;
					}else if(file.length() < 300000){
						rate=(float)0.3;
					}else if(file.length() < 750000){
						rate=(float)0.3;
					}else if(file.length() < 1240000){
						rate=(float)0.1;
					}
					// 压缩必须制定为MODE_EXPLICIT
					imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
					// 压缩比在0-1之间
					imgWriteParams.setCompressionQuality(rate);
					imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
					ColorModel colorModel = ColorModel.getRGBdefault();
					//颜色
					imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
							colorModel, colorModel.createCompatibleSampleModel(256, 256)));
					
					src = ImageIO.read(file);
					out = new FileOutputStream(descFilePath);
					imgWrier.reset();
					imgWrier.setOutput(ImageIO.createImageOutputStream(out));
					imgWrier.write(null, new IIOImage(src, null, null),
							imgWriteParams);
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static byte[] compressPic(byte[] data) {
		ByteArrayInputStream is = new ByteArrayInputStream(data);

		BufferedImage src = null;
		ByteArrayOutputStream out = null;
		ImageWriter imgWrier;
		ImageWriteParam imgWriteParams;

		imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
		imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
				null);
		imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
		imgWriteParams.setCompressionQuality((float) 0.3 / data.length);
		imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
		ColorModel colorModel = ColorModel.getRGBdefault();
		imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
				colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		try {
			src = ImageIO.read(is);
			out = new ByteArrayOutputStream(data.length);
			imgWrier.reset();
			imgWrier.setOutput(ImageIO.createImageOutputStream(out));
			imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);

			out.flush();
			out.close();
			is.close();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
