package com.app.dialog;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.app.callback.CommonCallback;
import com.app.utils.StringUtils;

/**带下拉选项的Dialog
 * @author ERIC
 *
 */
public class SelectDialog extends Dialog implements ActionListener{
	private JButton okBtn = new JButton("确定");
	private JButton cancleBtn = new JButton("取消");
	private JLabel taxlabel = new JLabel("税号选择:");
	private JComboBox comboBox = new JComboBox();
	private CommonCallback callback;
 
	public SelectDialog(Frame owner,String title,String invoiceNum,String selectedItem,CommonCallback callback) {
		super(owner);
		this.callback = callback;
		setTitle(title);
		setBounds(100, 100, 400, 250);  
        setLocationRelativeTo(owner);//定位在父类窗口中间  
        setResizable(false);  
        this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
			}
			@Override
			public void windowIconified(WindowEvent arg0) {
			}
			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}
			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				dispose();
			}
			@Override
			public void windowClosed(WindowEvent arg0) {
			}
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
        setLayout(null);
      
        okBtn.setBounds(220, 200, 60, 30);  
        okBtn.setActionCommand("ok");
        cancleBtn.setBounds(290, 200, 60, 30); 
        cancleBtn.setActionCommand("cancle");
        okBtn.addActionListener(this);  
        cancleBtn.addActionListener(this); 
        String invoiceList[] = invoiceNum.split(",");
        if(invoiceList!=null){
	        for(String str : invoiceList){
	        	comboBox.addItem(str);
	        }
        }
        //设置默认选中税号
        if(!StringUtils.isBlank(selectedItem)){
        	comboBox.setSelectedItem(selectedItem);
        }else{
        	comboBox.setSelectedIndex(0);
        }
        comboBox.setBounds(120,60, 230, 20);
        taxlabel.setBounds(50,60,100,20);
        add(okBtn);
        add(cancleBtn);
        add(comboBox);
        add(taxlabel);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("ok")){
			System.out.println(comboBox.getSelectedItem());
			callback.doAction((String)comboBox.getSelectedItem());
			dispose();
		}else{
			dispose();
		}
	}
	
	

}
