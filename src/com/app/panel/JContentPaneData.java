package com.app.panel;

import javax.swing.*;
import java.awt.*;

public class JContentPaneData extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image image; // 定义图像对象
    private JPanel searchPanel;
    private JTextArea textArea;
    private JLabel lastUploadLabel;
    private JLabel messageLabel;
    public JContentPaneData(Image image,String message) {
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
    }
}
