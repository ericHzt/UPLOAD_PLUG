package com.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * @author ERIC
 *
 */
public class PropertiesLoader {
	
	private final Properties properties ;
	
	public PropertiesLoader(String... resourcesPaths){
		properties = initialize(resourcesPaths);
	}
	
	/**读取配置文件
	 * @param resourcesPaths
	 * @return
	 */
	public Properties initialize(String... resourcesPaths){
		Properties properties = new Properties();
		for(String path : resourcesPaths){
			try {
				BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+java.io.File.separator+"resources"+java.io.File.separator+path));
				properties.load(reader);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "配置文件加载失败，请检查配置文件。\n"
	                    + e.getMessage());
			}
		}
		return properties;
	}
	
	/**获取配置文件的值
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		String value = null;
		if(properties==null){
			throw new NullPointerException();
		}
		if(properties.containsKey(key)){
			value = properties.getProperty(key);
		}
		return value;
	}
	
	/**取出String 类型的Property
	 * @param key
	 * @return
	 */
	public String getProperty(String key){
		String value = getValue(key);
		if(value==null){
			throw new NoSuchElementException();
		}
		return value;
	}
	
	/**取出Integer 类型的Property
	 * @param key
	 * @return
	 */
	public Integer getIntegerProperty(String key){
		String value = getValue(key);
		if(value == null){
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}
	
	/**
	 * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}
	
	/**更新或插入键值
	 * @param key 键
	 * @param value 值
	 */
	public boolean updateProperties(String key, String value){
		if(key==null||"".equals(key)){
			return false;
		}
		if(key == null || "".equals(value)){
			return false;
		}
		if(properties == null){
			return false;
		}
		OutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+java.io.File.separator+"resources"+java.io.File.separator+"app.properties");
			properties.setProperty(key, value);
			//更新记录
			properties.store(fos, "Update "+key+" "+value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	/**
	 * 配置文件完整性检查
	 */
	public boolean completeCheck(){
		boolean flag = false;
		if(properties==null){
			return flag;
		}
		try{
			String filePath = getProperty("filePath");
			String zFilePath = getProperty("zFilePath");
			/*String stampPath = getProperty("stampPath");*/
			String waterFilePath = getProperty("waterFilePath");
			String savePath = getProperty("savePath");
			String listSavePath = getProperty("listSavePath");
			String x = getProperty("x");
			String y = getProperty("y");
			String numX = getProperty("numX");
			String numY = getProperty("numY");
			String codeX = getProperty("codeX");
			String codeY = getProperty("codeY");
			String isStamp = getProperty("isStamp");
			String alpha = getProperty("alpha");
			
			String rectX = getProperty("rectX");
			String rectY = getProperty("rectY");
			String rectWidth = getProperty("rectWidth");
			String rectHeight = getProperty("rectHeight");
			if(StringUtils.isBlank(filePath)||StringUtils.isBlank(zFilePath)/*||StringUtils.isBlank(stampPath)*/||StringUtils.isBlank(waterFilePath)
					||StringUtils.isBlank(savePath)||StringUtils.isBlank(listSavePath)||StringUtils.isBlank(x)||StringUtils.isBlank(y)
					||StringUtils.isBlank(numX)||StringUtils.isBlank(numY)||StringUtils.isBlank(codeX)||StringUtils.isBlank(codeY)
					||StringUtils.isBlank(isStamp)||StringUtils.isBlank(alpha)||StringUtils.isBlank(rectX)
					||StringUtils.isBlank(rectY)||StringUtils.isBlank(rectWidth)||StringUtils.isBlank(rectHeight)){
				JOptionPane.showMessageDialog(null, "所有配置项都不能为空，请检查配置文件。");
				return flag;
			}
			if(!StringUtils.isInteger(x)||!StringUtils.isInteger(y)||!StringUtils.isInteger(numX)||!StringUtils.isInteger(numY)
					||!StringUtils.isInteger(codeX)||!StringUtils.isInteger(codeY)
					||!StringUtils.isInteger(rectX)||!StringUtils.isInteger(rectY)
					||!StringUtils.isInteger(rectWidth)||!StringUtils.isInteger(rectHeight)){
				JOptionPane.showMessageDialog(null, "偏移量配置应都为整数，请检查配置文件。");
				return flag;
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "配置文件不完整，请检查配置文件。");
			return flag;
		}
		flag = true;
		return flag;
	}

}
