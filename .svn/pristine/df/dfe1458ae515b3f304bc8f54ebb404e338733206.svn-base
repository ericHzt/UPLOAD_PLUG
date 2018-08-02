package com.app.frame;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.app.bean.SettingBean;
import com.app.utils.PropertiesLoader;
import com.app.utils.StringUtils;

/**	JFrame 基类
 * @author ERIC
 *
 */
public class BaseFrame extends JFrame {
	//发票类型
	public final static String INVOICE_TYPE = "0";
	//发票列表类型
	public final static String INVOICE_LIST_TYPE = "1";
	//根目录
	public final static String rootPath = System.getProperty("user.dir")+java.io.File.separator;
	//图片url
	public final static String imagePath = rootPath+"resources"+java.io.File.separator+"images";
	//系统文件资源路径
	public final static String sysResourcePath = rootPath+"resources"+java.io.File.separator;
	//系统配置图片资源路径
	public final static String configFilesPath = rootPath+"resources"+java.io.File.separator+"configFiles"
			+java.io.File.separator;
	//文件锁路径
	public final static String uniqueLockPath = sysResourcePath+"uniqueLock.txt";
	
	//历史文件路径
	public final static String printRecordPath = sysResourcePath+"record.txt";
	
	//清单历史文件路径
	public final static String printListRecordPath = sysResourcePath+"listrecord.txt";
	
	//配置文件读取
	public PropertiesLoader initLoader;		
	
	//允许打印的税号
	public static String INVOICE_NUM = "91440101MA59MUC459";/*914401065505835702,914401065505835762,91440101MA59MUC459*/
	
	//税号个数
	public final static Integer INVOICE_COUNT = 1;
	
	//允许使用的设备
	public static String MAC_ADDRESS = "";
	
	
	public static Image appImage = null;
	
	public BaseFrame(){
		this.initLoader = new PropertiesLoader("app.properties");
		if(StringUtils.isBlank(INVOICE_NUM)){
			try {
				throw new NullPointerException();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "税号未绑定！");
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		try {
			appImage = javax.imageio.ImageIO.read(new File(imagePath+java.io.File.separator+"app.png"));
		} catch (IOException e) {
			appImage = null;
			e.printStackTrace();
		}
		this.setIconImage(appImage);
	}
	
}
