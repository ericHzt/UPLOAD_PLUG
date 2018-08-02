package com.app.frame;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.app.bean.SettingBean;
import com.app.utils.FileUtils;
import com.app.utils.PropertiesLoader;
import com.app.utils.StringUtils;

/**带配置缓存的基类 不是单例 勿重复使用
 * @author ERIC
 *
 */
public class BaseSettingFrame extends BaseFrame {
	//保存文件根目录
	public String rootDic;		
	//普票底版文件路径
	public String filePath;
	//专票底版文件路径
	public String zFilePath;
	//印章文件路径
	public Map<String, String> stampMap;
	//水印文件路径
	public String waterFilePath;
	//上传文件路径
	public String uploadFilePath;
	//保存路径
	public String savePath;
	//发票保存路径
	public String listSavePath;
	//测试文件路径
	public String testWaterFile;
	//测试清单文件路径
	public String testInvoiceList;
			
	//发票数据偏移量配置
	public Integer x;
	
	public Integer y;
	//发票号码偏移量配置
	public Integer numX;
	
	public Integer numY;
	//发票代码偏移量配置
	public Integer codeX;
	
	public Integer codeY;
	//发票印章偏移量配置
	public Integer stampX;
	
	public Integer stampY;
	//清单印章偏移量配置
	public Integer lStampX;
	
	public Integer lStampY;
	//有印章标志
	public Boolean isStamp;
	
	//透明度
	public Integer alpha;
	
	//debug
	public Boolean debug;
	
	public SettingBean settingBean;
	
	public Integer rectX;
	
	public Integer rectY;
	
	public Integer rectWidth;
	
	public Integer rectHeight;
	
	public  Rectangle rect ;//= new Rectangle(439, 1003, 504, 45)
	
	public Set<String> recordSet; //发票打印历史
	
	public Set<String> listRecordSet;//发票清单打印历史
	
	public Boolean isUpload;
	
	public String uploadUrl;
	
	
	
	public BaseSettingFrame(){
		super();
		settingBean = new SettingBean();
		this.stampMap = new HashMap<String, String>();
		String invoiceList[] = INVOICE_NUM.split(",");
		for(String str : invoiceList){
			if(!StringUtils.isBlank(str)){
				this.stampMap.put(str,configFilesPath+str+".jpg");
			}
		}
		this.rootDic = initLoader.getProperty("rootDic");
		this.filePath = configFilesPath+initLoader.getProperty("filePath");
		this.zFilePath = configFilesPath+initLoader.getProperty("zFilePath");
		this.testInvoiceList = configFilesPath+initLoader.getProperty("testInvoiceList");
		this.testWaterFile = configFilesPath+initLoader.getProperty("testWaterFile");
		this.waterFilePath = initLoader.getProperty("waterFilePath");
		this.savePath = initLoader.getProperty("savePath");
		this.listSavePath = initLoader.getProperty("listSavePath");
		this.x = initLoader.getIntegerProperty("x");
		this.y = initLoader.getIntegerProperty("y");
		this.numX = initLoader.getIntegerProperty("numX");
		this.numY = initLoader.getIntegerProperty("numY");
		this.codeX = initLoader.getIntegerProperty("codeX");
		this.codeY = initLoader.getIntegerProperty("codeY");
		this.isStamp = initLoader.getBoolean("isStamp");
		this.debug = initLoader.getBoolean("debug");
		this.alpha = initLoader.getIntegerProperty("alpha");
		/*if(isStamp){
			
		}*/
		this.stampX = initLoader.getIntegerProperty("stampX");
		this.stampY = initLoader.getIntegerProperty("stampY");
		this.lStampX = initLoader.getIntegerProperty("lStampX");
		this.lStampY = initLoader.getIntegerProperty("lStampY");
		
		this.rectX = initLoader.getIntegerProperty("rectX");
		this.rectY = initLoader.getIntegerProperty("rectY");
		this.rectWidth = initLoader.getIntegerProperty("rectWidth");
		this.rectHeight = initLoader.getIntegerProperty("rectHeight");
		this.isUpload = initLoader.getBoolean("isUpload");
		this.uploadFilePath = initLoader.getProperty("uploadFilePath");
		this.uploadUrl = initLoader.getProperty("uploadUrl");
		rect = new Rectangle(this.rectX,this.rectY,this.rectWidth,this.rectHeight);
		
		settingBean.setX(this.x);
		settingBean.setY(this.y);
		settingBean.setNumX(this.numX);
		settingBean.setNumY(this.numY);
		settingBean.setCodeX(this.codeX);
		settingBean.setCodeY(this.codeY);
		settingBean.setIsStamp(this.isStamp);
		settingBean.setStampX(this.stampX);
		settingBean.setStampY(this.stampY);
		settingBean.setAlpha(this.alpha);
		settingBean.setFilePath(this.filePath);
		/*settingBean.setStampPath(this.stampPath);*/
		settingBean.setWaterFilePath(this.waterFilePath);
		settingBean.setSavePath(this.savePath);
		settingBean.setzFilePath(this.zFilePath);
		settingBean.setListSavePath(this.listSavePath);
		settingBean.setlStampX(this.lStampX);
		settingBean.setlStampY(this.lStampY);
		settingBean.setDebug(this.debug);
		settingBean.setRectX(this.rectX);
		settingBean.setRectY(this.rectY);
		settingBean.setRectWidth(this.rectWidth);
		settingBean.setRectHeight(this.rectHeight);
		settingBean.setTestWaterFile(this.testWaterFile);
		settingBean.setRect(this.rect);
		settingBean.setTestInvoiceList(this.testInvoiceList);
		settingBean.setRootDic(this.rootDic);
		settingBean.setStampMap(stampMap);
		settingBean.setInvoice_num(INVOICE_NUM);
		settingBean.setIsUpload(this.isUpload);
		settingBean.setUploadFilePath(this.uploadFilePath);
		settingBean.setUploadUrl(this.uploadUrl);
		settingBean.setUploadFilesPath(this.uploadFilePath+"file"+java.io.File.separator);
		settingBean.setUploadInvoicePath(this.uploadFilePath+"invoiceImage"+java.io.File.separator);
		settingBean.setUploadInvoiceListPath(this.uploadFilePath+"invoiceListImage"+java.io.File.separator);
		
		try {
			/*recordSet = FileUtils.fileToSet(printRecordPath);
			listRecordSet = FileUtils.fileToSet(printListRecordPath);*/
			recordSet = FileUtils.nioFileToSet(printRecordPath);
			listRecordSet = FileUtils.fileToSet(printListRecordPath);
		} catch (FileNotFoundException e) {
			recordSet =Collections.synchronizedSet(new HashSet<String>());
			System.err.println("没有打印历史记录文件!");
		}
	}
}
