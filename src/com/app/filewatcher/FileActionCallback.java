package com.app.filewatcher;

import java.io.File;

/**文件操作的回调方法
 * @author ERIC
 *
 */
public abstract class FileActionCallback {
	public void delete(File file) {
	};

	public void modify(File file) {
	};

	public void create(File file) {
	};
	
	public void reFresh(){
		
	};
}
