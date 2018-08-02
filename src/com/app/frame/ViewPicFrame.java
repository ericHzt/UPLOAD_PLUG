package com.app.frame;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.app.callback.CommonCallback;
import com.app.panel.BackgroundPanel;

/**预览图片JFrame
 * @author ERIC
 *
 */
public class ViewPicFrame extends BaseFrame {
	private JPanel jContentPane = null;
	
	private BackgroundPanel jPanel = null;// 声明自定义背景面板对象
	
	public ViewPicFrame(String path){
		super();
		initialize(path);
	}
	
	public ViewPicFrame(BufferedImage image,String type){
		super();
		initialize(image,type);
	}
	
	public void initialize(String path){
		this.setSize(1017, 584);
        this.setTitle("预览图片");
        this.setContentPane(getJContentPane(path));
        this.addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                	dispose();
                }
            }
        );
	}
	
	/**
	 * @param image
	 * @param type 0为发票类型 1为清单类型
	 */
	public void initialize(BufferedImage image,String type){
		if("0".equals(type)){
			this.setSize(1017, 584);
		}else{
			this.setSize(400, 600);
		}
        this.setTitle("预览图片");
        this.setContentPane(getJContentPane(image));
        this.addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                	dispose();
                }
            }
        );
	}
	
	private JPanel getJContentPane(String path) {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getjPanel(path), BorderLayout.CENTER);
        }
        return jContentPane;
    }
	
	private JPanel getJContentPane(BufferedImage image) {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getjPanel(image), BorderLayout.CENTER);
        }
        return jContentPane;
    }

	public BackgroundPanel getjPanel(String path) {
		if(jPanel == null){
			Image image = null; 
			try {
				image=javax.imageio.ImageIO.read(new File(path));
			} catch (IOException e) {
				 JOptionPane.showMessageDialog(null, "该文件不存在，请检查文件是否被移动或删除。");
				e.printStackTrace();
			}
			jPanel = new BackgroundPanel(image);
		}
		return jPanel;
	}

	public BackgroundPanel getjPanel(BufferedImage bufferedImage){
		if(jPanel == null){
			Image image = null; 
			try {
				image = (Image)bufferedImage;
			} catch (Exception e) {
				 JOptionPane.showMessageDialog(null, "该文件不存在，请坚持文件是否被移动或删除。");
				e.printStackTrace();
			}
			jPanel = new BackgroundPanel(image);
		}
		return jPanel;
	}

	
}
