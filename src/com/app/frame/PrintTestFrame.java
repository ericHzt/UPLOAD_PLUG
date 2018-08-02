package com.app.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import com.app.Listener.JbActionListener;
import com.app.bean.SettingBean;
import com.app.callback.CommonCallback;
import com.app.common.PicFunction;
import com.app.ocr.TesseractException;
import com.app.panel.BackgroundPanel;
import com.app.utils.FileUtils;
import com.app.utils.NewImageUtils;
import com.app.utils.StringUtils;
import com.sun.corba.se.impl.interceptors.PINoOpHandlerImpl;

public class PrintTestFrame extends BaseFrame {
	
	private SettingBean settingBean;
	
	private JPanel jContentPane = null;
	
	private BackgroundPanel jPanel = null;// 声明自定义背景面板对象
	//x偏移标签
	private JLabel xLabel = null;
	 
	//y偏移标签
	private JLabel yLabel = null;
	 
	//x文本域
	private JTextField xTextField = null;
	
	//Y文本域
	private JTextField yTextField = null;
	
	//发票号码x偏移
	private JLabel numXLabel = null;
	
	//发票号码y偏移
	private JLabel numYLabel = null;
	
	//发票号码x文本域
	private JTextField numXTextField = null;
	
	//发票号码Y文本域
	private JTextField numYTextField = null;
	
	//发票代码x偏移
	private JLabel codeXLabel = null;
	
	//发票代码y偏移
	private JLabel codeYLabel = null;
	
	//发票代码x文本域
	private JTextField codeXTextField = null;
	
	//发票代码Y文本域
	private JTextField codeYTextField = null;
	
	//印章x偏移
	private JLabel stampXLabel = null;
	
	//印章y偏移
	private JLabel stampYLabel = null;
	
	//印章x文本域
	private JTextField stampXTextField = null;
	
	//印章Y文本域
	private JTextField stampYTextField = null;
	
	//清单印章x偏移
	private JLabel lStampXLabel = null;
	
	//清单印章y偏移
	private JLabel lStampYLabel = null;
	
	//清单印章x文本域
	private JTextField lStampXTextField = null;
	
	//清单印章Y文本域
	private JTextField lStampYTextField = null;
	
	//ocr识别X起点标签
	private JLabel rectXLabel = null;
	
	//ocr识别Y起点标签
	private JLabel rectYLabel = null;
	
	//ocr识别宽度
	private JLabel rectWidthLabel = null;
	
	//ocr识别高度
	private JLabel rectHeightLabel = null;
	
	//ocr识别X起点文本域
	private JTextField rectXTextFied;
	
	//ocr识别Y起点文本域
	private JTextField rectYTextField;
	
	//ocr识别宽度文本域
	private JTextField rectWidthTextField;
	
	//ocr识别高度文本域
	private JTextField rectHeightTextField;
	
	//需要印章
	private JRadioButton needStampBtn = null;
	
	//不需要印章
	private JRadioButton notNeedStampBtn = null;
	
	//按钮组
	private ButtonGroup  needStampBg = null;	
	
	//按钮标签
	private JLabel needStampLabel = null;
	
	//是否需要印章
	private boolean isStamp = false;
	
	//运维信息标签
	private JLabel mainTainInfoLabel = null;
	
	//运维信息文本域
	private JTextArea mainTainInfotTextFied = null;
	
	//普通发票打印测试
	private JButton invoiceTestBtn;
	
	//专用发票打印测试
	private JButton zInvoiceTestBtn;
	
	//发票清单打印测试
	private JButton invoiceListTestBtn;
	
	//ocr测试按钮
	private JButton ocrTestBtn;
	
	//保存
	private JButton saveBtn;
	
	//回调方法
	private CommonCallback commonCallback ;
	
	//测试税号
	private String testInvoiceNum;
	
	//是否需要接入系统
	private Boolean isUpload;
	
	//是否需要接入系统表标签
	private JLabel isUploadLabel;
	
	//是否接入系统按钮组
	private ButtonGroup isUploadGroup;
	
	//需要接入按钮
	private JRadioButton needUploadRBtn;
	
	//不需要接入按钮
	private JRadioButton noNeedUploadRBtn;

	
	public PrintTestFrame(SettingBean settingBean,CommonCallback commonCallback){
		super();
		this.settingBean = settingBean;
		initialize();
		this.commonCallback = commonCallback;
	}
	
	public void initialize(){
		this.testInvoiceNum = INVOICE_NUM.split(",")[0];
		this.setSize(584, 584);
        this.setTitle("打印调试");
        this.setContentPane(getJContentPane());
        this.addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                	dispose();
                }
            }
        );
	}
	
	private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getjPanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }
	
	public BackgroundPanel getjPanel() {
		if(jPanel == null){
			Image image = null; 
			try {
				image=javax.imageio.ImageIO.read(new File(imagePath+"/main_frame.bmp"));
			} catch (IOException e) {
				 JOptionPane.showMessageDialog(null, "背景文件不存在，请检查文件是否被移动或删除。");
				e.printStackTrace();
			}
			jPanel = new BackgroundPanel(image);
			jPanel.setLayout(null);
           /* jPanel.add(getSearchPanel());
            jPanel.add(getJTextArea());*/
			jPanel.add(getxLabel());
			jPanel.add(getyLabel());
			jPanel.add(getxTextField());
			jPanel.add(getyTextField());
			jPanel.add(getNumXLabel());
			jPanel.add(getNumYLabel());
			jPanel.add(getNumXTextField());
			jPanel.add(getNumYTextField());
			jPanel.add(getCodeXLabel());
			jPanel.add(getCodeXTextField());
			jPanel.add(getCodeYLabel());
			jPanel.add(getCodeYTextField());
			jPanel.add(getStampXLabel());
			jPanel.add(getStampYLabel());
			jPanel.add(getStampXTextField());
			jPanel.add(getStampYTextField());
			jPanel.add(getlStampXLabel());
			jPanel.add(getlStampYLabel());
			jPanel.add(getlStampXTextField());
			jPanel.add(getlStampYTextField());
			jPanel.add(getNeedStampLabel());
			jPanel.add(getNeedStampBtn());
			jPanel.add(getNotNeedStampBtn());
			jPanel.add(getInvoiceTestBtn());
			jPanel.add(getzInvoiceTestBtn());
			jPanel.add(getInvoiceListTestBtn());
			jPanel.add(getSaveBtn());
			jPanel.add(getRectXLabel());
			jPanel.add(getRectYLabel());
			jPanel.add(getRectWidthLabel());
			jPanel.add(getRectHeightLabel());
			jPanel.add(getRectXTextFied());
			jPanel.add(getRectYTextField());
			jPanel.add(getRectWidthTextField());
			jPanel.add(getRectHeightTextField());
			jPanel.add(getOctTestBtn());
			jPanel.add(getMainTainInfoLabel());
			jPanel.add(getMainTainInfotTextFied());
			jPanel.add(getIsUploadLabel());
			jPanel.add(getNeedUploadRBtn());
			jPanel.add(getNoNeedUploadRBtn());
		}
		return jPanel;
	}


	/**初始化x偏移标签
	 * @return
	 */
	private JLabel getxLabel() {
		if(xLabel == null){
			xLabel = new JLabel();
			xLabel.setBounds(new Rectangle(15, 20, 120, 18));
			xLabel.setText("发票底版X偏移量：");
		}
		return xLabel;
	}

	/**初始化y偏移标签
	 * @return
	 */
	private JLabel getyLabel() {
		if(yLabel == null){
			yLabel = new JLabel();
			yLabel.setBounds(new Rectangle(15, 48, 120, 18));
			yLabel.setText("发票底版Y偏移量：");
		}
		return yLabel;
	}

	/**初始化x文本域
	 * @return
	 */
	private JTextField getxTextField() {
		if(xTextField == null){
			xTextField = new JTextField();
			xTextField.setBounds(new Rectangle(140, 20, 120, 18));
			xTextField.setText(settingBean.getX().toString());
		}
		return xTextField;
	}

	/**初始化y文本域
	 * @return
	 */
	private JTextField getyTextField() {
		if(yTextField == null){
			yTextField = new JTextField();
			yTextField.setBounds(new Rectangle(140, 48, 120, 18));
			yTextField.setText(settingBean.getY().toString());
		}
		return yTextField;
	}

	/**初始化发票号码x标签
	 * @return
	 */
	private JLabel getNumXLabel() {
		if(numXLabel == null){
			numXLabel = new JLabel();
			numXLabel.setBounds(new Rectangle(15, 76, 120, 18));
			numXLabel.setText("发票号码X偏移量：");
		}
		return numXLabel;
	}

	/**初始化发票号码y标签
	 * @return
	 */
	private JLabel getNumYLabel() {
		if(numYLabel==null){
			numYLabel = new JLabel();
			numYLabel.setBounds(new Rectangle(15, 104, 120, 18));
			numYLabel.setText("发票号码Y偏移量：");
		}
		return numYLabel;
	}

	/**初始化发票号码x文本域
	 * @return
	 */
	private JTextField getNumXTextField() {
		if(numXTextField==null){
			numXTextField = new JTextField();
			numXTextField.setBounds(new Rectangle(140, 76, 120, 18));
			numXTextField.setText(settingBean.getNumX().toString());
			
		}
		return numXTextField;
	}

	/**初始化发票号码y文本域
	 * @return
	 */
	private JTextField getNumYTextField() {
		if(numYTextField == null){
			numYTextField = new JTextField();
			numYTextField.setBounds(new Rectangle(140, 104, 120, 18));
			numYTextField.setText(settingBean.getNumY().toString());
		}
		return numYTextField;
	}

	/**初始化发票代码x标签
	 * @return
	 */
	private JLabel getCodeXLabel() {
		if(codeXLabel == null){
			codeXLabel = new JLabel();
			codeXLabel.setBounds(new Rectangle(15, 132, 120, 18));
			codeXLabel.setText("发票代码X偏移量：");
		}
		return codeXLabel;
	}

	/**初始化发票代码Y标签
	 * @return
	 */
	private JLabel getCodeYLabel() {
		if(codeYLabel == null){
			codeYLabel = new JLabel();
			codeYLabel.setBounds(new Rectangle(15, 160, 120, 18));
			codeYLabel.setText("发票代码Y偏移量：");
		}
		return codeYLabel;
	}

	/**初始化发票代码x文本域
	 * @return
	 */
	private JTextField getCodeXTextField() {
		if(codeXTextField==null){
			codeXTextField = new JTextField();
			codeXTextField.setBounds(new Rectangle(140, 132, 120, 18));
			codeXTextField.setText(settingBean.getCodeX().toString());
			
		}
		return codeXTextField;
	}

	/**初始化发票代码y文本域
	 * @return
	 */
	private JTextField getCodeYTextField() {
		if(codeYTextField == null){
			codeYTextField = new JTextField();
			codeYTextField.setBounds(new Rectangle(140, 160, 120, 18));
			codeYTextField.setText(settingBean.getCodeY().toString());
		}
		return codeYTextField;
	}

	/**初始化印章x标签
	 * @return
	 */
	private JLabel getStampXLabel() {
		if(stampXLabel == null){
			stampXLabel = new JLabel();
			stampXLabel.setBounds(new Rectangle(15, 188, 120, 18));
			stampXLabel.setText("印章X偏移量：");
			
		}
		return stampXLabel;
	}

	/**初始化印章y标签
	 * @return
	 */
	private JLabel getStampYLabel() {
		if(stampYLabel == null){
			stampYLabel = new JLabel();
			stampYLabel.setBounds(new Rectangle(15, 216, 120, 18));
			stampYLabel.setText("印章Y偏移量：");
			
		}
		return stampYLabel;
	}

	/**初始化印章x文本域
	 * @return
	 */
	private JTextField getStampXTextField() {
		if(stampXTextField == null){
			stampXTextField = new JTextField();
			stampXTextField.setBounds(new Rectangle(140, 188, 120, 18));
			stampXTextField.setText(settingBean.getStampX().toString());
		}
		return stampXTextField;
	}

	/**初始化印章y文本域
	 * @return
	 */
	private JTextField getStampYTextField() {
		if(stampYTextField == null){
			stampYTextField = new JTextField();
			stampYTextField.setBounds(new Rectangle(140, 216, 120, 18));
			stampYTextField.setText(settingBean.getStampY().toString());
		}
		return stampYTextField;
	}

	/**初始化清单印章x标签
	 * @return
	 */
	private JLabel getlStampXLabel() {
		if(lStampXLabel == null){
			lStampXLabel = new JLabel();
			lStampXLabel.setBounds(new Rectangle(15, 244, 120, 18));
			lStampXLabel.setText("清单印章X偏移量：");
			
		}
		return lStampXLabel;
	}

	 /**初始化清单印章y标签
	 * @return
	 */
	private JLabel getlStampYLabel() {
		if(lStampYLabel == null){
			lStampYLabel = new JLabel();
			lStampYLabel.setBounds(new Rectangle(15, 272, 120, 18));
			lStampYLabel.setText("清单印章Y偏移量：");
			
		}
		return lStampYLabel;
	}

	/**初始化清单印章x文本域
	 * @return
	 */
	private JTextField getlStampXTextField() {
		if(lStampXTextField == null){
			lStampXTextField = new JTextField();
			lStampXTextField.setBounds(new Rectangle(140, 244, 120, 18));
			lStampXTextField.setText(settingBean.getlStampX().toString());
		}
		return lStampXTextField;
	}

	/**初始化清单印章y文本域
	 * @return
	 */
	private JTextField getlStampYTextField() {
		if(lStampYTextField == null){
			lStampYTextField = new JTextField();
			lStampYTextField.setBounds(new Rectangle(140, 272, 120, 18));
			lStampYTextField.setText(settingBean.getlStampY().toString());
		}
		return lStampYTextField;
	}

	

	/**初始化ocr识别起点
	 * @return
	 */
	private JLabel getRectXLabel() {
		if(rectXLabel == null){
			rectXLabel = new JLabel();
			rectXLabel.setBounds(new Rectangle(15, 300, 120, 18));
			rectXLabel.setText("ocr识别X起点：");
		}
		return rectXLabel;
	}

	/**初始化ocr识别起点
	 * @return
	 */
	private JLabel getRectYLabel() {
		if(rectYLabel == null){
			rectYLabel = new JLabel();
			rectYLabel.setBounds(new Rectangle(15, 328, 120, 18));
			rectYLabel.setText("ocr识别Y起点：");
		}
		return rectYLabel;
	}

	/**初始化ocr识别宽度
	 * @return
	 */
	private JLabel getRectWidthLabel() {
		if(rectWidthLabel == null){
			rectWidthLabel = new JLabel();
			rectWidthLabel.setBounds(new Rectangle(15, 356, 120, 18));
			rectWidthLabel.setText("ocr识别宽度：");
		}
		return rectWidthLabel;
	}

	/**初始化ocr识别高度
	 * @return
	 */
	private JLabel getRectHeightLabel() {
		if(rectHeightLabel == null){
			rectHeightLabel = new JLabel();
			rectHeightLabel.setBounds(new Rectangle(15, 384, 120, 18));
			rectHeightLabel.setText("ocr识别高度：");
		}
		return rectHeightLabel;
	}

	/**初始化ocr识别X起点文本域
	 * @return
	 */
	private JTextField getRectXTextFied() {
		if(rectXTextFied == null){
			rectXTextFied = new JTextField();
			rectXTextFied.setBounds(new Rectangle(140, 300, 120, 18));
			rectXTextFied.setText(settingBean.getRectX().toString());
		}
		return rectXTextFied;
	}

	/**初始化ocr识别Y起点文本域
	 * @return
	 */
	private JTextField getRectYTextField() {
		if(rectYTextField == null){
			rectYTextField = new JTextField();
			rectYTextField.setBounds(new Rectangle(140, 328, 120, 18));
			rectYTextField.setText(settingBean.getRectY().toString());
			
		}
		return rectYTextField;
	}

	/**初始化ocr识别宽度文本域
	 * @return
	 */
	private JTextField getRectWidthTextField() {
		if(rectWidthTextField == null){
			rectWidthTextField = new JTextField();
			rectWidthTextField.setBounds(new Rectangle(140, 356, 120, 18));
			rectWidthTextField.setText(settingBean.getRectWidth().toString());
		}
		return rectWidthTextField;
	}

	/**初始化ocr识别高度文本域
	 * @return
	 */
	private JTextField getRectHeightTextField() {
		if(rectHeightTextField == null){
			rectHeightTextField = new JTextField();
			rectHeightTextField.setBounds(new Rectangle(140, 384, 120, 18));
			rectHeightTextField.setText(settingBean.getRectHeight().toString());
		}
		return rectHeightTextField;
	}

	/**初始化需要印章按钮
	 * @return
	 */
	private JRadioButton getNeedStampBtn() {
		if(needStampBtn == null){
			if(settingBean.getIsStamp()){
				needStampBtn = new JRadioButton("是",true);
				isStamp = true;
			}else{
				needStampBtn = new JRadioButton("是",false);
				isStamp = false;
			}
			needStampBtn.setBounds(140, 412, 50, 18);
			needStampBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					isStamp = true;
					
				}
			});
		}
		needStampBg = getNeedStampBg();
		needStampBg.add(needStampBtn);
		return needStampBtn;
	}

	/**初始化不需要印章按钮
	 * @return
	 */
	private JRadioButton getNotNeedStampBtn() {
		if(notNeedStampBtn == null){
			if(settingBean.getIsStamp()){
				notNeedStampBtn = new JRadioButton("否",false);
				isStamp = true;
			}else{
				notNeedStampBtn = new JRadioButton("否",true);
				isStamp = false;
			}
			notNeedStampBtn.setBounds(200, 412, 50, 18);
			notNeedStampBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					isStamp = false;
					
				}
			});
		}
		needStampBg = getNeedStampBg();
		needStampBg.add(notNeedStampBtn);
		return notNeedStampBtn;
	}

	/**初始化按钮组
	 * @return
	 */
	private ButtonGroup getNeedStampBg() {
		if(needStampBg == null){
			needStampBg = new ButtonGroup();
		}
		return needStampBg;
	}

	private JLabel getNeedStampLabel() {
		if(needStampLabel == null){
			needStampLabel = new JLabel();
			needStampLabel.setBounds(new Rectangle(15, 412, 120, 18));
			needStampLabel.setText("是否需要印章：");
		}
		return needStampLabel;
	}
	
	

	/**初始化接入系统按钮组
	 * @return
	 */
	private JLabel getIsUploadLabel() {
		if(isUploadLabel == null){
			isUploadLabel = new JLabel();
			isUploadLabel.setBounds(new Rectangle(260, 412, 120, 18));
			isUploadLabel.setText("是否需要接入系统：");
		}
		
		return isUploadLabel;
	}

	/** 接入系统按钮组
	 * @return
	 */
	private ButtonGroup getIsUploadGroup() {
		if(isUploadGroup == null){
			isUploadGroup = new ButtonGroup();
		}
		return isUploadGroup;
	}

	/**接入系统按钮
	 * @return
	 */
	private JRadioButton getNeedUploadRBtn() {
		
		if(needUploadRBtn == null){
			if(settingBean.getIsUpload()){
				needUploadRBtn = new JRadioButton("是",true);
				isUpload = true;
			}else{
				needUploadRBtn = new JRadioButton("是",false);
				isUpload = false;
			}
			needUploadRBtn.setBounds(385, 412, 50, 18);
			needUploadRBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					isUpload = true;
					
				}
			});
		}
		isUploadGroup = getIsUploadGroup();
		isUploadGroup.add(needUploadRBtn);
		return needUploadRBtn;
	}

	private JRadioButton getNoNeedUploadRBtn() {
		
		if(noNeedUploadRBtn == null){
			if(settingBean.getIsUpload()){
				noNeedUploadRBtn = new JRadioButton("否",false);
				isUpload = true;
			}else{
				noNeedUploadRBtn = new JRadioButton("否",true);
				isUpload = false;
			}
			noNeedUploadRBtn.setBounds(445, 412, 50, 18);
			noNeedUploadRBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					isUpload = false;
					
				}
			});
		}
		isUploadGroup = getIsUploadGroup();
		isUploadGroup.add(noNeedUploadRBtn);
		return noNeedUploadRBtn;
	}

	private JButton getInvoiceTestBtn() {
		if(invoiceTestBtn == null){
			invoiceTestBtn = new JButton();
			invoiceTestBtn.setText("普票测试预览");
			invoiceTestBtn.setBounds(new Rectangle(15, 486, 120, 18));
			invoiceTestBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*JOptionPane.showMessageDialog(null, "普通发票测试预览");*/
					//底版文件
					File file = new File(settingBean.getFilePath());
					//测试文件
					/*File testFile = new File("D://ACTPRINTPLUG//filepath//testinvoice.bmp");*/
					File testFile = new File(settingBean.getTestWaterFile());
					//印章文件
					/*File stampFile = new File("D://ACTPRINTPLUG//filepath//stamp.jpg");*/
					File stampFile = new File(settingBean.getStampMap().get(testInvoiceNum));
					String x = xTextField.getText();
					String y = yTextField.getText();
					String numX = numXTextField.getText();
					String numY = numYTextField.getText();
					String codeX = codeXTextField.getText();
					String codeY = codeYTextField.getText();
					String stampX = stampXTextField.getText();
					String stampY = stampYTextField.getText();
					if(legalTest()){
						BufferedImage image = PicFunction.printInvoiceTest(file,testFile,stampFile,isStamp, x, y,numX,numY,codeX,codeY,stampX,stampY);
						PicFunction.viewPic(image,"0");
					}
				}
			});
		}
		return invoiceTestBtn;
	}
	
	private JButton getzInvoiceTestBtn() {
		if(zInvoiceTestBtn == null){
			zInvoiceTestBtn = new JButton();
			zInvoiceTestBtn.setText("专票测试预览");
			zInvoiceTestBtn.setBounds(new Rectangle(140, 486, 120, 18));
			zInvoiceTestBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					/*JOptionPane.showMessageDialog(null, "专用发票测试预览");*/
					//底版文件
					File file = new File(settingBean.getzFilePath());
					//测试文件
					/*File testFile = new File("D://ACTPRINTPLUG//filepath//testinvoice.bmp");*/
					File testFile = new File(settingBean.getTestWaterFile());
					//印章文件
					/*File stampFile = new File("D://ACTPRINTPLUG//filepath//stamp.jpg");*/
					File stampFile = new File(settingBean.getStampMap().get(testInvoiceNum));
					String x = xTextField.getText();
					String y = yTextField.getText();
					String numX = numXTextField.getText();
					String numY = numYTextField.getText();
					String codeX = codeXTextField.getText();
					String codeY = codeYTextField.getText();
					String stampX = stampXTextField.getText();
					String stampY = stampYTextField.getText();
					if(legalTest()){
						BufferedImage image = PicFunction.printInvoiceTest(file,testFile,stampFile,isStamp, x, y,numX,numY,codeX,codeY,stampX,stampY);
						PicFunction.viewPic(image,"0");
					}
				}
			});
		}
		return zInvoiceTestBtn;
	}

	private JButton getInvoiceListTestBtn() {
		if(invoiceListTestBtn == null){
			invoiceListTestBtn = new JButton();
			invoiceListTestBtn.setText("清单测试预览");
			invoiceListTestBtn.setBounds(new Rectangle(265, 486, 120, 18));
			invoiceListTestBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					/*JOptionPane.showMessageDialog(null, "发票清单测试预览");*/
					//测试文件
					/*File file = new File("D://ACTPRINTPLUG//filepath//testinvoicelist.bmp");*/
					File file = new File(settingBean.getTestInvoiceList());
					//印章文件
					File stampFile = new File(settingBean.getStampMap().get(testInvoiceNum));
					
					String lStampX = lStampXTextField.getText();
					String lStampY = lStampYTextField.getText();
					if(legalTest()){
						BufferedImage image = PicFunction.printInvoiceListTest(file, stampFile, isStamp, lStampX, lStampY);
						PicFunction.viewPic(image,"1");
					}
				}
			});
		}
		
		return invoiceListTestBtn;
	}

	private JButton getOctTestBtn(){
		if(ocrTestBtn == null){
			ocrTestBtn = new JButton();
			ocrTestBtn.setBounds(new Rectangle(390, 486, 120, 18));
			ocrTestBtn.setText("OCR识别预览");
			ocrTestBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String rectX = rectXTextFied.getText();
					String rectY = rectYTextField.getText();
					String rectWidth = rectWidthTextField.getText();
					String rectHeight = rectHeightTextField.getText();
					if(legalTest()){
						try {
							String result = PicFunction.ocrTest(settingBean.getTestWaterFile()
									, new Rectangle(Integer.parseInt(rectX),Integer.parseInt(rectY),Integer.parseInt(rectWidth),Integer.parseInt(rectHeight)));
							JOptionPane.showMessageDialog(null, "识别结果为："+result);
						} catch (TesseractException e1) {
							JOptionPane.showMessageDialog(null, "识别出错！");
							e1.printStackTrace();
						}
					}
					
				}
			});
		}
		return ocrTestBtn;
	}
	
	private JButton getSaveBtn() {
		if(saveBtn == null){
			saveBtn = new JButton();
			saveBtn.setText("保存设置");
			saveBtn.setBounds(new Rectangle(15, 514, 120, 18));
			saveBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int choice = JOptionPane.showConfirmDialog(null, "是否保存设置？");
					switch (choice) {
					case JOptionPane.YES_OPTION:
						saveSetting();
						break;
					default:
						break;
					}
					
				}
			});
		}
		return saveBtn;
	}
	
	/**运维信息标签
	 * @return
	 */
	public JLabel getMainTainInfoLabel() {
		if(mainTainInfoLabel == null){
			mainTainInfoLabel = new JLabel();
			mainTainInfoLabel.setText("运维信息：");
			mainTainInfoLabel.setBounds(new Rectangle(15, 440, 120, 18));
		}
		return mainTainInfoLabel;
	}

	/**运维信息文本域 
	 * @return
	 */
	public JScrollPane getMainTainInfotTextFied() {
		mainTainInfotTextFied = new JTextArea();
		mainTainInfotTextFied.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
		mainTainInfotTextFied.setLineWrap(true);        //激活自动换行功能 
		mainTainInfotTextFied.setWrapStyleWord(true);            // 激活断行不断字功能
		mainTainInfotTextFied.setAutoscrolls(true);
		String str;
		try {
			str = FileUtils.readFile(sysResourcePath+"maintaininfo.txt");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "运维信息文件读写操作异常！");
			str = "";
		}
		mainTainInfotTextFied.setText(str);
		JScrollPane jsp = new JScrollPane(mainTainInfotTextFied);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		jsp.setBounds(new Rectangle(140, 440, 360, 36));
		return jsp;
	}
	
	/**输入合法性校验
	 * @return
	 */
	private boolean legalTest(){
		boolean flag = false;
		String x = xTextField.getText();
		String y = yTextField.getText();
		String numX = numXTextField.getText();
		String numY = numYTextField.getText();
		String codeX = codeXTextField.getText();
		String codeY = codeYTextField.getText();
		String stampX = stampXTextField.getText();
		String stampY = stampYTextField.getText();
		String lStampX = lStampXTextField.getText();
		String lStampY = lStampYTextField.getText();
		
		String rectX = rectXTextFied.getText();
		String rectY = rectYTextField.getText();
		String rectWidth = rectWidthTextField.getText();
		String rectHeight = rectHeightTextField.getText();
		
		if(StringUtils.isBlank(x)||StringUtils.isBlank(y)||StringUtils.isBlank(numX)||StringUtils.isBlank(numY)||
				StringUtils.isBlank(codeX)||StringUtils.isBlank(codeY)||StringUtils.isBlank(lStampX)||StringUtils.isBlank(lStampY)
				||StringUtils.isBlank(rectX)||StringUtils.isBlank(rectY)||StringUtils.isBlank(rectWidth)
				||StringUtils.isBlank(rectHeight)){
			JOptionPane.showMessageDialog(null, "x变量和Y变量不能为空");
			return flag;
		}
		if(isStamp &&(StringUtils.isBlank(stampX)||StringUtils.isBlank(stampY))){
			JOptionPane.showMessageDialog(null, "x变量和Y变量不能为空");
			return flag;
		}
		if(!StringUtils.isInteger(x)||!StringUtils.isInteger(y)||!StringUtils.isInteger(numX)||!StringUtils.isInteger(numY)||
				!StringUtils.isInteger(codeX)||!StringUtils.isInteger(codeY)||!StringUtils.isInteger(lStampX)||!StringUtils.isInteger(lStampY)
				||!StringUtils.isInteger(rectX)||!StringUtils.isInteger(rectY)||!StringUtils.isInteger(rectWidth)
				||!StringUtils.isInteger(rectHeight)){
			JOptionPane.showMessageDialog(null, "请输入整数");
			return flag;
		}
		if(isStamp &&(!StringUtils.isInteger(stampX)||!StringUtils.isInteger(stampY))){
			JOptionPane.showMessageDialog(null, "请输入整数");
			return flag;
		}
		flag = true;
		return flag;
	}
	
	/**
	 * 保存设置
	 */
	private void saveSetting(){
		String x = xTextField.getText();
		String y = yTextField.getText();
		String numX = numXTextField.getText();
		String numY = numYTextField.getText();
		String codeX = codeXTextField.getText();
		String codeY = codeYTextField.getText();
		String stampX = stampXTextField.getText();
		String stampY = stampYTextField.getText();
		String lStampX = lStampXTextField.getText();
		String lStampY = lStampYTextField.getText();
		
		String rectX = rectXTextFied.getText();
		String rectY = rectYTextField.getText();
		String rectWidth = rectWidthTextField.getText();
		String rectHeight = rectHeightTextField.getText();
		if(legalTest()){
			initLoader.updateProperties("x", x);
			initLoader.updateProperties("y", y);
			initLoader.updateProperties("numX",numX);
			initLoader.updateProperties("numY",numY);
			initLoader.updateProperties("codeX",codeX);
			initLoader.updateProperties("codeY",codeY);
			
			initLoader.updateProperties("rectX", rectX);
			initLoader.updateProperties("rectY", rectY);
			initLoader.updateProperties("rectWidth", rectWidth);
			initLoader.updateProperties("rectHeight", rectHeight);
			if(isStamp){
				initLoader.updateProperties("isStamp","true");
				initLoader.updateProperties("stampX",stampX);
				initLoader.updateProperties("stampY",stampY);
				initLoader.updateProperties("lStampX",lStampX);
				initLoader.updateProperties("lStampY",lStampY);
			}else{
				initLoader.updateProperties("isStamp","false");
			}
			if(isUpload){
				initLoader.updateProperties("isUpload", "true");
			}else{
				initLoader.updateProperties("isUpload", "false");
			}
			commonCallback.doAction();
			JOptionPane.showMessageDialog(null, "保存成功！");
		}
		String info = mainTainInfotTextFied.getText();
		try {
			FileUtils.writeFile(info, sysResourcePath+"maintaininfo.txt");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "运维信息文件读写操作异常！");
		}
		
	}


	
	
	
	
	
	

}
