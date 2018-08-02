package com.app.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;







/**
 * @author ERIC
 *
 */
public class NewImageUtils {
	//色差范围0~255
    public static int color_range = 210;
	/**
	 * @param file 源文件
	 * @param waterFile 水印文件
	 * @param x		距离右下角X的偏移量
	 * @param y		距离右下角Y的偏移量
	 * @param alpha 透明度0.1~1.0 1.0完全不透明
	 * @return	BufferedImage
	 * @throws IOException
	 */
	private static BufferedImage waterMaker(File file,File waterFile,int x, int y ,float alpha,int RGB)throws IOException{
		 // 获取底图
		BufferedImage buffImg = ImageIO.read(file);
		// 获取层图
		BufferedImage waterImg = getTransParentImage(waterFile,RGB);
		// 创建Graphics2D对象，用在底图对象上绘图
		Graphics2D g2d = buffImg.createGraphics();
		int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
		int waterImgHeight = waterImg.getHeight();// 获取层图的高度
		// 在图形和图像中实现混合和透明效果
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// 绘制
		g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
		g2d.dispose();// 释放图形上下文使用的系统资源
		return buffImg;
	}
	
	private static BufferedImage waterMaker(BufferedImage buffImg,BufferedImage OraWaterImg,int x, int y ,float alpha,int RGB)throws IOException{
		/* // 获取底图
		BufferedImage buffImg = ImageIO.read(file);
*/		// 获取层图
		BufferedImage waterImg = getTransParentImage(OraWaterImg,RGB);
		// 创建Graphics2D对象，用在底图对象上绘图
		Graphics2D g2d = buffImg.createGraphics();
		int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
		int waterImgHeight = waterImg.getHeight();// 获取层图的高度
		// 在图形和图像中实现混合和透明效果
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// 绘制
		g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
		g2d.dispose();// 释放图形上下文使用的系统资源
		return buffImg;
	}
	
	private static void generateWaterFile(BufferedImage buffImg, String savePath){
		int temp = savePath.lastIndexOf(".") + 1;
		try {
		ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**合成图片
	 * @param filePath 源文件
	 * @param waterFilePath	水印文件
	 * @param savePath	合成图片保存路径
	 * @param x		距离右下角X的偏移量
	 * @param y		距离右下角Y的偏移量
	 * @param alpha	透明度0.1~1.0 1.0完全不透明
	 * @param RGB	Color.BLUE.getRGB()
	 * @throws IOException 
	 */
	public static void composeFile(String filePath,String waterFilePath,String savePath, int x,int y, float alpha,int RGB) throws IOException{
		File file = new File(filePath);
		File waterFile = new File(waterFilePath);
		BufferedImage image = null;
		image = waterMaker(file, waterFile, x, y, alpha,RGB);
		generateWaterFile(image, savePath);
	}
	
	/**合成图片
	 * @param filePath
	 * @param waterFilePath
	 * @param x
	 * @param y
	 * @param alpha
	 * @param RGB
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage composeFileToBufferImage(String filePath,String waterFilePath, int x,int y, float alpha,int RGB) throws IOException{
		File file = new File(filePath);
		File waterFile = new File(waterFilePath);
		BufferedImage image = null;
		image = waterMaker(file, waterFile, x, y, alpha,RGB);
		return image;
		/*generateWaterFile(image, savePath);*/
	}
	
	/**
	 * @param oraFile 		原文件
	 * @param waterFile 	水印文件
	 * @param x				距离右下角X的偏移量
	 * @param y				距离右下角Y的偏移量
	 * @param alpha			透明度0.1~1.0 1.0完全不透明
	 * @param RGB			Color.BLUE.getRGB()
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage composeFileWithOutSave(BufferedImage oraFile,BufferedImage waterFile,int x,int y, float alpha,int RGB)throws IOException{
		BufferedImage image = null;
		image = waterMaker( oraFile, waterFile, x, y, alpha,RGB);
		return image;
	}
	
	public static BufferedImage composeFileWithOutSave(BufferedImage oraFile, String waterFilePath ,int x, int y, float alpha,int RGB)throws IOException{
		BufferedImage image = null;
		File file = new File(waterFilePath);
		image = waterMaker(oraFile,ImageIO.read(file),x,y,alpha,RGB);
		return image;
	}
	
	// 判断是背景还是内容
    private static boolean colorInRange(int color) {
        int red = (color & 0xff0000) >> 16;// 获取color(RGB)中R位
        int green = (color & 0x00ff00) >> 8;// 获取color(RGB)中G位
        int blue = (color & 0x0000ff);// 获取color(RGB)中B位
        // 通过RGB三分量来判断当前颜色是否在指定的颜色区间内
        if (red >= color_range && green >= color_range && blue >= color_range){
            return true;
        };
        return false;
    }
    
    /**获取背景透明的层图
     * @param waterFile
     * @return
     * @throws IOException
     */
    public static BufferedImage getTransParentImage(File waterFile,int RGB) throws IOException{
    	BufferedImage image = ImageIO.read(waterFile);
    	 // 高度和宽度
        int height = image.getHeight();
        int width = image.getWidth();
        
        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片
        int alpha = 0; // 图片透明度
        // 外层遍历是Y轴的像素
        for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
            // 内层遍历是X轴的像素
            for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                // 对当前颜色判断是否在指定区间内
                if (colorInRange(rgb)){
                    alpha = 0;
                    // #AARRGGBB 最前两位为透明度
                    rgb = (alpha << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(x, y, rgb);
                }else{
                    // 设置为不透明,更改为蓝色
                    alpha = 255;
                    rgb = RGB;/*Color.BLUE.getRGB();*/
                    bufferedImage.setRGB(x, y, rgb);
                }
               
                
            }
        }
        // 绘制设置了RGB的新图片
        g2D.drawImage(bufferedImage, 0, 0, null);
        return bufferedImage;

       /* // 生成图片为PNG
        ImageIO.write(bufferedImage, "png", new File("C:/Users/grand/Desktop/lanzhou.png"));*/
    }
    
    
    /**
     * @param image
     * @param RGB
     * @return
     * @throws IOException
     */
    private static BufferedImage getTransParentImage(BufferedImage image,int RGB) throws IOException{
    	/*BufferedImage image = ImageIO.read(waterFile);*/
    	 // 高度和宽度
        int height = image.getHeight();
        int width = image.getWidth();
        
        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片
        int alpha = 0; // 图片透明度
        // 外层遍历是Y轴的像素
        for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
            // 内层遍历是X轴的像素
            for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                // 对当前颜色判断是否在指定区间内
                if (colorInRange(rgb)){
                    alpha = 0;
                    // #AARRGGBB 最前两位为透明度
                    rgb = (alpha << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(x, y, rgb);
                }else{
                    // 设置为不透明,更改为蓝色
                    alpha = 255;
                    rgb = RGB;/*Color.BLUE.getRGB();*/
                    bufferedImage.setRGB(x, y, rgb);
                }
               
                
            }
        }
        // 绘制设置了RGB的新图片
        g2D.drawImage(bufferedImage, 0, 0, null);
        return bufferedImage;

       /* // 生成图片为PNG
        ImageIO.write(bufferedImage, "png", new File("C:/Users/grand/Desktop/lanzhou.png"));*/
    }
    
    
    
    /**生成字符串图片
     * @param str		     目的字符串
     * @param fontSize    字体字号
     * @param font		     字体样式
     * @param width		     图片宽度
     * @param height	     图片高度	
     * @param tempFilePath 临时保存路径
     * @return
     */
    public static BufferedImage createStringImage(String str,Integer fontSize,String fontType,Integer width,Integer height,
    		boolean isBold){
    	if(StringUtils.isBlank(str)){
    		return null;
    	}
    	if(fontSize==null || fontSize==null || width == null || height == null){
    		return null;
    	}
    	/*File resultFile = new File(tempFilePath);*/
    	/*Font font = new Font("微软雅黑", Font.PLAIN, 32);*/
    	Font font;
    	if(isBold){
    		font = new Font(fontType, Font.BOLD, fontSize);
    	}else{
    		font = new Font(fontType, Font.PLAIN, fontSize);
    	}
    	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
    	Graphics g = image.getGraphics();
		g.setClip(0, 0, width, height);    
        g.setColor(Color.white);    
        g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景    
        g.setColor(Color.black);// 在换成黑色
        g.setFont(font);// 设置画笔字体
        /* 用于获得垂直居中y */ 
        Rectangle clip = g.getClipBounds();    
        FontMetrics fm = g.getFontMetrics(font);    
        int ascent = fm.getAscent();    
        int descent = fm.getDescent();    
        int y = (clip.height - (ascent + descent)) / 2 + ascent;    
        for (int i = 0; i < 6; i++) {// 256 340 0 680    
            g.drawString(str, i * 680, y);// 画出字符串    
        }    
        g.dispose();    
    	return image;
    }
    
    /**保存图片
     * @param image
     * @param savePath
     */
    public static void savePic(BufferedImage image,String savePath){
    	generateWaterFile(image, savePath);
    }
    
    /**改变位深度
     * @param oraFilePath
     * @return
     * @throws IOException
     */
    public static BufferedImage changeBitDepth(String oraFilePath) throws IOException{
    	BufferedImage image = ImageIO.read(new File(oraFilePath));
		
		ImageIcon imageIcon = new ImageIcon(image);
		//
		BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
		
		Graphics2D g2D = (Graphics2D) image2.getGraphics(); // 获取画笔
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片
        return image2;
    }
    /**改变位深度
     * @param oraFilePath
     * @return
     * @throws IOException
     */
    public static BufferedImage changeBitDepth(File oraFile) throws IOException{
    	BufferedImage image = ImageIO.read(oraFile);
		
		ImageIcon imageIcon = new ImageIcon(image);
		//
		BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
		
		Graphics2D g2D = (Graphics2D) image2.getGraphics(); // 获取画笔
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片
        return image2;
    }
}
