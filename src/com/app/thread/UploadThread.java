package com.app.thread;

import java.io.File;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.app.bean.FileType;
import com.app.bean.SettingBean;
import com.app.callback.CommonCallback;
import com.app.common.Constants;
import com.app.utils.FileUtils;

/**上传文件线程
 * @author ERIC
 *
 */
public class UploadThread extends Thread {
	private BlockingQueue<FileType> uploadQueue;
	private SettingBean settingBean;
	private Map<String,String> map;
	private boolean exit = false;
	private CommonCallback callBack;
	private String uploadType;
	public UploadThread(Map<String, String> map,SettingBean settingBean,BlockingQueue<FileType> uploadQueue,CommonCallback callback,String uploadType) {
		this.map = map;
		this.settingBean = settingBean;
		this.uploadQueue = uploadQueue;
		this.callBack = callback;
		this.uploadType = uploadType;
	}
	@Override
	public void run() {
		super.run();
		//普通文件上传
		while(!exit){
			try {
				FileType fileType = uploadQueue.take();
					if(fileType.getFileType().equals(Constants.Dict.FILE.getValue())){
						
						File tempFile = new File(settingBean.getUploadFilesPath()+fileType.getFileName());
						//设置上传文件类型参数
						map.put("fileType", fileType.getFileType());
						
						uploadFile(settingBean.getUploadUrl(), tempFile, fileType.getFileName());
						
						map.remove("fileType");
						
					}else if(fileType.getFileType().equals(Constants.Dict.INVOICE_IMAGE.getValue())){
						
						File tempFile = new File(settingBean.getUploadInvoicePath()+fileType.getFileName());
						
						map.put("fileType", fileType.getFileType());
						
						uploadFile(settingBean.getUploadUrl(), tempFile, fileType.getFileName());
						
						map.remove("fileType");
						
					}else if(fileType.getFileType().equals(Constants.Dict.INVOICELIST_IMAGE.getValue())){
						
						File tempFile = new File(settingBean.getUploadInvoiceListPath()+fileType.getFileName());
						
						map.put("fileType", fileType.getFileType());
						
						uploadFile(settingBean.getUploadUrl(), tempFile, fileType.getFileName());
						
						map.remove("fileType");
						
					}else{
						;
					}
					
			} catch (InterruptedException e) {
				e.printStackTrace();
				//发生错误继续执行不shutdown
				continue;
			}
		}
	}
	
	public void uploadFile(String url,File tempFile,String fileName){
		if(tempFile.exists()){
			String code = FileUtils.uploadFile(url, map, tempFile);
			if(Constants.Dict.UPLOAD_SUCCESS.getValue().equals(code)){
				if(map.get("fileType").equals(Constants.Dict.FILE.getValue())){
					FileUtils.deleteFile(settingBean.getUploadFilesPath()+fileName);
				}else if(map.get("fileType").equals(Constants.Dict.INVOICE_IMAGE.getValue())){
					FileUtils.deleteFile(settingBean.getUploadInvoicePath()+fileName);
				}else if(map.get("fileType").equals(Constants.Dict.INVOICELIST_IMAGE.getValue())){
					FileUtils.deleteFile(settingBean.getUploadInvoiceListPath()+fileName);
				}else{
					;
				}
				callBack.doAction(fileName+"文件上传成功！",code,null);
			}else{
				//文件上传失败
				String msg = Constants.Dict.REQUEST_FIAL.getMessageByKey(code);
				callBack.doAction(fileName+msg,code,new FileType(fileName,map.get("fileType")));
			}
		}else{
			callBack.doAction(fileName+"文件上传失败,文件被移动或不存在！");
		}
	}
	
	public void isExit(boolean isExit){
		this.exit = isExit;
	}

}
