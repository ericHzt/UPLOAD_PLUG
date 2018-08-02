package com.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

import com.app.utils.PropertiesLoader;

/**
 * @author ERIC
 *
 */
public class Dao {
	private static Dao dao = new Dao(); // 声明DAO类的静态实例
	
	private static PropertiesLoader loader;
	
	/**
     * 构造方法，加载数据库驱动
     */
    public Dao(){
		loader = new PropertiesLoader("app.properties");
        try {
            Class.forName(loader.getProperty("jdbc.driver")); // 加载数据库驱动
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "数据库驱动加载失败，请将JDBC驱动配置到构建路径中。\n"
                    + e.getMessage());
        }
    }
    
    /**
     * 获得数据库连接的方法
     * 
     * @return Connection
     */
    public static Connection getConn() {
        try {
        	
            Connection conn = null; // 定义数据库连接
            String url = loader.getProperty("jdbc.url"); // 数据库db_Express的URL
            String username = loader.getProperty("jdbc.username"); // 数据库的用户名
            String password = loader.getProperty("jdbc.password"); // 数据库密码
            conn = DriverManager.getConnection(url, username, password); // 建立连接
            return conn; // 返回连接
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "数据库连接失败。\n请检查URL地址，\n以及数据库用户名和密码是否正确。"
                            + e.getMessage());
            return null;
        }
    }
}
