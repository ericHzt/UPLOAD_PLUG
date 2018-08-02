package com.app.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.app.bean.SettingBean;
import com.app.frame.BaseFrame;
import com.app.frame.MainFrame;
import com.app.frame.ViewPicFrame;
import com.app.ocr.Tesseract1;
import com.app.ocr.TesseractException;
import com.app.utils.FileUtils;
import com.app.utils.NewImageUtils;
import com.app.utils.PropertiesLoader;
import com.app.utils.StringUtils;
import com.sun.imageio.plugins.common.ImageUtil;

public class PicFunction{
	
	public static void viewPic(String path){
		/*JOptionPane.showMessageDialog(null, "点击了查看图片按钮。");*/
		if (true){
            ViewPicFrame thisClass = new ViewPicFrame(path);
            thisClass.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            Toolkit tookit = thisClass.getToolkit();
            Dimension dm = tookit.getScreenSize();
            thisClass.setLocation(
                    (dm.width - thisClass.getWidth()) / 2,
                    (dm.height - thisClass.getHeight()) / 2);
            thisClass.setVisible(true);
        }
	}
	
	/**图片预览
	 * @param image
	 * @param type 0为发票类型 1为清单类型
	 */
	public static void viewPic(BufferedImage image,String type){
		if (true){
            ViewPicFrame thisClass = new ViewPicFrame(image,type);
            thisClass.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            Toolkit tookit = thisClass.getToolkit();
            Dimension dm = tookit.getScreenSize();
            thisClass.setLocation(
                    (dm.width - thisClass.getWidth()) / 2,
                    (dm.height - thisClass.getHeight()) / 2);
            thisClass.setVisible(true);
        }
	}
	
	/**打印前清空文件夹
	 * 
	 */
	public static void emptyInit(String ... emptyPath){
		if(emptyPath == null){
			return ;
		}
		for(String tempString : emptyPath ){
			FileUtils.deleteFile(new File(tempString));
		}
		
		
	}
	
	/**ocr税号检查打印图片
	 * @param waterFilePath 识别文件路径
	 * @param saveName	识别文件名
	 * @param settingBean 带参数的bean
	 * @param initInvoiceNum	初始化税号用于识别匹配
	 * @param instance	识别实例
	 * @return
	 */
	public static boolean printPicsWithCheck(String waterFilePath,String saveName,
			SettingBean settingBean,String initInvoiceNum,Tesseract1 instance,Rectangle rect){
		boolean flag = false;
		String aimInvoiceNum = null;
		try {
			aimInvoiceNum  = checkInvoiceNum(initInvoiceNum, instance,waterFilePath,rect);
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		if(!StringUtils.isBlank(aimInvoiceNum)){
			flag = printPics(waterFilePath, saveName, settingBean,aimInvoiceNum);
		}
		return flag;
	}

	
	/**打印发票图片方法
	 * @param waterFilePath  数据源文件路径
	 * @param saveName		  保存文件名称
	 */
	public static boolean printPics(String waterFilePath,String saveName,
			SettingBean settingBean,String aimInvoiceNum){
		if(settingBean==null || StringUtils.isBlank(settingBean.getFilePath()) || 
				StringUtils.isBlank(settingBean.getSavePath())){
			return false;
		}
		boolean flag = false;
		if(waterFilePath == null || "".equals(waterFilePath)
				||saveName == null || "".equals(saveName)){
			return flag;
		}
		String invoiceNum;	//发票号码
		String invoiceCode;	//发票代码
		String fileName; 	//保存文件名
		String invoiceType; //发票类型
		String[] tempList = saveName.split(" ");
		if(tempList!=null && tempList.length == 2){
			invoiceCode = tempList[0];
			invoiceNum = tempList[1];
			invoiceNum = invoiceNum.substring(0,invoiceNum.lastIndexOf("."));
			invoiceType = getInvoiceType(invoiceCode);
			if(StringUtils.isBlank(invoiceType)){
				return flag;
			}
		}else{
			return flag;
		}
		fileName = saveName.replace(" ", "");
		fileName =fileName.substring(0, fileName.lastIndexOf(".")+1);
		fileName = fileName+"jpg";
		
    	//可配置项
    	/*int x = 100;
    	int y = 50;
    	float alpha = 1;*/
    	try {
    		//发票图片
    		BufferedImage rootImage = null;
    		if("0".equals(invoiceType)){
	    		rootImage = NewImageUtils.composeFileToBufferImage(settingBean.getFilePath(), waterFilePath,settingBean.getX(), settingBean.getY()
	    				, settingBean.getAlpha(),Color.BLUE.getRGB());
    		}
    		if("1".equals(invoiceType)){
    			rootImage = NewImageUtils.composeFileToBufferImage(settingBean.getzFilePath(), waterFilePath,settingBean.getX(), settingBean.getY()
	    				, settingBean.getAlpha(),Color.BLUE.getRGB());
    		}
    		//发票号码图片
    		BufferedImage stringImage = NewImageUtils.createStringImage(invoiceNum, 55, "仿宋", 250, 100,true);
    		//发票代码图片
    		BufferedImage stringImage2 = NewImageUtils.createStringImage(invoiceCode, 60, "仿宋", 350, 100,false);
    		//合成发票号和发票图片
    		rootImage = NewImageUtils.composeFileWithOutSave(rootImage, stringImage, settingBean.getNumX(), settingBean.getNumY()
    				, settingBean.getAlpha(), new Color(Integer.parseInt("191970", 16)).getRGB());
    		//合成发票代码和发票图片
    		rootImage = NewImageUtils.composeFileWithOutSave(rootImage, stringImage2, settingBean.getCodeX(), settingBean.getCodeY()
    				, settingBean.getAlpha(), Color.BLACK.getRGB());
    		if(settingBean.getIsStamp()){
    			String stampPath = settingBean.getStampMap().get(aimInvoiceNum);
    			if(StringUtils.isBlank(stampPath)){
    				return flag;
    			}
    			/*System.out.println(stampPath);*/
    			rootImage = NewImageUtils.composeFileWithOutSave(rootImage, stampPath, settingBean.getStampX(), settingBean.getStampY(),
    					settingBean.getAlpha(), Color.RED.getRGB());
    		}
    		StringBuilder sb = new StringBuilder();
    		sb.append(settingBean.getRootDic());
    		sb.append(aimInvoiceNum);
    		sb.append(java.io.File.separator);
    		sb.append(settingBean.getSavePath());
    		sb.append(fileName);
    		NewImageUtils.savePic(rootImage, sb.toString());
    		//复制文件
    		if(settingBean.getIsUpload()){
    			FileUtils.copyFile(new File(sb.toString()), new File(settingBean.getUploadInvoicePath()+saveName));
    		}
		} catch (IOException e1) {
			System.out.println("图片生成异常！");
			e1.printStackTrace();
			return flag;
		}
    	flag = true;
    	return flag;
	}
	
	/**根据发票代码获取发票类型
	 * @param invoiceCode 发票代码
	 * @return 0为增值税普通发票 1为增值税专用发票
	 */
	public static String getInvoiceType(String invoiceCode){
		if(StringUtils.isBlank(invoiceCode)){
			return null;
		}
		if(invoiceCode.length() == 12){
			return "0";
		}
		if(invoiceCode.length() ==10){
			String tempStr = invoiceCode.substring(7,8);
			if(tempStr.equals("1")){
				return "1";
			}
			if(tempStr.equals("3")){
				return "0";
			}
			
		}
		return null;
	}
	
	/**
	 * @param oraFilePath  原始文件路径
	 * @param saveName	        文件名称
	 * @param settingBean	
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static boolean printListPic(String oraFilePath,String saveName,
			SettingBean settingBean,String selectedInvoice){
		boolean flag = false;
		String fileName; 	//保存文件名
		fileName = saveName.replace(" ", "");
		fileName =fileName.substring(0, fileName.lastIndexOf(".")+1);
		fileName = fileName+"jpg";
		//如果选择税号为空，默认一个税号
		if(StringUtils.isBlank(selectedInvoice)){
			selectedInvoice = settingBean.getInvoice_num().split(",")[0];
		}
		String stampPath = settingBean.getStampMap().get(selectedInvoice);
		if(!settingBean.getIsStamp()){
			try {
				FileInputStream input = new FileInputStream(oraFilePath);
				FileOutputStream output = new FileOutputStream(settingBean.getRootDic()+selectedInvoice+java.io.File.separator+settingBean.getListSavePath()+fileName);
				FileOutputStream outputCopy = new FileOutputStream(settingBean.getUploadInvoiceListPath()+saveName);
				int in=input.read();
				if(settingBean.getIsUpload()){
				    while(in!=-1){
				           output.write(in);
				           //复制一个副本用于上传
				           outputCopy.write(in);
				           in=input.read();
				    }
				}else{
					while(in!=-1){
				           output.write(in);
				           in=input.read();
				    }
				}
			    input.close();
			    output.close();
			    outputCopy.close();
			} catch (Exception e) {
				System.out.println("打印文件失败");
				return flag;
			}
			
		}else{
			try {
				BufferedImage newImage = NewImageUtils.changeBitDepth(oraFilePath);
				BufferedImage saveImage = NewImageUtils.composeFileWithOutSave(newImage, stampPath
						, settingBean.getlStampX(),settingBean.getlStampY(), settingBean.getAlpha(), Color.RED.getRGB());
				NewImageUtils.savePic(saveImage,  settingBean.getRootDic()+selectedInvoice+java.io.File.separator+settingBean.getListSavePath()+fileName);
			} catch (IOException e) {
				System.out.println("打印文件失败");
				e.printStackTrace();
				return flag;
			}
		}
		flag = true;
		return flag;
	}
	
	/**发票打印测试预览
	 * @param file
	 * @param testFile
	 * @param stampFile
	 * @param isStamp
	 * @param x
	 * @param y
	 * @param numX
	 * @param numY
	 * @param codeX
	 * @param codeY
	 * @param stampX
	 * @param stampY
	 * @return
	 */
	public static BufferedImage printInvoiceTest(File file,File testFile,File stampFile,boolean isStamp,String x,String y,
			String numX,String numY, String codeX,String codeY,String stampX,String stampY){
		BufferedImage image = null;
		String testInvoiceNum = "48313037";
		String testInvoiceCode ="4400173130";
		if(file == null || file.length()==0 ||testFile == null || testFile.length()==0){
			JOptionPane.showMessageDialog(null, "测试文件不完整，请检测测试文件路径和测试文件命名是否正确");
			return null;
		}
		if(isStamp && (stampFile == null || stampFile.length() == 0)){
			JOptionPane.showMessageDialog(null, "测试文件不完整，请检测测试文件路径和测试文件命名是否正确");
			return null;
		}
		try {
			image = NewImageUtils.composeFileWithOutSave(ImageIO.read(file), ImageIO.read(testFile)
					, Integer.parseInt(x), Integer.parseInt(y), 1, Color.BLUE.getRGB());
			//发票号码图片
    		BufferedImage stringImage = NewImageUtils.createStringImage(testInvoiceNum, 55, "仿宋", 250, 100,true);
    		//发票代码图片
    		BufferedImage stringImage2 = NewImageUtils.createStringImage(testInvoiceCode, 60, "仿宋", 350, 100,false);
    		//合成发票号和发票图片
    		image = NewImageUtils.composeFileWithOutSave(image, stringImage, Integer.parseInt(numX), Integer.parseInt(numY)
    				, 1, new Color(Integer.parseInt("191970", 16)).getRGB());
    		//合成发票代码和发票图片
    		image = NewImageUtils.composeFileWithOutSave(image, stringImage2, Integer.parseInt(codeX), Integer.parseInt(codeY)
    				, 1, Color.BLACK.getRGB());
    		if(isStamp){
    			image = NewImageUtils.composeFileWithOutSave(image,ImageIO.read(stampFile),Integer.parseInt(stampX), Integer.parseInt(stampY)
    					,1, Color.RED.getRGB());
    		}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "请输入合法的整数数字");
			return null;
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "测试文件不完整，请检测测试文件路径和测试文件命名是否正确");
			e1.printStackTrace();
			return null;
		}
		return image;
	}
	
	public static BufferedImage printInvoiceListTest(File file,File stampFile,boolean isStamp,String x,String y){
		BufferedImage image = null;
		if(file == null || file.length() == 0 ){
			JOptionPane.showMessageDialog(null, "测试文件不完整，请检测测试文件路径和测试文件命名是否正确");
			return null;
		}
		if(isStamp&&(stampFile == null || stampFile.length() == 0)){
			JOptionPane.showMessageDialog(null, "测试文件不完整，请检测测试文件路径和测试文件命名是否正确");
			return null;
		}
		
		try {
			image = NewImageUtils.changeBitDepth(file);
			if(isStamp){
				image = NewImageUtils.composeFileWithOutSave(image, ImageIO.read(stampFile)
						, Integer.parseInt(x), Integer.parseInt(y), 1, Color.RED.getRGB());
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "请输入合法的整数数字");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "测试文件IO错误");
			e.printStackTrace();
			return null;
		}
		return image;
	}
	
	
	/**税号识别
	 * @return 返回识别税号，如果为空则不是目的税号之一
	 * @throws TesseractException 
	 */
	public static String checkInvoiceNum(String initInvoiceNum,Tesseract1 instance,String waterFilePath,Rectangle rect) throws Exception{
		if(StringUtils.isBlank(initInvoiceNum)){
			return null;
		}
		if(instance == null){
			instance = new Tesseract1();
		}
		File imageFile = new File(waterFilePath);
        /*Rectangle rect = new Rectangle(settingBean.getRectX(), settingBean.getRectY(), settingBean.getRectWidth(), settingBean.getRectHeight()); */// define an equal or smaller region of interest on the image
        String result = instance.doOCR(imageFile, rect);
        if(!StringUtils.isBlank(result)){
        	result = result.replaceAll("\r|\n", "");
        }else{
        	return null;
        }
        String invoiceList[] = initInvoiceNum.split(",");
        //ocr 识别模糊适配
        if(invoiceList!=null && invoiceList.length>0){
        	for(String str : invoiceList){
        		String initStr = str.replaceAll("O", "0");
        		String tempStr = result.replaceAll("O", "0");
        		//小写initStr不用转
        		tempStr = tempStr.replaceAll("l", "1");
        		if(tempStr.equals(initStr)){
        			return str;
        		}
        	}
        }
        /*if(initInvoiceNum.replace("O", "0").contains(result.replace("O", "0"))){
        	return result;
        }*/
		return null;
	}
	
	public static String ocrTest(String waterFilePath,Rectangle rect) throws TesseractException{
		Tesseract1 instance = new Tesseract1();
		File imageFile = new File(waterFilePath);
		String result = instance.doOCR(imageFile, rect);
		return result;
	}

}
