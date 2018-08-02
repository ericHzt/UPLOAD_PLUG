package com.app.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.junit.Test;

import com.app.bean.SettingBean;
import com.app.common.Constants;
import com.app.common.PicFunction;
import com.app.filewatcher.FileActionCallback;
import com.app.filewatcher.WatchDir;
import com.app.frame.BaseFrame;
import com.app.ocr.Tesseract1;
import com.app.ocr.TesseractException;
import com.app.thread.WatchThread;
import com.app.utils.FileUtils;
import com.app.utils.NewImageUtils;
import com.app.utils.PropertiesLoader;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;

public class AppTest {

	@Test
	public void test() {
		
		fail("Not yet implemented");
	}
	
	@Test
	public void loderTest(){
		/*PropertiesLoader loader  = new PropertiesLoader("app.properties");
		System.out.println(loader.getProperty("jdbc.url"));*/
		/*System.out.println("1234567890".substring(7,8));
		String temp  = "123";
		String temp2 = temp;
		System.out.println("temp: "+temp.hashCode());
		System.out.println("temp2: "+temp2.hashCode());*/
		
		System.out.println(java.io.File.separator);
	}
	
	@Test
	public void ActiveXComponentTest(){
		/* System.out.println(System. getProperty( "java.library.path" ));*/
		ComThread.InitSTA();
		ActiveXComponent dotnetCom = new ActiveXComponent("TaxCardX.GoldTax");
		ComThread.Release();
	}
	@Test 
	public void propretiesTest(){
		Properties properties = new Properties();
		try {
		BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/resources/"+"app.properties"));
		properties.load(reader);
		OutputStream fos =  new FileOutputStream(System.getProperty("user.dir")+"/resources/"+"app.properties");
		properties.setProperty("test", "2");
		properties.store(fos, "Create or Update'"+"test"+"'1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	@Test 
	public void printTest(){
		/*try {
			BufferedImage image ;
			image = NewImageUtils.getTransParentImage(new File("D://ACTPRINTPLUG//filepath//stamp.jpg"), Color.RED.getRGB());
			image=(BufferedImage) image.getScaledInstance(1024, 768, Image.SCALE_SMOOTH);
			NewImageUtils.savePic(image, "D://ACTPRINTPLUG//savepath//test.jpg");
			NewImageUtils.composeFile("D://tempImage//4400173130 48313080.bmp", "D://ACTPRINTPLUG//filepath//stamp.jpg", "D://ACTPRINTPLUG//savepath//test.jpg", 50, 500, 1, Color.BLUE.getRGB());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		/*SettingBean bean = new SettingBean();
		bean.setListSavePath("D://ACTPRINTPLUG//listsavepath//");
		try {
			PicFunction.printListPic("D://tempImage//4400173130 48313080.bmp", "4400173130 48313080.", bean);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			BufferedImage image = ImageIO.read(new File("D://tempImage//4400173130 48313080.bmp"));
			
			ImageIcon imageIcon = new ImageIcon(image);
			
			BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
			
			Graphics2D g2D = (Graphics2D) image2.getGraphics(); // 获取画笔
	        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片
	        
	       NewImageUtils.savePic(image2, "D://ACTPRINTPLUG//listsavepath//test.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test 
	public void threadTest(){
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				int count = 0;
				while(true){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(count++);
				}
			}
		});
		thread.start();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(thread.isAlive());
		try {
			thread.interrupted();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*System.out.println(thread.isAlive());*/
		thread.start();
	}
	@Test
	public void ocrtest() throws TesseractException{
		Tesseract1 instance = new Tesseract1();
		System.out.println("doOCR on a BMP image with bounding rectangle");
        File imageFile = new File("C://Users//ERIC//Desktop//testpic//pic//4400174130 11168577.bmp");
        Rectangle rect = new Rectangle(100, 501, 409, 48); // define an equal or smaller region of interest on the image
        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        /*instance.setOcrEngineMode(2);*/
        String result = instance.doOCR(imageFile, rect);
        System.out.println("wtf");
        System.out.println(result.toUpperCase());
        /*assertEquals(expResult, result.substring(0, expResult.length()));*/
	}

	@Test
	public void getMacTest() throws UnknownHostException, SocketException{
		InetAddress ia = InetAddress.getLocalHost();
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		System.out.println("mac数组长度："+mac.length);
		StringBuffer sb = new StringBuffer("");
		for(int i=0; i<mac.length; i++) {
			if(i!=0) {
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i]&0xff;
			String str = Integer.toHexString(temp);
			System.out.println("每8位:"+str);
			if(str.length()==1) {
				sb.append("0"+str);
			}else {
				sb.append(str);
			}
		}
		System.out.println("本机MAC地址:"+sb.toString().toUpperCase());
	}
	
	@Test
	public void mytest(){
		File file = new File("C://Users//ERIC//Desktop//testpic//增值税专普发票数据导出20180704.xlsx");
		/*String url = "http://192.168.100.31:8080/a/test/testUploadFile/upload";*/
		String url = "http://localhost:8080/a/api/printApi/upload";
		Map<String,String> map = new HashMap<String,String>();
		map.put("invoice", BaseFrame.INVOICE_NUM);
		String value = FileUtils.uploadFile(url, map,file);
		String name = Constants.Dict.ILLEGAL_PARAMETER.getMessageByKey(value);
		System.out.println(name);
	}
}
