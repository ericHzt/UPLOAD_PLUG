package com.app.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.app.panel.BackgroundPanel;
import com.app.utils.FileUtils;
import com.app.utils.PropertiesLoader;
import com.app.utils.StringUtils;
import com.app.utils.renew.OfficeRenewUtils;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;

/**
 * @author ERIC
 *	 20180613
 */
public class LoginFrame extends BaseFrame {
	private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private URL url = null;// 声明图片的URL
    private Image image=null;// 声明图像对象
    private BackgroundPanel jPanel = null;// 声明自定义背景面板对象    
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JTextField tf_username = null;
    private JPasswordField pf_password = null;
    private JButton btn_login = null;
    private JButton btn_reset = null;
    private JButton btn_exit = null;
    
    private JPanel getJPanel() {
        if (jPanel == null) {
            jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(221, 176, 63, 18));
            jLabel1.setText("密    码：");
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(220, 141, 63, 18));
            jLabel.setText("用    户：");
            /*url = LoginFrame.class.getResource("/image/登录.jpg");    // 获得图片的URL
*/            try {
				image=javax.imageio.ImageIO.read(new File(imagePath+java.io.File.separator+"login_main_7_gray_ignorev.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}                      // 创建图像对象 
            jPanel = new BackgroundPanel(image);
            jPanel.setLayout(null);
            /*jPanel.add(jLabel, null);
            jPanel.add(jLabel1, null);*/
            /*jPanel.add(getTf_username(), null);
            jPanel.add(getPf_password(), null);*/
            jPanel.add(getBtn_login(), null);
            /*jPanel.add(getBtn_reset(), null);*/
            /*jPanel.add(getBtn_exit(), null);*/
        }
        return jPanel;
    }

    private JTextField getTf_username() {
        if (tf_username == null) {
            tf_username = new JTextField();
            tf_username.setBounds(new Rectangle(290, 140, 143, 22));
            tf_username.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    pf_password.requestFocus();
                }
            });
        }
        return tf_username;
    }
    
    private JPasswordField getPf_password() {
        if (pf_password == null) {
            pf_password = new JPasswordField();
            pf_password.setBounds(new Rectangle(290, 175, 141, 22));
            pf_password.setEchoChar('*');
            pf_password.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    /*String username = tf_username.getText().trim();
                    String password = new String(pf_password.getPassword());
                    User user = new User();
                    user.setName(username);
                    user.setPwd(password);*/
                    if (true){
                    MainFrame thisClass = new MainFrame();
                    thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Toolkit tookit = thisClass.getToolkit();
                    Dimension dm = tookit.getScreenSize();
                    thisClass.setLocation(
                            (dm.width - thisClass.getWidth()) / 2,
                            (dm.height - thisClass.getHeight()) / 2);
                    thisClass.setVisible(true);
                    dispose();
                    }
                }
            });
        }
        return pf_password;
    }
    
    private JButton getBtn_login() {
        if (btn_login == null) {
            btn_login = new JButton();
            btn_login.setBounds(new Rectangle(190, 170, 100, 32));
            btn_login.setRolloverIcon(new ImageIcon(imagePath+java.io.File.separator+"login_5_32_comfirm.jpg"));
            btn_login.setIcon(new ImageIcon(imagePath+java.io.File.separator+"login_5_32_normal.jpg"));
            btn_login.setBackground(Color.getColor("#eae8f5"));
            btn_login.setBorder(null);
            btn_login.addActionListener(new java.awt.event.ActionListener() {
            	
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	BaseFrame.INVOICE_NUM = OfficeRenewUtils.getTaxNumber(rootPath).get("TAXPAYNUMBER");
                	//System.err.append(BaseFrame.INVOICE_NUM);
                	if(BaseFrame.INVOICE_NUM == null || "".equals(BaseFrame.INVOICE_NUM)){                		
                		JOptionPane.showMessageDialog(null, "软件使用期过期或者不是在注册的电脑上运行！");
                	}else{
                		JOptionPane.showMessageDialog(null, "本机打印的税号是【"+BaseFrame.INVOICE_NUM+"】！");
	                    if (initLoader.completeCheck()){
	                    	if(initFileCheck()){
			                    MainFrame thisClass = new MainFrame();
			                    thisClass.setResizable(false);
			                    thisClass.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			                    Toolkit tookit = thisClass.getToolkit();
			                    Dimension dm = tookit.getScreenSize();
			                    thisClass.setLocation(
			                            (dm.width - thisClass.getWidth()) / 2,
			                            (dm.height - thisClass.getHeight()) / 2);
			                    thisClass.setVisible(true);
			                    dispose();
	                    	}
	                    }
                	}
                }
            });
        }
        return btn_login;
    }
    
    private JButton getBtn_reset() {
        if (btn_reset == null) {
            btn_reset = new JButton();
            btn_reset.setBounds(new Rectangle(299, 211, 55, 23));
            btn_reset.setRolloverIcon(new ImageIcon(getClass().getResource("/image/cz.jpg")));
            btn_reset.setIcon(new ImageIcon(getClass().getResource("/image/cz01.jpg")));
            btn_reset.setMargin(new Insets(0,0,0,0));
            btn_reset.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    tf_username.setText("");
                    pf_password.setText("");
                    tf_username.requestFocus();
                }
            });
        }
        return btn_reset;
    }
    
    /*private JButton getBtn_exit() {
        if (btn_exit == null) {
            btn_exit = new JButton();
            btn_exit.setBounds(new Rectangle(374, 211, 53, 22));
            btn_exit.setRolloverIcon(new ImageIcon(getClass().getResource("/image/tc.jpg")));
            btn_exit.setIcon(new ImageIcon(getClass().getResource("/image/tc01.jpg")));
            btn_exit.setMargin(new Insets(0,0,0,0));
            btn_exit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return btn_exit;
    }*/
    
    public LoginFrame() {
        super();
        initialize();
    }
    
    private void initialize() {
    	boolean flag = uniqueCheck(uniqueLockPath);
    	if(flag){
	        this.setSize(476, 300);
	        this.setContentPane(getJContentPane());
	        this.setTitle("ACT发票图片生成软件 V3.1");
    	}else{
    		System.exit(0);
    	}
    }
    
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginFrame thisClass = new LoginFrame();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setResizable(false);
               /* thisClass.setSize(420, 300);*/
                Toolkit tookit = thisClass.getToolkit();
                Dimension dm = tookit.getScreenSize();
                thisClass.setLocation(
                        (dm.width - thisClass.getWidth()) / 2,
                        (dm.height - thisClass.getHeight()) / 2);
                thisClass.setVisible(true);
            }
        });

	}
	/**初始化文件检查和创建
	 * 
	 */
	public boolean initFileCheck(){
		boolean flag = false;
		String invoiceNumList[] = INVOICE_NUM.split(",");
		if(invoiceNumList == null && invoiceNumList.length==0){
			return false;
		}
		try{
			String rootDic = initLoader.getProperty("rootDic");
			String waterFilePath = initLoader.getProperty("waterFilePath");
			String savePath = initLoader.getProperty("savePath");
			String listSavePath = initLoader.getProperty("listSavePath");
			
			String filePath = configFilesPath+initLoader.getProperty("filePath");
			String zFilePath = configFilesPath+initLoader.getProperty("zFilePath");
			/*String stampPath = configFilesPath+initLoader.getProperty("stampPath");*/
			String testFilePath = configFilesPath+initLoader.getProperty("testWaterFile");
			String testInvoiceList = configFilesPath+initLoader.getProperty("testInvoiceList");
			boolean isStamp = initLoader.getBoolean("isStamp");
			boolean isUpload = initLoader.getBoolean("isUpload");
			/*String str = savePath.substring(0,savePath.lastIndexOf("//save"));*/
			if(!new File(rootDic).isDirectory()){
				new File(rootDic).mkdir();
			}
			//循环创建文件夹
			for(String str: invoiceNumList){
				if(!StringUtils.isBlank(str)){
					if(!new File(rootDic+str+"//").isDirectory()){
						new File(rootDic+str+"//").mkdir();
					}
					System.out.println(rootDic+str+"//"+savePath);
					if(!new File(rootDic+str+"//"+savePath).isDirectory()){
						new File(rootDic+str+"//"+savePath).mkdir();
					}
					if(!new File(rootDic+str+"//"+listSavePath).isDirectory()){
						new File(rootDic+str+"//"+listSavePath).mkdir();
					}
				}
			}
			if(isUpload){
				String uploadFilePath = initLoader.getProperty("uploadFilePath");
				if(!(new File(uploadFilePath).isDirectory())){
					new File(uploadFilePath+"file//").mkdir();
					new File(uploadFilePath+"invoiceImage//").mkdir();
					new File(uploadFilePath+"invoiceListImage//").mkdir();
				}
				if(new File(uploadFilePath).isDirectory() && !(new File(uploadFilePath+"file//").isDirectory())){
					new File(uploadFilePath+"file//").mkdir();
				}
				if(new File(uploadFilePath).isDirectory() && !(new File(uploadFilePath+"invoiceImage//").isDirectory())){
					new File(uploadFilePath+"invoiceImage//").mkdir();
				}
				if(new File(uploadFilePath).isDirectory() && !(new File(uploadFilePath+"invoiceListImage//").isDirectory())){
					new File(uploadFilePath+"invoiceListImage//").mkdir();
				}
			}
			
			if(!(new File(waterFilePath).isDirectory())){
				new File(waterFilePath).mkdir();
			}
			
			if(!(new File(configFilesPath)).isDirectory()){
				new File(configFilesPath).mkdir();
				JOptionPane.showMessageDialog(null, "请在"+configFilesPath+"路径下配置底版文件与印章文件！");
				return flag;
			}
			if(!new File(filePath).exists()){
				JOptionPane.showMessageDialog(null, "请在"+configFilesPath+"路径下配置普通发票底版文件！");
				return flag;
			}
			if(!new File(zFilePath).exists()){
				JOptionPane.showMessageDialog(null, "请在"+configFilesPath+"路径下配置专用发票底版文件！");
				return flag;
			}
			if(!new File(testFilePath).exists()){
				JOptionPane.showMessageDialog(null, "请在"+configFilesPath+"路径下配置测试发票数据文件！");
				return flag;
			}
			if(!new File(testInvoiceList).exists()){
				JOptionPane.showMessageDialog(null, "请在"+configFilesPath+"路径下配置测试发票清单数据文件！");
				return flag;
			}
			if(isStamp){
				for(String str: invoiceNumList){
					if(!StringUtils.isBlank(str)){
						if(!new File(configFilesPath+str+".jpg").exists()){
							JOptionPane.showMessageDialog(null, "请在"+configFilesPath+"路径下配置"+str+"印章文件！");
							return flag;
						}
					}
				}
			}
		}catch(NoSuchElementException e){
			JOptionPane.showMessageDialog(null, "配置文件不完整!");
			e.printStackTrace();
			return flag;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "配置初始化校验出错!");
			e.printStackTrace();
			return flag;
		}
		flag = true;
		return flag;
		
	}
	
	/**重复应用开启检测
	 * @param lockFilePath
	 * @return
	 */
	public boolean uniqueCheck(String lockFilePath){
		boolean flag = false;
		try {
			flag = FileUtils.fileLock(lockFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}
	

}
