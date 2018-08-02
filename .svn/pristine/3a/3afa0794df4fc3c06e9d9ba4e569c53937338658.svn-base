package com.app.utils;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

/**
 * @author ERIC
 * 图片检查工具
 */
public class ImageCheck {
	private  MimetypesFileTypeMap mtftp;

    public ImageCheck(){
        mtftp = new MimetypesFileTypeMap();
        /* 不添加下面的类型会造成误判 详见：http://stackoverflow.com/questions/4855627/java-mimetypesfiletypemap-always-returning-application-octet-stream-on-android-e*/
        mtftp.addMimeTypes("bmp");/*image png tif jpg jpeg*/ 
    }
    public boolean isImage(File file){
        String mimetype= mtftp.getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("image");
    }
    public boolean isImageWithOutMimetype(File file){
    	if(file.isDirectory()){
    		return false;
    	}
    	if(!file.exists()){
    		return false;
    	}
    	InputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			return isImage(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
    	
    	
    }
    public boolean isImage(InputStream inputStream){
    	if(inputStream == null){
    		return false;
    	}
    	Image image;
    	try {
    		image = ImageIO.read(inputStream);
    		inputStream.close();
             return !(image == null || image.getWidth(null) <= 0 || image.getHeight(null) <= 0);
        } catch (Exception e) {
             return false;
        }
    }
}
