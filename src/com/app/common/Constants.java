package com.app.common;

/**常量类
 * @author ERIC
 *
 */
public class Constants {
	
	/**普通字典
	 * @author ERIC
	 *
	 */
	public static enum Dict{
		REQUEST_FIAL("400","上传失败，请求错误"),
		REQUEST_NOTFIND("404","上传失败，服务器无响应"),
		REQUEST_SUCCESS("200","请求成功"),
		UPLOAD_SUCCESS("0","文件上传成功"),
		UPLOAD_FIAL("-1","文件上传失败"),
		UPLOAD_ILLEGAL_FILE("-4","上传的文件无法识别"),
		UPLOAD_FILE_NOTEXITS("-6","文件不存在或被移动"),
		FTP_CONNECT_FAIL("-7","上传失败，无法连接ftp服务器"),
		UPLOAD_UNKNOW_FAIL("-5","发生未知错误，请稍后重传"),
		ILLEGAL_PARAMETER("-2","非法参数"),
		NULL_PARAMETER("-3","参数为空"),
		
		INVOICE_IMAGE("1000","发票数据"),
		INVOICELIST_IMAGE("1001","发票清单"),
		FILE("1002","普通文件"),
		OPERATION_TYPE_IMAGE("2001","上传图片"),
		OPERATION_TYPE_XML("2002","上传数据"),
		OPERATION_TYPE_DISXML("2003","作废数据");
		
		
		private Dict(String value,String name){
	        this.value=value;
	        this.name=name;
	    }
		private final String value;
		private final String name;
		public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public String getMessageByKey(String key){
        	for(Dict d : Dict.values()){
        		if(d.getValue().equals(key)){
        			return d.getName();
        		}
        	}
        	return null;
        }
	}
	
}
