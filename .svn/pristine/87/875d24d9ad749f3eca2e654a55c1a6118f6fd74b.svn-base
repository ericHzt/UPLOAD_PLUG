package com.app.thread;

import java.io.File;

import com.app.filewatcher.FileActionCallback;
import com.app.filewatcher.WatchDir;

/**自定义监控线程启停
 * @author ERIC
 *
 */
public class WatchThread {
	private boolean suspend = false; //暂停标志
	private WatchDir watchDir;
	private File file;
	private boolean subDir;
	private FileActionCallback callback;
	
	public WatchThread(File file, boolean subDir, FileActionCallback callback){
		this.file = file;
		this.subDir = subDir;
		this.callback = callback;
	}
	public boolean isSuspend() {
		return this.suspend;
	}

	public void setSuspend(boolean suspend) {
		this.suspend = suspend;
		if(!suspend){
			try {
				watchDir.startWatcher();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			watchDir.stopWatcher();
		}
	}

	public boolean getSuspend(){
		return this.suspend;
	}
	/*@Override*/
	public void run(){
		try {
			watchDir = new WatchDir(file, false, callback);
			this.suspend = true;
			watchDir.stopWatcher();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
