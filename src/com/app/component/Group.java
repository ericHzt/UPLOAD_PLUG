package com.app.component;

import javax.swing.*;
import java.awt.*;

public class Group extends JPanel{

    private JTextField invoiceField;
    private JTextField checkCodeField;
    private JButton delBtn;
    private Integer x;
    private Integer y;

    public Group(Integer x,Integer y,String invoiceStr,String checkCodeStr){
        this.x = x;
        this.y = y;
        init(invoiceStr,checkCodeStr);
    }

    public void init(String invoiceStr,String checkCodeStr){
        this.setLayout(null);
        this.setSize(110,20);
        this.add(getInvoiceField(invoiceStr));
        this.add(getCheckCodeField(checkCodeStr));
    }

    public JTextField getInvoiceField(String str) {
        if(invoiceField == null){
            invoiceField = new JTextField(str);
            invoiceField.setBounds(new Rectangle(0,10,40,20));
        }
        return invoiceField;
    }


    public JTextField getCheckCodeField(String str) {
        if(checkCodeField == null){
            checkCodeField = new JTextField(str);
            checkCodeField.setBounds(new Rectangle(45,10,40,20));
        }
        return checkCodeField;
    }


    public JButton getDelBtn() {
        if(delBtn==null){
            delBtn = new JButton("删除");
            delBtn.setBounds(new Rectangle(90,10,20,20));
        }
        return delBtn;
    }

}
