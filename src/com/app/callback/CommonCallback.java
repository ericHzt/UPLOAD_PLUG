package com.app.callback;

import java.io.File;

import com.app.bean.FileType;

public abstract class CommonCallback {
	public void doAction() {
	};
	
	public void doAction(String str){
		
	};
	
	public void doAction(String str,String code,FileType type){
		
	};

	public void updateDataSuccessLabel(String timeStr,String operaType,String fileName){

	};

	public void uplateDataFailLabel(String fileName,String operaType){

	};

}
