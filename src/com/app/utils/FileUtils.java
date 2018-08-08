package com.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.app.common.Constants;
import com.google.common.collect.Lists;

/**文件处理工具类
 * @author ERIC
 *
 */
public class FileUtils {
	
	/**删除文件夹下的所有文件
	 * @param file
	 */
	public static boolean deleteFile(File file){
		boolean flag = false;
		if(file == null ){
			return flag;
		}
		if(file.isFile()){ //是文件而不是文件夹
			file.delete();
			flag = true;
		}else{
			String childFilePaths[] = file.list();
			for(String childFilePath : childFilePaths){
				File childFile=new File(file.getAbsolutePath()+"\\"+childFilePath);  
                deleteFile(childFile);  
			}
		}
		return flag;
	}
	
	/**获取文件夹下的文件名列表
	 * @param filePath
	 * @return
	 */
	public static String[] getFileList(String filePath){
		File file = new File(filePath);
		if(file == null ){
			return null;
		}
		if(file.isFile()){
			return null;
		}else{
			String childFilePaths[] = file.list();
			return childFilePaths;
		}
	}
	
	/**文件锁  JVM退出后自动释放锁
	 * @return
	 * @throws Exception 
	 */
	public static boolean fileLock(String filePath) throws Exception{
		boolean flag = false;
		if(!new File(filePath).exists()){
			return flag;
		}
		File file = new File(filePath);
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = randomAccessFile.getChannel();
		FileLock fileLock = null;
		fileLock = fileChannel.tryLock();
		if(fileLock != null){
			flag = true;
		}
		/*randomAccessFile.close();*/
		return flag;
	}
	
	/**文件锁释放 
	 * @return
	 */
	public static boolean relaseFileLock(FileLock fileLock){
		boolean flag = false;
		if(fileLock == null){
			return flag;
		}
		return flag;
	}
	
	/**写入目标文件
	 * @param str
	 * @param path
	 * @throws IOException
	 */
	public static void writeFile(String str,String path) throws IOException{
		if(StringUtils.isBlank(str)){
			throw new NullPointerException();
		}
		File file = new File(path);
		if(!file.exists()){
			file.getParentFile().mkdir();
			file.createNewFile();
		}
		FileOutputStream ops = new FileOutputStream(file);
		ops.write(str.getBytes());
		ops.close();
	}
	
	/**文件拷贝
	 * @param fromFile
	 * @param toFile
	 * @throws IOException
	 */
	public static void copyFile(File fromFile,File toFile) throws IOException{
		FileInputStream in = new FileInputStream(fromFile);
		FileOutputStream out = new FileOutputStream(toFile);
		byte[] b = new byte[1024];
		int n = 0;
		while((n=in.read(b))!=-1){
			out.write(b);
		}
		in.close();
		out.close();
	}
	
	/**列出目标文件夹下的文件名称 不包含目录
	 * @param file
	 * @return
	 */
	public static ArrayList<String> listFile(File file){
		if(!file.isDirectory()){
			return null;
		}
		
		File[] childFiles = file.listFiles();
		ArrayList<String> fileNames = new ArrayList<String>();
		for(File tempFile:childFiles){
			if(tempFile.isFile()){
				fileNames.add(tempFile.getName());
			}
		}
		return fileNames;
	}
	
	/**读出文件信息
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException{
		File file = new File(path);
		String str = null;
		if(!file.exists()){
			return null;
		}
		FileInputStream fis = new FileInputStream(file);
		Long fileLength = file.length();
		byte strByte[] = new byte[fileLength.intValue()];
		fis.read(strByte);
		fis.close();
		str = new String(strByte);
		return str;
	}
	
	/**
	 * 
	 * @param dirName
	 * @param
	 * @return
	 */
	public static boolean deleteDirectoryFile(String dirName,String prefix) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				if(files[i].getName().toUpperCase().contains(prefix)){
					flag = FileUtils.deleteFile(files[i].getAbsolutePath());
				}
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileUtils.deleteDirectory(files[i]
						.getAbsolutePath(),prefix);
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 * 删除目录及目录下的文件
	 * 
	 * @param dirName 被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName,String prefix) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileUtils.deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileUtils.deleteDirectory(files[i]
						.getAbsolutePath(),prefix);
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}

	}
	
	
	/**
	 * 
	 * 删除单个文件
	 * 
	 * @param fileName 被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	
	/**
	 * 解压缩ZIP文件，将ZIP文件里的内容解压到descFileName目录下
	 * @param zipFileName 需要解压的ZIP文件
	 * @param descFileName 目标文件
	 */
	public static boolean unZipFiles(String zipFileName, String descFileName) {
		String descFileNames = descFileName;
		if (!descFileNames.endsWith(File.separator)) {
			descFileNames = descFileNames + File.separator;
		}		
        try {
			// 根据ZIP文件创建ZipFile对象
			ZipFile zipFile = new ZipFile(zipFileName);
			ZipEntry entry = null;
			String entryName = null;
			String descFileDir = null;
			byte[] buf = new byte[4096];
			int readByte = 0;
			// 获取ZIP文件里所有的entry
			@SuppressWarnings("rawtypes")
			Enumeration enums = zipFile.getEntries();
			// 遍历所有entry
			while (enums.hasMoreElements()) {
				entry = (ZipEntry) enums.nextElement();
				// 获得entry的名称
				entryName = entry.getName();
				descFileDir = descFileNames + entryName;
				if (entry.isDirectory()) {
					// 如果entry是一个目录，则创建目录
					new File(descFileDir).mkdirs();
					continue;
				} else {
					// 如果entry是一个文件，则创建父目录
					new File(descFileDir).getParentFile().mkdirs();
				}
				File file = new File(descFileDir);
				// 打开文件输出流
				OutputStream os = new FileOutputStream(file);
				// 从ZipFile对象中打开entry的输入流
		        InputStream is = zipFile.getInputStream(entry);
				while ((readByte = is.read(buf)) != -1) {
					os.write(buf, 0, readByte);
				}
				os.close();
				is.close();
			}
			zipFile.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 只获取注册文件
	 * 获目录下的文件列表的名称
	 * @return 文件列表
	 */
	public static List<String> getLicenceList(String path) {
		List<String> files = Lists.newArrayList();
		File dir = new File(path);
		if(dir != null && dir.isDirectory()){
			for (String subFiles : dir.list()) {
				File file = new File(dir + "/" + subFiles);
				if(getFileExtension(file.getName()).equals("licence")){
					files.add(file.getName());
				}
			}
		}
		return files;
	}
	
	/**
	 * 获取文件扩展名(返回小写)
	 * @param fileName 文件名
	 * @return 例如：test.jpg  返回：  jpg
	 */
	public static String getFileExtension(String fileName) {
		if ((fileName == null) || (fileName.lastIndexOf(".") == -1) || (fileName.lastIndexOf(".") == fileName.length() - 1)) {
			return null;
		}
		return StringUtils.lowerCase(fileName.substring(fileName.lastIndexOf(".")+1));
	}
	
	/**原有文件基础上写入文件
	 * @param filePath
	 */
	public static void writeToFile(String filePath,String str){
		File file = new File(filePath);
		writeToFile(file, str);
		
	}
	public static void writeToFile(File file,String str){
		if(StringUtils.isBlank(str)){
			return ;
		}
		/*if(!file.exists()){
			file.mkdir()
		}*/
		try {
			FileOutputStream fis = new FileOutputStream(file,true);
			PrintStream ps =  new PrintStream(fis);
			ps.append(str);
			ps.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**txt file 转 set
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Set<String> fileToSet(String filePath) throws FileNotFoundException{
		if(StringUtils.isBlank(filePath)){
			throw new FileNotFoundException();
		}
		File file = new File(filePath);
		return fileToSet(file);
	}
	
	public static Set<String> fileToSet(File file){
		HashSet<String> recordSet = new HashSet<String>();
		try {
			String temp;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((temp=reader.readLine())!=null){
				/*System.out.println(temp);*/
				recordSet.add(temp);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.synchronizedSet(recordSet);
	}
	
	/**nio 读取文件方式
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Set<String> nioFileToSet(String filePath) throws FileNotFoundException{
		if(StringUtils.isBlank(filePath)){
			throw new FileNotFoundException();
		}
		File file = new File(filePath);
		return nioFileToSet(file);
	}
	
	public static Set<String> nioFileToSet(File file){
		HashSet<String> recordSet = new HashSet<String>();
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			FileChannel fileChannel = randomAccessFile.getChannel();
			//1M内存
			ByteBuffer bb = ByteBuffer.allocate(1024*1024);
			//一行大概30个字节 可按需扩充
			ByteBuffer stringBuffer = ByteBuffer.allocate(30);
			while(fileChannel.read(bb)!=-1){
				//转换为读模式
				bb.flip();
				while(bb.hasRemaining()){
					byte b = bb.get();
					if(b == 10 || b == 13){
						stringBuffer.flip();
						String temp= Charset.forName("utf-8").decode(stringBuffer).toString();
						if(!StringUtils.isBlank(temp)){
							recordSet.add(temp);
						}
						stringBuffer.clear();
					}else{
						if(stringBuffer.hasRemaining()){
							stringBuffer.put(b);
						}else{
							stringBuffer = reAllocate(stringBuffer);
							stringBuffer.put(b);
						}
					}
				}
				bb.clear();
			}
			fileChannel.close();
			randomAccessFile.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Collections.synchronizedSet(recordSet);
	}
	
	private static ByteBuffer reAllocate(ByteBuffer stringBuffer){
		//原始长度
		int capacity = stringBuffer.capacity();
		//double
		byte[] newBuffer = new byte[capacity * 2];
		//拷贝到新申请的内存空间
	    System.arraycopy(stringBuffer.array(), 0, newBuffer, 0, capacity);
	    //返还新申请的空间，position定在拷贝前最后的位置
	    return (ByteBuffer) ByteBuffer.wrap(newBuffer).position(capacity);
	}
	
	/**httppost 文件上传
	 * @param url
	 * @param file
	 * @return JSON对象
	 *
	 */
	public static JSONObject uploadFile(String url,Map<String,String> map,File file){
		JSONObject failJson = new JSONObject();
		if(StringUtils.isBlank(url)){
			failJson.put("code", Constants.Dict.NULL_PARAMETER.getValue());
			return failJson;
		}
		if(!file.exists()){
			failJson.put("code", Constants.Dict.ILLEGAL_PARAMETER.getValue());
			return failJson;
		}
		/*//设置代理
		HttpHost proxy = new HttpHost("127.0.0.1",8888,"http");
		//把代理设置到请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        //实例化CloseableHttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();*/
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder params =  MultipartEntityBuilder.create();
		params.setCharset(Charset.forName("UTF-8"));
		params.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		FileBody fileBody = new FileBody(file);
		try {
			ContentType contentType = ContentType.create("text/plain", "UTF-8");
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, String> entry = it.next();
				params.addPart(entry.getKey(), new StringBody(entry.getValue(),contentType));
			}
			//方式1
			/*params.addBinaryBody("image", new FileInputStream(file),ContentType.DEFAULT_BINARY,"image.jpg");*/
			//方式2
			params.addPart("image",fileBody);
			HttpEntity entity = params.build();
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				//解析json返回值
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuilder sb = new StringBuilder();
				String line = "";
				String NL = System.getProperty("line.separator");  
				while((line = reader.readLine())!=null){
					sb.append(line + NL);
				}
				reader.close();
				System.out.println(sb.toString());
				if(StringUtils.isNotBlank(sb.toString())){
					return JSONObject.fromObject(sb.toString());
				}
				failJson.put("code",Constants.Dict.UPLOAD_UNKNOW_FAIL.getValue());
				return failJson;
			}else{
				failJson.put("code",Constants.Dict.UPLOAD_FIAL.getValue());
				return failJson;
			}
		}catch (HttpHostConnectException e){
			e.printStackTrace();
			failJson.put("code",Constants.Dict.REQUEST_NOTFIND.getValue());
			return failJson;
		} catch (Exception e) {
			e.printStackTrace();
			failJson.put("code",Constants.Dict.UPLOAD_FIAL.getValue());
			return failJson;
		}finally{
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	
	/**多文件批量上传
	 * @param url
	 * @param map
	 * @param fileList
	 * @return 参数错误返回空
	 */
	public static String uploadFile(String url,Map<String,String> map,File[] fileList){
		//参数错误
		if(StringUtils.isBlank(url)){
			return null;
		}
		if(fileList == null || fileList.length == 0){
			return null;
		}
		CloseableHttpClient httpClient = HttpClients.createDefault();
		ArrayList<String> failList = new ArrayList<String>();
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder params =  MultipartEntityBuilder.create();
		params.setCharset(Charset.forName("UTF-8"));
		params.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		ContentType contentType = ContentType.create("text/plain", "UTF-8");
		while(it.hasNext()){
			Map.Entry<String, String> entry = it.next();
			params.addPart(entry.getKey(), new StringBody(entry.getValue(),contentType));
		}
		for(File file : fileList){
			if(file.exists()){
				FileBody fileBody = new FileBody(file);
				//方式1
				/*params.addBinaryBody("image", new FileInputStream(file),ContentType.DEFAULT_BINARY,"image.jpg");*/
				//方式2
				params.addPart("image",fileBody);
				HttpEntity entity = params.build();
				httpPost.setEntity(entity);
			}
		}
		try {
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				//解析json返回值 失败了进入失败列表
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuilder sb = new StringBuilder();
				String line = "";
				String NL = System.getProperty("line.separator");  
				while((line = reader.readLine())!=null){
					sb.append(line + NL);
				}
				reader.close();
				System.out.println(sb.toString());
			}else{
				Constants.Dict.UPLOAD_FIAL.getValue();
			}
		} catch (Exception e) {
			return Constants.Dict.UPLOAD_FIAL.getValue();
		}finally{
			httpPost.releaseConnection();
		}
		//关闭httpclient
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return Constants.Dict.UPLOAD_SUCCESS.getValue();
		
	}
	
	public static String preaseJSON(String jsonStr){
		if(StringUtils.isBlank(jsonStr)){
			return null;
		}
		JSONObject json = JSONObject.fromObject(jsonStr);
		return json.getString("code");
	}

	/*

	*function: 根据Key获取json对象

	*parameter

	*throw

	*created by Eric

	*/
	public static Object preaseJSON(JSONObject json, String key) {
		if(json==null){
			return null;
		}
		return json.get(key);
	}

}
