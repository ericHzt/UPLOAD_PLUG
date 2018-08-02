package com.app.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.app.frame.BaseFrame;

public class JbActionListener implements ActionListener {
	String type;
	boolean isSuspend;
	Integer length;
	
	public JbActionListener(String type, boolean isSuspend,Integer length) {
		this.type = type;
		this.isSuspend = isSuspend;
		this.length = length;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(isSuspend && length!=null && length == 0 ){
			switch (command) {
			case "发票":
				type = BaseFrame.INVOICE_TYPE;
				break;
			case "发票列表":
				type = BaseFrame.INVOICE_LIST_TYPE;
				break;
			default:
				type = BaseFrame.INVOICE_TYPE;
				break;
			}
		}else if(!isSuspend){
			JOptionPane.showMessageDialog(null, "请先停止自动打印才能修改打印类型！");
		}else if(length!=null && length!=0){
			JOptionPane.showMessageDialog(null, "打印任务正在进行，请等待打印完成在进行类型修改！");
		}else{
			JOptionPane.showMessageDialog(null, "修改类型失败！");
		}
	}

}
