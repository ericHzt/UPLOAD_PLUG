package com.app.panel;

import com.app.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.app.frame.BaseFrame.imagePath;

public class JContentPaneUpLoad extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image image; // 定义图像对象
    private JPanel searchPanel;
    private JTextArea textArea;
    private JLabel lastUploadLabel;
    private JLabel messageLabel;
    public JContentPaneUpLoad(Image image,String message) {
        super(); // 调用超类的构造方法
        this.image = image; // 为图像对象赋值
        initialize(message);
    }

    /*
     * 重写paintComponent方法
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 调用父类的方法
        Graphics2D g2 = (Graphics2D) g; // 创建Graphics2D对象
        if (image != null) {
            int width = getWidth(); // 获得面板的宽度
            int height = getHeight(); // 获得面板的高度
            // 绘制图像
            g2.drawImage(image, 0, 0, width, height, this);
        }
    }
    private void initialize(String message) {
        this.setSize(575, 575);
        this.setLayout(null);
        this.add(getSearchPanel(message));
        this.add(getTextArea());
    }

    public JPanel getSearchPanel(String message) {
        Image image = null;
        if(searchPanel==null){
            try {
                image=javax.imageio.ImageIO.read(new File(imagePath+"/search_frame.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            searchPanel = new BackgroundPanel(image);
            searchPanel.setBounds(new Rectangle(0, 3, 575, 55));
            searchPanel.setLayout(null);
            lastUploadLabel = new JLabel();
            lastUploadLabel.setBounds(new Rectangle(5, 5, 575, 18));
            lastUploadLabel.setText("最后一次上传成功时间：");
            searchPanel.add(lastUploadLabel);
            searchPanel.add(getMessageLabel(message));
        }
        return searchPanel;
    }

    public JScrollPane getTextArea() {
        this.textArea = new JTextArea();
        textArea.setLineWrap(true);//激活自动换行功能
        textArea.setWrapStyleWord(true);//激活断行不断字功能
        textArea.setEditable(false);
        textArea.append("==** 欢迎使用ACT发票图片采集系统**==\n\n");
        //为JTextArea添加滚动条
        JScrollPane jsp = new JScrollPane(textArea);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        jsp.setBounds(0, 55, 584, 450);
        return jsp;
    }

    public JLabel getMessageLabel(String message){
        if(messageLabel==null){
            messageLabel = new JLabel(message);
            messageLabel.setBounds(new Rectangle(5, 30, 575, 18));
        }
        return messageLabel;
    }

    public void updateLabel(String str){
        if(StringUtils.isBlank(str)){
            return;
        }
        this.lastUploadLabel.setText("最后一次上传成功时间："+str);
    }

    public void appendText(String str){
        if(this.textArea == null){
            return;
        }
        this.textArea.append(str+"\n");
        this.textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
