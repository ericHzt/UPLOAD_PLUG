package com.app.utils.renew;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.app.utils.AuthorizationUtils;
import com.app.utils.DESEncryptUtil;
import com.app.utils.DateUtils;
import com.app.utils.DesUtil;
import com.app.utils.FileUtils;
import com.app.utils.RSAUtils2;
import com.app.utils.StringUtils;


/**
 * 公司可能存在多条续费记录，并且是多条提前续费的记录，按照最新一条记录，续费时间是叠加的。
 * @author carl
 *
 */
public class OfficeRenewUtils {
	
	public static final String RENEW_CACHE = "renewCache";
	public static final String RENEW_CACHE_OFFICE_ID = "renew_cache_office_id_";
	public static final String RENEW_CACHE_OFFICE_NUMBER = "renew_cache_office_number";  //允许创建公司数量
	public static final String RENEW_CACHE_OFFICE_STATUS = "renew_cache_office_status_";  //0整除，1到期，2冻结
	public static final String RENEW_CACHE_START_FLAG = "renew_cache_start_flag";  //true启动，被验证后设置为false
	public static final String RENEW_CACHE_START_TIME = "renew_cache_start_time";  //启动时间
	
	private static final Logger LOG = LoggerFactory.getLogger(OfficeRenewUtils.class);
	/**
	 * 解密
	 * @param tempRenew 需要解密的实体
	 * @return
	 * @throws Exception
	 */
	public static Renew decryptRenew(Renew tempRenew) throws Exception{
		//dateTime
		tempRenew.setDateTime(RSAUtils2.decryptByPublicKey(tempRenew.getDateTime(), tempRenew.getPublicKeyStr()));
		tempRenew.setDateTimeD(DateUtils.parseDate(tempRenew.getDateTime()));
		//taCode
		tempRenew.setTaCode(RSAUtils2.decryptByPublicKey(tempRenew.getTaCode(), tempRenew.getPublicKeyStr()));
		tempRenew.setTaCode(DESEncryptUtil.decrypt(tempRenew.getTaCode(), tempRenew.getCount() + tempRenew.getDateTime()));
		//endDate
		tempRenew.setEndDate(RSAUtils2.decryptByPublicKey(tempRenew.getEndDate(), tempRenew.getPublicKeyStr()));
		tempRenew.setEndDate(DESEncryptUtil.decrypt(tempRenew.getEndDate(), tempRenew.getTaCode() + tempRenew.getDateTime()));
		tempRenew.setEndDateD(DateUtils.parseDate(tempRenew.getEndDate()));
		//startDate
		tempRenew.setStartDate(RSAUtils2.decryptByPublicKey(tempRenew.getStartDate(), tempRenew.getPublicKeyStr()));
		tempRenew.setStartDate(DESEncryptUtil.decrypt(tempRenew.getStartDate(), tempRenew.getEndDate() + tempRenew.getDateTime()));
		tempRenew.setStartDateD(DateUtils.parseDate(tempRenew.getStartDate()));
		//uniqueCode
		tempRenew.setUniqueCode(RSAUtils2.decryptByPublicKey(tempRenew.getUniqueCode(), tempRenew.getPublicKeyStr()));
		tempRenew.setUniqueCode(DESEncryptUtil.decrypt(tempRenew.getUniqueCode(), tempRenew.getStartDate() + tempRenew.getDateTime()));
		//officeNumber
		tempRenew.setOfficeNumber(RSAUtils2.decryptByPublicKey(tempRenew.getOfficeNumber(), tempRenew.getPublicKeyStr()));
		tempRenew.setOfficeNumber(DESEncryptUtil.decrypt(tempRenew.getOfficeNumber(), tempRenew.getUniqueCode() + tempRenew.getDateTime()));
		if(StringUtils.isBlank(tempRenew.getOfficeNumber())){
			tempRenew.setOfficeNumber("0");
		}
		tempRenew.setOfficeNumberInt(Integer.parseInt(tempRenew.getOfficeNumber()));
		//taxpayerNumber
		tempRenew.setTaxpayerNumber(RSAUtils2.decryptByPublicKey(tempRenew.getTaxpayerNumber(), tempRenew.getPublicKeyStr()));
		tempRenew.setTaxpayerNumber(DESEncryptUtil.decrypt(tempRenew.getTaxpayerNumber(), tempRenew.getOfficeNumber() + tempRenew.getDateTime()));
		//mac
		tempRenew.setMac(RSAUtils2.decryptByPublicKey(tempRenew.getMac(), tempRenew.getPublicKeyStr()));
		tempRenew.setMac(DESEncryptUtil.decrypt(tempRenew.getMac(), tempRenew.getTaxpayerNumber() + tempRenew.getDateTime()));
		//officeId
		tempRenew.setOfficeId(RSAUtils2.decryptByPublicKey(tempRenew.getOfficeId(), tempRenew.getPublicKeyStr()));
		tempRenew.setOfficeId(DESEncryptUtil.decrypt(tempRenew.getOfficeId(), tempRenew.getMac() + tempRenew.getDateTime()));
		//data
		tempRenew.setData(tempRenew.getMac() + tempRenew.getTaCode() + tempRenew.getOfficeId() + tempRenew.getStartDate() + tempRenew.getEndDate());
		//sign
		boolean isSignTrue = RSAUtils2.signVerify(tempRenew.getData(), tempRenew.getPublicKeyStr(), tempRenew.getSign());
		tempRenew.setIsSignTrue(isSignTrue);
		
		return tempRenew;
	}
	
	/**
	 * 读取文件
	 * @param filePath  文件路径
	 * @param count 文件名中的数字
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<Renew> readDocument(String filePath, String count) throws ParserConfigurationException, SAXException, IOException{
		List<Renew> renewList = new ArrayList<Renew>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 1、创建一个DocumentBuilderFactory的对象
		DocumentBuilder db = dbf.newDocumentBuilder();  // 2、创建一个DocumentBuilder的对象
		Document document = db.parse(new File(filePath)); // 3、通过DocumentBuilder对象的parser方法加载
		Element root = (Element) document.getDocumentElement(); //最外层
		NodeList lisenceList = root.getElementsByTagName("LICENCE");  // 获取所有LISENCE节点的集合
		for (int k = 0; k < lisenceList.getLength(); k++) { 
			Renew renew = new Renew();
			renew.setCount(count);
			
			Node node = lisenceList.item(k);
			String id = ((Element) node).getAttribute("id");
			System.out.println("id-->" + id);
			renew.setId(id);
			 //获取book结点的子节点
            NodeList cList = node.getChildNodes();
            ArrayList<String> contents = new ArrayList<>();
            for(int j=1; j<cList.getLength(); j+=2){  
                Node cNode = cList.item(j);  
                String content = cNode.getFirstChild().getTextContent();  
                contents.add(content);
                System.out.println(cNode.getNodeName() + "-->" + cNode.getFirstChild().getTextContent());
                if(cNode.getNodeName().equals("NAME")){ //taCode
                	renew.setTaCode(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("VERSION")){ //taxpayerNumber
                	renew.setTaxpayerNumber(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("PRODUCTNAME")){ //officeId
                	renew.setOfficeId(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("TIME")){  //date time
                	renew.setDateTime(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("USER")){  //startDate
                	renew.setStartDate(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("PASSWORD")){  //endDdate
                	renew.setEndDate(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("TITLE")){  //publicKeyStr
                	renew.setPublicKeyStr(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("PARAMS")){  //sign
                	renew.setSign(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("TIMEOUTCLEAN")){ //uniqueCode
                	renew.setUniqueCode(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("DESCRIPTION")){ //officeNumber
                	renew.setOfficeNumber(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("OFFICALWEBSITE")){ //renewMac
                	renew.setMac(cNode.getFirstChild().getTextContent());
                }else if(cNode.getNodeName().equals("COPYRIGHT")){ //privateKeyStr
                	renew.setPrivateKeyStr(cNode.getFirstChild().getTextContent());
                }
            }  
            renewList.add(renew);
		}
		
		return renewList;
	}
	
	/**
	 * 根据路径解析注册码文件
	 * @param path
	 */
	public static boolean doRenew(String realPath){
		boolean bFlag = true;
		String filePath = realPath + "licences/";
		Date currentDate = new Date();
		//清空临时文件夹
		FileUtils.deleteDirectoryFile(filePath,".LICENCE");
		//解压license文件
        FileUtils.unZipFiles(filePath + "LICENCE.zip", filePath);
        
		//获取加密的注册文件名
		List<String> fileList = FileUtils.getLicenceList(filePath);
		List<String> tempFileList = new ArrayList<String>();
		//解密注册文件
		for(String str : fileList){
			try {
				DesUtil.decrypt(new File(filePath+ str), filePath, filePath  + "T_" + str, realPath);
				tempFileList.add("T_" + str);
			} catch (Exception e) {
				LOG.error("1001，注册/续费失败，文件操作失败，请检查续费文件是否有效！", e);
				bFlag = false;
				return bFlag;
			}
		}
		if(tempFileList == null || tempFileList.size() <= 0){
			LOG.error("1002，注册/续费失败，文件操作失败，请检查续费文件是否有效！");
			bFlag = false;
			return bFlag;
		}
		
		Hashtable<Renew, List<Renew>> hashtable = new Hashtable<Renew, List<Renew>>();
		//获取注册码文件的信息
		for(String str : tempFileList){
			String count = str.split("\\.")[0].replace("T_LICENCE", "");
    		List<Renew> renewList = new ArrayList<Renew>();
			try {
				renewList = OfficeRenewUtils.readDocument(filePath + str, count);
				//解密
				if(renewList != null && renewList.size() > 0){
					for(Renew tempRenew : renewList){
						
						tempRenew = OfficeRenewUtils.decryptRenew(tempRenew);
						//验证信息完整性
						if(StringUtils.isBlank(tempRenew.getTaCode()) 
								|| StringUtils.isBlank(tempRenew.getOfficeId())
								|| StringUtils.isBlank(tempRenew.getMac())
								|| StringUtils.isBlank(tempRenew.getOfficeNumber())
								|| StringUtils.isBlank(tempRenew.getStartDate())
								|| StringUtils.isBlank(tempRenew.getEndDate())
								|| StringUtils.isBlank(tempRenew.getSign())
								|| StringUtils.isBlank(tempRenew.getPublicKeyStr())
								|| StringUtils.isBlank(tempRenew.getDateTime())
								|| tempRenew.getStartDateD() == null
								|| tempRenew.getEndDateD() == null
								|| tempRenew.getDateTimeD() == null
								|| tempRenew.getOfficeNumberInt() == null){
							LOG.error("1011，注册/续费失败，解析注册码失败，请检查续费文件是否有效！");
							continue;
						}
						
						if(tempRenew.getIsSignTrue() == false){
							LOG.error("1012，注册/续费失败，解析注册码失败，请检查续费文件是否有效！");
							continue;
						}
					}
					
					hashtable.put(renewList.get(0), renewList);
				}else{
					LOG.error("1003，注册/续费失败，解析注册码失败，请检查续费文件是否有效！");
					continue;
				}
			} catch (ParserConfigurationException e) {
				LOG.error("1004，注册/续费失败，解析注册码失败，请检查续费文件是否有效！", e);
				continue;
			} catch (SAXException e) {
				LOG.error("1005，注册/续费失败，解析注册码失败，请检查续费文件是否有效！", e);
				continue;
			} catch (Exception e) {
				LOG.error("1006，续费失败，解析注册码失败，请检查续费文件是否有效！", e);
				continue;
			}
		}
		
		if(hashtable.isEmpty()){
			LOG.error("1010，注册/续费失败，文件操作失败，请检查续费文件是否有效！");
			//清空临时文件夹
	        //FileUtils.deleteDirectoryFile(tempFilePath);
			bFlag = false;
			return bFlag;
		}
		
		Enumeration<Renew> enu = hashtable.keys();
		while (enu.hasMoreElements()) {  
            Renew key = enu.nextElement();  
            List<Renew> tempList = hashtable.get(key);  
            if(tempList != null && tempList.size() > 0){
				//mac
				String localMac = AuthorizationUtils.getMac();
				if(localMac.indexOf(key.getMac()) < 0){
					LOG.error("1014，注册/续费失败，注册码信息不正确，请联系工程师确认！");
					bFlag = false;
			        continue;
				}
				
				for(Renew renew : tempList){
					//验证时间，需要获取当前时间。（当前时间验证）
					if(currentDate.after(renew.getEndDateD())){
						bFlag = false;
            		}
				}
				
			}else{
				bFlag = false;
				LOG.error("1020，续费失败，解析注册码失败，请检查续费文件是否有效！");
		        continue;
			}
        }
		return bFlag;
	}
	
	
	public static Map<String,String> getTaxNumber(String realPath){
		Map<String,String> map = new HashMap<String,String>();
		String filePath = realPath + "licences"+java.io.File.separator;//税号：91440101MA59MUC459
		Date currentDate = new Date();
		//清空临时文件夹
        FileUtils.deleteDirectoryFile(filePath,".LICENCE");
		//解压license文件
        FileUtils.unZipFiles(filePath + "LICENCE.zip", filePath);
		//获取加密的注册文件名
		List<String> fileList = FileUtils.getLicenceList(filePath);
		List<String> tempFileList = new ArrayList<String>();
		//解密注册文件
		for(String str : fileList){
			try {
				DesUtil.decrypt(new File(filePath+ str), filePath, filePath  + "T_" + str, realPath);
				tempFileList.add("T_" + str);
			} catch (Exception e) {
				LOG.error("1001，注册/续费失败，文件操作失败，请检查续费文件是否有效！", e);
			}
		}
		if(tempFileList == null || tempFileList.size() <= 0){
			LOG.error("1002，注册/续费失败，文件操作失败，请检查续费文件是否有效！");
		}
		
		Hashtable<Renew, List<Renew>> hashtable = new Hashtable<Renew, List<Renew>>();
		//获取注册码文件的信息
		for(String str : tempFileList){
			String count = str.split("\\.")[0].replace("T_LICENCE", "");
    		List<Renew> renewList = new ArrayList<Renew>();
			try {
				renewList = OfficeRenewUtils.readDocument(filePath + str, count);
				//解密
				if(renewList != null && renewList.size() > 0){
					for(Renew tempRenew : renewList){
						tempRenew = OfficeRenewUtils.decryptRenew(tempRenew);
						//验证信息完整性
						if(StringUtils.isBlank(tempRenew.getTaCode()) 
								|| StringUtils.isBlank(tempRenew.getOfficeId())
								|| StringUtils.isBlank(tempRenew.getMac())
								|| StringUtils.isBlank(tempRenew.getOfficeNumber())
								|| StringUtils.isBlank(tempRenew.getStartDate())
								|| StringUtils.isBlank(tempRenew.getEndDate())
								|| StringUtils.isBlank(tempRenew.getSign())
								|| StringUtils.isBlank(tempRenew.getPublicKeyStr())
								|| StringUtils.isBlank(tempRenew.getDateTime())
								|| tempRenew.getStartDateD() == null
								|| tempRenew.getEndDateD() == null
								|| tempRenew.getDateTimeD() == null
								|| tempRenew.getOfficeNumberInt() == null){
							LOG.error("1011，注册/续费失败，解析注册码失败，请检查续费文件是否有效！");
							continue;
						}
						
						if(tempRenew.getIsSignTrue() == false){
							LOG.error("1012，注册/续费失败，解析注册码失败，请检查续费文件是否有效！");
							continue;
						}
					}
					
					hashtable.put(renewList.get(0), renewList);
				}else{
					LOG.error("1003，注册/续费失败，解析注册码失败，请检查续费文件是否有效！");
					continue;
				}
			} catch (ParserConfigurationException e) {
				LOG.error("1004，注册/续费失败，解析注册码失败，请检查续费文件是否有效！", e);
				continue;
			} catch (SAXException e) {
				LOG.error("1005，注册/续费失败，解析注册码失败，请检查续费文件是否有效！", e);
				continue;
			} catch (Exception e) {
				LOG.error("1006，续费失败，解析注册码失败，请检查续费文件是否有效！", e);
				continue;
			}
		}
		
		if(hashtable.isEmpty()){
			LOG.error("1010，注册/续费失败，文件操作失败，请检查续费文件是否有效！");
		}
		
		Enumeration<Renew> enu = hashtable.keys();
		while (enu.hasMoreElements()) {  
            Renew key = enu.nextElement();  
            List<Renew> tempList = hashtable.get(key);  
            if(tempList != null && tempList.size() > 0){
				//mac
				String localMac = AuthorizationUtils.getMac();
				if(localMac.indexOf(key.getMac()) < 0){
					LOG.error("1014，注册/续费失败，注册码信息不正确，请联系工程师确认！");
			        continue;
				}
				
				for(Renew renew : tempList){
					//验证时间，需要获取当前时间。（当前时间验证）
					if(!currentDate.after(renew.getEndDateD())){
						//写入可用税号税号
						map.put("TAXPAYNUMBER", renew.getTaxpayerNumber());
            		}
				}
				
			}else{
				LOG.error("1020，续费失败，解析注册码失败，请检查续费文件是否有效！");
		        continue;
			}
        }
		return map;
	}
	
}
