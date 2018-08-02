package com.app.frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.*;


import com.app.Listener.JbActionListener;
import com.app.Listener.MenuMouseListener;
import com.app.bean.FileType;
import com.app.bean.SettingBean;
import com.app.callback.CommonCallback;
import com.app.common.Constants;
import com.app.common.PicFunction;
import com.app.common.TestCommonFunction;
import com.app.dialog.SelectDialog;
import com.app.filewatcher.FileActionCallback;
import com.app.filewatcher.WatchDir;
import com.app.ocr.Tesseract1;
import com.app.panel.BackgroundPanel;
import com.app.thread.UploadThread;
import com.app.thread.WatchThread;
import com.app.utils.FileUtils;
import com.app.utils.ImageCheck;
import com.app.utils.NewImageUtils;
import com.app.utils.PropertiesLoader;
import com.eltima.components.ui.DatePicker;
import com.sun.org.glassfish.external.arc.Taxonomy;

/**
 * @author ERIC
 *
 */
public class MainFrame extends BaseSettingFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanle = null;					//主容器
	private JTabbedPane jTabbedpane = null;// 存放选项卡的组件
    private JPanel jContentPane = null;					//主页面
    private JPanel jContentPaneUpLoad = null;			//上传xml文件页面
    private JPanel jContentPaneDis = null;				//上传作废数据页面
    private URL url = null;								// 声明图片的URL
    private Image image=null;							// 声明图像对象
    private BackgroundPanel jPanel = null;				// 声明自定义背景面板对象
    private BackgroundPanel searchPanel = null;			// 搜索背景面板
    private JMenuBar jJMenuBar = null;
    private JMenu searchMenu = null;					// 查询
    private JMenuItem batchPrintMenuItem = null;		// 开始打印监听
    private JMenuItem printTestMenuItem = null;			// 打印调试
    private JMenu printMenu = null;						// 打印管理
    private JMenu viewPicMenu = null;					// 预览最后一张打印图片
    private JMenu sysMenu = null;						// 系统管理
    private JMenu aboutMenu = null;						// 关于
    private JMenuItem maintainMenuItem = null;			// 运维信息
    private JMenuItem printInvoiceMenuItem = null;		// 打印发票数据
    private JMenuItem stopFileWatcherMenuItem = null;	// 停止自动打印监听
    private JMenuItem uploadMenuItem = null;			// 重传按钮
    private JMenuItem jMenuItem4 = null;
    private JMenuItem jMenuItem5 = null;
    private JMenuItem jMenuItem1 = null;
    private JLabel startDateLabel = null;				// 开始日期标签
    private JLabel endDateLabel = null;					// 结束日期标签
    private JTextField startDateField = null;			// 开始日期文本
    private JTextField endDateField = null;				// 结束日期文本
    private DatePicker startDatePicker = null;
    private String DefaultFormat = "yyyyMMdd";
    private PropertiesLoader loader;					//配置文件读取
    private WatchThread watchThread;					//监控线程
    private WatchThread uploadWatchThread;				//自动上传监控线程
    private Thread consumerThread;						//消费线程
    private Thread refreshThread;						//遗漏监听线程
    private BlockingQueue<String> resourseQueue;		//资源队列
    private BlockingQueue<FileType> uploadQueue;			//文件上传队列
    private ArrayList<FileType> uploadFailList;			//文件重传列表
    private UploadThread uploadThread;					//自动上传文件线程
    private Map<String, String> map = new HashMap<String, String>();
    
    private JRadioButton invoiceType;					//发票类型
    private JRadioButton invoiceListType;				//发票列表类型
    private boolean invoiceTypeState = true;			//发票类型选中状态 默认选中
    private boolean invoiceListTypeState = false;		//发票列表类型选中状态
    private ButtonGroup  printTypeBg;					//打印类型
    private String selectType = INVOICE_TYPE;			//默认为打印发票	
    private JTextArea jTextArea;						//控制台输出域
    private JButton	clearButton;						//清空控制台按钮
    private Tesseract1 instance;						//ocr 实例
    private String selectedInvoice;						//已选择税号
    private ImageCheck icheck;
    
    private JButton startBtn;							//开始按钮
    private JButton stopBtn;							//结束按钮
    private JCheckBox checkBox;							//打印历史
    private String uuidchange = UUID.randomUUID().toString().replaceAll("-", "");//打印变动
    
    private CommonCallback commonCallback;				//通用回调方法
    
    /**
     * @param
     */
    public MainFrame() {
        super();
        initialize();
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
    	icheck = new ImageCheck();
    	instance = new Tesseract1();
    	printTypeBg = new ButtonGroup();
        loader = new PropertiesLoader("app.properties");
        resourseQueue = new LinkedBlockingQueue<String>();
        uploadQueue = new LinkedBlockingQueue<FileType>();
		File file = new File(waterFilePath);
        //监听线程初始化
        watchThread = new WatchThread(file,false,new FileActionCallback() {
        	@Override
        	public void create(File file) {
        		try {
        			resourseQueue.put(file.getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        	
        	@Override
        	public void reFresh(){
        		//事件丢失
        		System.err.println("文件变动事件遗失！");
        	}
		});
		watchThread.run();
		
		//遗漏监听线程
		refreshThread = new Thread(new Runnable() {
			String thisUuidchange = "1";
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronized (uuidchange) {
						/*System.out.println("check");*/
						if(thisUuidchange.equals(uuidchange)&& !watchThread.isSuspend()){
							//refresh;
							ArrayList<String> fileNames = FileUtils.listFile(new File(waterFilePath));
							if(fileNames!=null && fileNames.size()>0){
								for(String fileName : fileNames){
									if(!resourseQueue.contains(fileName)){
										try {
											resourseQueue.put(fileName);
										} catch (InterruptedException e) {
											e.printStackTrace();
											continue;
										}
									}
								}
							}
						}else{
							thisUuidchange = uuidchange;
						}
					}
				}
			}
		});
		refreshThread.start();
			
		
		
        //消费线程初始化
        consumerThread = new Thread(new Runnable() {
        	String fileName ;
        	long oldLen ;
        	long newLen ;
        	boolean printFlag = false;
        	boolean containFlag = false;
        	File file;
			@Override
			public void run() {
				while(true){
					oldLen = 0;
					newLen = 0;
					printFlag = false;
					containFlag = false;
					try {
						fileName = resourseQueue.take();
						synchronized (uuidchange) {
							uuidchange = UUID.randomUUID().toString().replaceAll("-", "");
						}
						while(true){
							file = new File(waterFilePath+fileName);
							newLen = file.length();
							if((newLen - oldLen) > 0){
								oldLen = newLen;
								Thread.sleep(200);
							}else{
								break;
							} 
						}
						if(icheck.isImageWithOutMimetype(file)){
							if(selectType.equals(INVOICE_TYPE)){
								containFlag = recordSet.contains(fileName);
								if(!containFlag && checkBox.isSelected()){
									printFlag = PicFunction.printPicsWithCheck(waterFilePath+fileName, fileName,settingBean,INVOICE_NUM,instance,rect);
								}else if(!checkBox.isSelected()){
									printFlag = PicFunction.printPicsWithCheck(waterFilePath+fileName, fileName,settingBean,INVOICE_NUM,instance,rect);
								}else{
									;
								}
								FileUtils.deleteFile(new File(waterFilePath+fileName));
							}else if(selectType.equals(INVOICE_LIST_TYPE)){
								containFlag = listRecordSet.contains(fileName);
								if(!containFlag && checkBox.isSelected()){
									printFlag = PicFunction.printListPic(waterFilePath+fileName, fileName,settingBean,selectedInvoice);
								}else if(!checkBox.isSelected()){
									printFlag = PicFunction.printListPic(waterFilePath+fileName, fileName,settingBean,selectedInvoice);
								}else{
									;
								}
								FileUtils.deleteFile(new File(waterFilePath+fileName));
							}else{
								;
							}
							if(printFlag){
								appendText("已打印"+fileName.substring(0, fileName.lastIndexOf(".")+1)+"jpg",true);
								if(selectType.equals(INVOICE_TYPE)){
									FileUtils.writeToFile(printRecordPath, fileName+"\r\n");
									recordSet.add(fileName);
									uploadQueue.add(new FileType(fileName, Constants.Dict.INVOICE_IMAGE.getValue()));
								}else{
									FileUtils.writeToFile(printListRecordPath, fileName+"\r\n");
									listRecordSet.add(fileName);
									uploadQueue.add(new FileType(fileName, Constants.Dict.INVOICELIST_IMAGE.getValue()));
								}
								
							}else{
								if(containFlag && checkBox.isSelected() && selectType.equals(INVOICE_TYPE)){
									appendText(fileName.substring(0, fileName.lastIndexOf(".")+1)+"jpg 已经打印过，打印失败！",true);
								}else if(containFlag && checkBox.isSelected() && selectType.equals(INVOICE_LIST_TYPE)){
									appendText(fileName.substring(0, fileName.lastIndexOf(".")+1)+"jpg 已经打印过，打印失败！",true);
								}
								else{
									appendText("打印"+fileName.substring(0, fileName.lastIndexOf(".")+1)+"jpg 失败!",true);
								}
							}
						}else{
							appendText("", true);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						//此处还需保存生成失败的图片名称
						continue;
					}
				}
			}
		});
        //开启消费线程
        consumerThread.start();
        //开启上传线程
        if(isUpload){
        	startUploadThread();
        }
        this.setSize(575, 565);
        this.setJMenuBar(getJJMenuBar());
        this.setTitle("ACT发票图片生成软件 V3.1");
        /*this.setContentPane(getJContentPane());*/
        this.setContentPane(getMainPanle());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		super.windowClosed(e);
        		if(resourseQueue.size()>0){
        			int n=JOptionPane.showConfirmDialog(null, "当前有打印任务正在进行，关闭程序打印任务将强制终止，是否确认关闭？","警告",JOptionPane.OK_CANCEL_OPTION);
        			if(n == 0){
        				System.exit(0);
        			}
        		}else{
        			int n=JOptionPane.showConfirmDialog(null, "是否确认关闭？","警告",JOptionPane.OK_CANCEL_OPTION);
        			if(n == 0){
        				System.exit(0);
        			}
        		}
        	}
		});
    }
    private JPanel getMainPanle(){
    	if(mainPanle==null){
    		mainPanle = new JPanel(new GridLayout(1, 1));
    		/*mainPanle.add("JContentPane",getJContentPane());//主页面
			mainPanle.add("JContentPaneUpLoad",getjContentPaneUpLoad()); // 上传页面
			mainPanle.add("JContentPaneDis",getjContentPaneDis());//作废页面*/
			mainPanle.add(getJTabbedpane());
		}
		return mainPanle;
	}

   	private JTabbedPane getJTabbedpane(){
    	if(jTabbedpane == null){
    		jTabbedpane = new JTabbedPane();
    		ImageIcon homeIcon = new ImageIcon(imagePath+java.io.File.separator+"home.png");
    		jTabbedpane.addTab("主页",homeIcon,getJContentPane(),"主页");
    		ImageIcon uploadIcon = new ImageIcon(imagePath+"/upload.png");
			jTabbedpane.addTab("上传页",uploadIcon,getjContentPaneUpLoad(),"上传数据");
			ImageIcon disIcon = new ImageIcon(imagePath+"/disable.png");
			jTabbedpane.addTab("作废页",disIcon,getjContentPaneDis(),"发票作废");
		}
		return jTabbedpane;
	}

	private JPanel getjContentPaneUpLoad(){
    	if(jContentPaneUpLoad == null){
    		jContentPaneUpLoad = new JPanel();
    		jContentPaneUpLoad.setBackground(Color.RED);
		}
		return jContentPaneUpLoad;
	}

	private JPanel getjContentPaneDis(){
		if(jContentPaneDis == null ){
			jContentPaneDis = new JPanel();
			jContentPaneDis.setBackground(Color.BLUE);
		}
		return jContentPaneDis;
	}
    
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private BackgroundPanel getJPanel() {
        if (jPanel == null) {
            try {
				image=javax.imageio.ImageIO.read(new File(imagePath+"/main_frame.bmp"));
			} catch (IOException e) {
				e.printStackTrace();
			}
            jPanel = new BackgroundPanel(image);
            jPanel.setLayout(null);
            jPanel.add(getSearchPanel());
            jPanel.add(getJTextArea());
            
        }
        return jPanel;
    }
    
    private BackgroundPanel getSearchPanel(){
    	Image image = null;
    	if(searchPanel==null){
    		try {
				image=javax.imageio.ImageIO.read(new File(imagePath+"/search_frame.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
    		searchPanel = new BackgroundPanel(image);
    		searchPanel.setBounds(new Rectangle(0, 3, 1170, 55));
    		searchPanel.setLayout(null);
    		/*searchPanel.add(getStartDatePicker());*/
    		searchPanel.add(getInvoiceType());
    		searchPanel.add(getInvoiceListType());
            startDateLabel = new JLabel();
            startDateLabel.setBounds(new Rectangle(5, 15, 70, 18));
            startDateLabel.setText("打印类型：");
            searchPanel.add(startDateLabel);
            searchPanel.add(getClearButton());
            searchPanel.add(getStartBtn());
            searchPanel.add(getStopBtn());
            searchPanel.add(getCheckBox());
    	}
    	return searchPanel;
    }
    
    private JScrollPane getJTextArea(){
    	this.jTextArea = new JTextArea();
    	jTextArea.setLineWrap(true);//激活自动换行功能 
        jTextArea.setWrapStyleWord(true);//激活断行不断字功能 
        jTextArea.setEditable(false);
        jTextArea.append("==** 欢迎使用ACT发票图片生成软件**==\n\n");
        //为JTextArea添加滚动条
        JScrollPane jsp = new JScrollPane(jTextArea);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        jsp.setBounds(0, 55, 584, 450);
        return jsp;
    }

    /**
     * This method initializes jJMenuBar	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
           /* jJMenuBar.add(getSearchMenu());*/
            
            /*jJMenuBar.add(getViewPicMenu());*/
            //打印按钮调整位置不放在菜单栏里
           /* jJMenuBar.add(getPrintMenu());*/
            jJMenuBar.add(getAboutMenu());
            jJMenuBar.add(getSysMenu());
        }
        return jJMenuBar;
    }
    

    public JMenu getAboutMenu() {
    	if(aboutMenu == null){
    		aboutMenu = new JMenu();
    		aboutMenu.setText("帮助中心");
    		aboutMenu.setIcon(new ImageIcon(imagePath+"/about.png"));
    		aboutMenu.add(getMaintainMenuItem());
    	}
		return aboutMenu;
	}
    
	/**
     * This method initializes jMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getSearchMenu() {
        if (searchMenu == null) {
        	searchMenu = new JMenu();
        	searchMenu.setText("查询");
            
        }
        searchMenu.setIcon(new ImageIcon(imagePath+"/search.png"));
        try {
			searchMenu.addMouseListener(new MenuMouseListener(new TestCommonFunction().getClass(),"testFunction",null));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
        return searchMenu;
    }
    
    private JMenu getViewPicMenu(){
    	if(viewPicMenu == null){
    		viewPicMenu = new JMenu();
    		viewPicMenu.setText("预览最后一张图片");
    	}
    	Object[] objects = new Object[1];
    	//预览图片地址
    	objects[0] = "D:/tempImage/newImage.jpg";
    	viewPicMenu.setIcon(new ImageIcon(imagePath+"/view_pic.png"));
    	try {
    		viewPicMenu.addMouseListener(new MenuMouseListener(new PicFunction().getClass(),"viewPic",objects));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
    	return viewPicMenu;
    }

    /**
     * This method initializes batchPrintMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getBatchPrintMenuItem() {
        if (batchPrintMenuItem == null) {
        	batchPrintMenuItem = new JMenuItem();
        	batchPrintMenuItem.setText("开启自动打印");
        	batchPrintMenuItem.setIcon(new ImageIcon(imagePath+"/start.png"));
        	batchPrintMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	if(watchThread.getSuspend()){
                		PicFunction.emptyInit(waterFilePath);
                		watchThread.setSuspend(false);
                		JOptionPane.showMessageDialog(MainFrame.this, "自动打印功能已开启！（进行打印时请务必选择使用“zan黑白图像打印机”\n，打印机选择错误可能造成真实发票套打！）");
                		appendText("自动打印功能已开启！",false);
                	}else{
                		JOptionPane.showMessageDialog(MainFrame.this, "自动打印已为开启状态，不能重复开启！");
                	}
                }
            });
        }
        return batchPrintMenuItem;
    }
    
    
    /**
     *  This method initializes stopFileWatcherMenuItem	
     *  
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getStopFileWatcherMenuItem(){
    	if(stopFileWatcherMenuItem == null){
    		stopFileWatcherMenuItem = new JMenuItem();
    		stopFileWatcherMenuItem.setText("停止自动打印");
    		stopFileWatcherMenuItem.setIcon(new ImageIcon(imagePath+"/stop.png"));
    		stopFileWatcherMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					watchThread.setSuspend(true);
					JOptionPane.showMessageDialog(MainFrame.this, "自动打印功能已停止");
					appendText("自动打印功能已停止！",false);
				}
			});
    	}
    	return stopFileWatcherMenuItem;
    }

    /**
     * This method initializes print10MenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getPrintTestMenuItem() {
        if (printTestMenuItem == null) {
        	printTestMenuItem = new JMenuItem();
        	printTestMenuItem.setText("打印位置调试");
        	printTestMenuItem.setIcon(new ImageIcon(imagePath+"/print_10.png"));
        	printTestMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	PrintTestFrame thisClass = new PrintTestFrame(settingBean,new CommonCallback() {
                		@Override
                		public void doAction() {
                			reloadSetting();                			
                		}
					});
                    thisClass.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    Toolkit tookit = thisClass.getToolkit();
                    Dimension dm = tookit.getScreenSize();
                    thisClass.setLocation(
                            (dm.width - thisClass.getWidth()) / 2,
                            (dm.height - thisClass.getHeight()) / 2);
                    thisClass.setVisible(true);
                }
            });
        }
        return printTestMenuItem;
    }

    /**
     * This method initializes printMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getPrintMenu() {
        if (printMenu == null) {
        	printMenu = new JMenu();
        	printMenu.setText("打印管理");
        	printMenu.add(getBatchPrintMenuItem());
        	printMenu.add(getStopFileWatcherMenuItem());
        	/*printMenu.add(getPrint10MenuItem());*/
        	/*printMenu.add(getPrintInvoiceMenuItem());*/
        	printMenu.setIcon(new ImageIcon(imagePath+"/manage_drak.png"));
        }
        return printMenu;
    }

    /**
     * This method initializes sysMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getSysMenu() {
        if (sysMenu == null) {
        	sysMenu = new JMenu();
        	sysMenu.setText("系统设置");
        	/*sysMenu.add(getJMenuItem1());
        	sysMenu.add(getJMenuItem4());*/
        	if(debug){
        		sysMenu.add(getPrintTestMenuItem());
        	}
        	if(isUpload){
        		sysMenu.add(getUploadMenuItem());
        	}
        	sysMenu.add(getJMenuItem5());
        	sysMenu.setIcon(new ImageIcon(imagePath+"/system.png"));
        }
        return sysMenu;
    }
    
    

    private JMenuItem getMaintainMenuItem() {
    	if(maintainMenuItem == null){
    		maintainMenuItem = new JMenuItem();
    		maintainMenuItem.setText("运维帮助");
    		maintainMenuItem.setIcon(new ImageIcon(imagePath+"/print_10.png"));
    		maintainMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(MainFrame.this, " 广州安创：020-82328212  \n      "
							+ "    吴工：17724016490");
				}
			});
    	}
		return maintainMenuItem;
	}

	/**
     * This method initializes printInvoiceMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getPrintInvoiceMenuItem() {
        if (printInvoiceMenuItem == null) {
        	printInvoiceMenuItem = new JMenuItem();
        	printInvoiceMenuItem.setText("打印发票数据");
        	printInvoiceMenuItem.setIcon(new ImageIcon(imagePath+"/print_invoice.png"));
        	printInvoiceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	JOptionPane.showMessageDialog(null, "点击了打印发票数据。");
                }
            });
        }
        return printInvoiceMenuItem;
    }
    
    

    private JMenuItem getUploadMenuItem() {
    	if(uploadMenuItem==null){
    		uploadMenuItem = new JMenuItem();
    		uploadMenuItem.setText("立即上传失败文件");
    		uploadMenuItem.setIcon(new ImageIcon(imagePath+"/upload.png"));
    		uploadMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					push2Queue();
				}
			});
    	}
		return uploadMenuItem;
	}

	/**
     * This method initializes jMenuItem4	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItem4() {
        if (jMenuItem4 == null) {
            jMenuItem4 = new JMenuItem();
            jMenuItem4.setText("修改用户密码");
            jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    
                }
            });
        }
        return jMenuItem4;
    }

    /**
     * This method initializes jMenuItem5	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItem5() {
        if (jMenuItem5 == null) {
            jMenuItem5 = new JMenuItem();
            jMenuItem5.setText("退出系统");
            jMenuItem5.setIcon(new ImageIcon(imagePath+"/exit.png"));
            jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return jMenuItem5;
    }

    /**
     * This method initializes jMenuItem1	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItem1() {
        if (jMenuItem1 == null) {
            jMenuItem1 = new JMenuItem();
            jMenuItem1.setText("添加用户");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    
                }
            });
        }
        return jMenuItem1;
    }

	/**
	 * This method initializes startDatePicker
	 * 
	 * @return DatePicker
	 */
	private DatePicker getStartDatePicker() {
		Date date = new Date();
		// 字体
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        
        Dimension dimension = new Dimension(120, 24);
        
       /* int[] hilightDays = { 1, 3, 5, 7 };
   
        int[] disabledDays = { 4, 6, 5, 9 };*/
   
        this.startDatePicker = new DatePicker(date, DefaultFormat, font, dimension);
   
        startDatePicker.setLocation(78, 19);
        startDatePicker.setBounds(78, 19, 120, 24);
       /* // 设置一个月份中需要高亮显示的日子
        startDatePicker.setHightlightdays(hilightDays, Color.red);
        // 设置一个月份中不需要的日子，呈灰色显示
        startDatePicker.setDisableddays(disabledDays);*/
        // 设置国家
        startDatePicker.setLocale(Locale.CHINA);
        /*// 设置时钟面板可见
        startDatePicker.setTimePanleVisible(true);*/
		return startDatePicker;
	}
	
	private JRadioButton getInvoiceType(){
		invoiceType = new JRadioButton("打印发票",true);
		invoiceType.setBounds(70, 1, 80, 24);
		invoiceType.setBackground(new Color(Integer.parseInt("f6f6f6", 16)));
		invoiceType.setActionCommand("发票");
		/*invoiceType.addActionListener(new JbActionListener(selectType, watchThread.getSuspend(),resourseQueue.size()));*/
		invoiceType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if( watchThread.getSuspend()&& resourseQueue.size() == 0 ){
						selectType = BaseFrame.INVOICE_TYPE;
						invoiceTypeState = true;
						invoiceListTypeState = false;
						appendText("选择打印发票！", false);
				}else if(!watchThread.getSuspend()){
					JOptionPane.showMessageDialog(null, "请先停止自动打印才能修改打印类型！");
					if(invoiceTypeState&&invoiceType.isSelected()){
						;
					}else{
						invoiceListType.setSelected(true);
						invoiceTypeState = false;
						invoiceListTypeState = true;
					}
					
				}else if(resourseQueue.size()!=0){
					JOptionPane.showMessageDialog(null, "打印任务正在进行，请等待打印完成在进行类型修改！");
					if(invoiceTypeState&&invoiceType.isSelected()){
						;
					}else{
						invoiceListType.setSelected(true);
						invoiceTypeState = false;
						invoiceListTypeState = true;
					}
				}else{
					JOptionPane.showMessageDialog(null, "修改类型失败！");
					if(invoiceTypeState&&invoiceType.isSelected()){
						;
					}else{
						invoiceListType.setSelected(true);
						invoiceTypeState = false;
						invoiceListTypeState = true;
					}
				}
			}
		});
		printTypeBg.add(invoiceType);
		return invoiceType;
	}
	
	private JRadioButton getInvoiceListType(){
		invoiceListType = INVOICE_COUNT > 1 ? new JRadioButton("打印清单（点击选择税号）") : new JRadioButton("打印清单");
		invoiceListType.setActionCommand("打印清单");
		invoiceListType.setBackground(new Color(Integer.parseInt("f6f6f6", 16)));
		invoiceListType.setBounds(70, 25, 80, 24);
		invoiceListType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if( watchThread.getSuspend()&& resourseQueue.size() == 0 ){
						selectType = BaseFrame.INVOICE_LIST_TYPE;
						invoiceListTypeState = true;
						invoiceTypeState = false;
						if(INVOICE_COUNT<=1){
							appendText("选择打印清单！", false);
						}
						//多税号需选择，用于保存路径或者印章的选择
						if(INVOICE_COUNT>1 ){
							SelectDialog selectDialog = new SelectDialog(MainFrame.this, "税号选择",INVOICE_NUM,selectedInvoice,new CommonCallback() {
								@Override
								public void doAction(String str) {
									selectedInvoice = str;
									invoiceListType.setText("打印清单（"+str+"）");
									appendText("选择打印"+str+"税号的清单！", false);
								}
							});
							selectDialog.setVisible(true);
						}
				}else if(!watchThread.getSuspend()){
					JOptionPane.showMessageDialog(null, "请先停止自动打印才能修改打印类型！");
					if(invoiceListTypeState&&invoiceListType.isSelected()){
						;
					}else{
						invoiceType.setSelected(true);
						invoiceListTypeState = false;
						invoiceTypeState = true;
					}
					
				}else if(resourseQueue.size()!=0){
					JOptionPane.showMessageDialog(null, "打印任务正在进行，请等待打印完成在进行类型修改！");
					if(invoiceListTypeState&&invoiceListType.isSelected()){
						;
					}else{
						invoiceType.setSelected(true);
						invoiceListTypeState = false;
						invoiceTypeState = true;
					}
				}else{
					JOptionPane.showMessageDialog(null, "修改类型失败！");
					if(invoiceListTypeState&&invoiceListType.isSelected()){
						;
					}else{
						invoiceType.setSelected(true);
						invoiceListTypeState = false;
						invoiceTypeState = true;
					}
				}
			}
		});
		printTypeBg.add(invoiceListType);
		return invoiceListType;
	}
	
	/**输出提示
	 * @param str
	 * @param isRemain 剩余数量
	 */
	private void appendText(String str,boolean isRemain){
		if(this.jTextArea == null){
			return;
		}
		if(isRemain){
			this.jTextArea.append(str+" 剩余:"+resourseQueue.size()+"\n");
		}else{
			this.jTextArea.append(str+"\n");
		}
		this.jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
	}

	/** 初始化清空按钮
	 * @return
	 */
	private JButton getClearButton() {
		if(clearButton == null){
			clearButton = new JButton();
			/*clearButton.setText("清空");*/
			clearButton.setBounds(530, 13, 24, 24);
			clearButton.setContentAreaFilled(false);
			clearButton.setBorder(null);
			clearButton.setIcon(new ImageIcon(imagePath+"/clear.png"));
			clearButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(jTextArea!=null){
						jTextArea.setText("");
					}
				}
			});
		}
		return clearButton;
	}
	
	
	
	/**初始化开始按钮
	 * @return
	 */
	public JButton getStartBtn() {
		if(startBtn==null){
			startBtn = new JButton();
			startBtn.setBounds(270, 8, 128, 32);
			startBtn.setContentAreaFilled(false);
			startBtn.setRolloverIcon(new ImageIcon(imagePath+"/start_big_blue.png"));
			startBtn.setBorder(null);
			startBtn.setIcon(new ImageIcon(imagePath+"/start_nor.png"));
			startBtn.setPressedIcon(new ImageIcon(imagePath+"/start_nor_blue.png"));
			startBtn.setText("开启打印");
			startBtn.setFocusable(false);
			startBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*JOptionPane.showMessageDialog(MainFrame.this, "点击了开始");*/
					if(watchThread.getSuspend()){
                		PicFunction.emptyInit(waterFilePath);
                		stopBtn.setIcon(new ImageIcon(imagePath+"/stop_nor.png"));
                		startBtn.setIcon(new ImageIcon(imagePath+"/start_nor_blue.png"));
                		watchThread.setSuspend(false);
                		JOptionPane.showMessageDialog(MainFrame.this, "自动打印功能已开启！（进行打印时请务必选择使用“zan黑白图像打印机”\n，打印机选择错误可能造成真实发票套打！）");
                		appendText("自动打印功能已开启！",false);
                	}else{
                		JOptionPane.showMessageDialog(MainFrame.this, "自动打印已为开启状态，不能重复开启！");
                	}
					//隐藏白边
					startBtn.setFocusable(false);
				}
			});
			
		}
		return startBtn;
	}

	/**初始化结束按钮
	 * @return
	 */
	public JButton getStopBtn() {
		if(stopBtn == null){
			stopBtn = new JButton();
			stopBtn.setBounds(385, 8, 128, 32);
			stopBtn.setContentAreaFilled(false);
			stopBtn.setBorder(null);
			stopBtn.setIcon(new ImageIcon(imagePath+"/stop_nor.png"));
			stopBtn.setRolloverIcon(new ImageIcon(imagePath+"/stop_big_red.png"));
			stopBtn.setPressedIcon(new ImageIcon(imagePath+"/stop_nor_red.png"));
			stopBtn.setText("停止打印");
			stopBtn.setFocusable(false);
			stopBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					stopBtn.setIcon(new ImageIcon(imagePath+"/stop_nor_red.png"));
					startBtn.setIcon(new ImageIcon(imagePath+"/start_nor.png"));
					watchThread.setSuspend(true);
					JOptionPane.showMessageDialog(MainFrame.this, "自动打印功能已停止");
					appendText("自动打印功能已停止！",false);
					//隐藏白边
					stopBtn.setFocusable(false);
					
				}
			});
		}
		return stopBtn;
	}
	
	

	private JCheckBox getCheckBox() {
		if(checkBox == null){
			checkBox = new JCheckBox();
			checkBox.setText("启用打印历史");
			checkBox.setSelected(true);
			checkBox.setBounds(160, 12, 110, 24);
			checkBox.setContentAreaFilled(false);
			checkBox.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!checkBox.isSelected()){
						checkBox.setSelected(false);
					}else{
						checkBox.setSelected(true);
					}
					
				}
			});
		}
		return checkBox;
	}

	/**
	 * 重载配置文件
	 */
	public void reloadSetting(){
		this.initLoader = new PropertiesLoader("app.properties");
		
		this.filePath = configFilesPath + initLoader.getProperty("filePath");
		this.zFilePath = configFilesPath + initLoader.getProperty("zFilePath");
		/*this.stampPath = configFilesPath + initLoader.getProperty("stampPath");*/
		
		this.waterFilePath = initLoader.getProperty("waterFilePath");
		this.savePath =  initLoader.getProperty("savePath");
		this.listSavePath = initLoader.getProperty("listSavePath");
		this.x = initLoader.getIntegerProperty("x");
		this.y = initLoader.getIntegerProperty("y");
		this.numX = initLoader.getIntegerProperty("numX");
		this.numY = initLoader.getIntegerProperty("numY");
		this.codeX = initLoader.getIntegerProperty("codeX");
		this.codeY = initLoader.getIntegerProperty("codeY");
		this.isStamp = initLoader.getBoolean("isStamp");
		this.isUpload = initLoader.getBoolean("isUpload");
		this.alpha = initLoader.getIntegerProperty("alpha");
		
		this.rectX = initLoader.getIntegerProperty("rectX");
		this.rectY = initLoader.getIntegerProperty("rectY");
		this.rectWidth = initLoader.getIntegerProperty("rectWidth");
		this.rectHeight = initLoader.getIntegerProperty("rectHeight");
		this.rect = new Rectangle(rectX,rectY,rectWidth,rectHeight);
		
		if(isStamp){
			this.stampX = initLoader.getIntegerProperty("stampX");
			this.stampY = initLoader.getIntegerProperty("stampY");
			this.lStampX = initLoader.getIntegerProperty("lStampX");
			this.lStampY = initLoader.getIntegerProperty("lStampY");
		}
		settingBean.setX(this.x);
		settingBean.setY(this.y);
		settingBean.setNumX(this.numX);
		settingBean.setNumY(this.numY);
		settingBean.setCodeX(this.codeX);
		settingBean.setCodeY(this.codeY);
		settingBean.setIsStamp(this.isStamp);
		settingBean.setStampX(this.stampX);
		settingBean.setStampY(this.stampY);
		settingBean.setAlpha(this.alpha);
		settingBean.setFilePath(this.filePath);
		/*settingBean.setStampPath(this.stampPath);*/
		settingBean.setWaterFilePath(this.waterFilePath);
		settingBean.setSavePath(this.savePath);
		settingBean.setzFilePath(this.zFilePath);
		settingBean.setListSavePath(this.listSavePath);
		settingBean.setlStampX(this.lStampX);
		settingBean.setlStampY(this.lStampY);
		
		settingBean.setRectX(this.rectX);
		settingBean.setRectY(this.rectY);
		settingBean.setRectWidth(this.rectWidth);
		settingBean.setRectHeight(this.rectHeight);
		settingBean.setRect(this.rect);
		settingBean.setIsUpload(this.isUpload);
	}
	
	/**
	 * 失败列表推入上传队列
	 */
	public void push2Queue(){
		if(uploadFailList == null || uploadFailList.size() == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "没有文件需要上传！");
			return;
		}
		uploadQueue.addAll(uploadFailList);
		uploadFailList.clear();
	}
	
	/**
	 * 启动自动上传线程
	 */
	public void startUploadThread(){
		if(uploadQueue == null){
			uploadQueue = new LinkedBlockingQueue<FileType>();
		}
		if(uploadFailList == null){
			uploadFailList = new ArrayList<FileType>();
		}
		//启动检查上传文件夹是否有未上传的文件
		checkUnUploadFiles();
		
		this.commonCallback = new CommonCallback() {
			@Override
			public void doAction(String str) {
				super.doAction();
				appendText(str, false);
			}
			@Override
			public void doAction(String str,String code,FileType type) {
				super.doAction(str);
				//上传不成功并且文件不是不可识别类型的，添加到重传队列
				if(!code.equals(Constants.Dict.UPLOAD_SUCCESS.getValue())
						&&!code.equals(Constants.Dict.UPLOAD_ILLEGAL_FILE.getValue())){
					//添加上传失败数据
					uploadFailList.add(type);
				}
				appendText(str, false);
			}
		};
		if(settingBean.getIsUpload()){
			//文件上传参数
			map.put("invoice", this.INVOICE_NUM);
			File file = new File(settingBean.getUploadFilesPath());
			//监听线程初始化
	        this.uploadWatchThread = new WatchThread(file,false,new FileActionCallback() {
	        	@Override
	        	public void create(File file) {
	        		try {
	        			FileType fileType = new FileType(file.getName(),Constants.Dict.FILE.getValue());
	        			uploadQueue.put(fileType);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        	}
			});
	        this.uploadWatchThread.run();
	        this.uploadWatchThread.setSuspend(false);
	        //自动文件上传线程
	        this.uploadThread = new UploadThread(map, settingBean,this.uploadQueue,this.commonCallback,Constants.Dict.FILE.getValue());
	        this.uploadThread.start();
		}
	}
	
	/**启动时检查未上传完成的文件加入上传队列自动上传
	 * 
	 */
	public void checkUnUploadFiles(){
		File fileDic  = new File(settingBean.getUploadFilesPath());
		File invoiceDic = new File(settingBean.getUploadInvoicePath());
		File invoiceListDic = new File(settingBean.getUploadInvoiceListPath());
		if(fileDic.isDirectory()){
			ArrayList<String> tempFiles = FileUtils.listFile(fileDic);
			if(tempFiles != null && tempFiles.size()>0){
				for(String fileName:tempFiles){
					uploadQueue.add(new FileType(fileName, Constants.Dict.FILE.getValue()));
				}
			}
		}
		if(invoiceDic.isDirectory()){
			ArrayList<String> tempFiles = FileUtils.listFile(invoiceDic);
			if(tempFiles != null && tempFiles.size()>0){
				for(String fileName:tempFiles){
					uploadQueue.add(new FileType(fileName, Constants.Dict.INVOICE_IMAGE.getValue()));
				}
			}
		}
		if(invoiceListDic.isDirectory()){
			ArrayList<String> tempFiles = FileUtils.listFile(invoiceListDic);
			if(tempFiles != null && tempFiles.size()>0){
				for(String fileName:tempFiles){
					uploadQueue.add(new FileType(fileName, Constants.Dict.INVOICELIST_IMAGE.getValue()));
				}
			}
		}
	}
	
}
