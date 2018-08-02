package com.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
  
public class DesUtil {  
    /** 
     * <p> DES解密文件 
     * @param file 需要解密的文件 
     * @param filePath 需要解密的文件的路径
     * @param dest 解密后的文件 
     * @param realPath request.getSession().getServletContext().getRealPath("/");
     * @throws Exception 
     */  
    public static void decrypt(File file, String filePath, String dest, String realPath) throws Exception {  
        Cipher cipher = Cipher.getInstance("DES");  
        cipher.init(Cipher.DECRYPT_MODE, getKey(realPath)); 
        InputStream is = null;
        if(StringUtils.isEmpty(filePath)){
        	is = new FileInputStream(filePath); 
        }else{
        	is = new FileInputStream(file);
        }
        OutputStream out = new FileOutputStream(dest);  
        CipherOutputStream cos = new CipherOutputStream(out, cipher);  
        byte[] buffer = new byte[1024];  
        int r;  
        while ((r = is.read(buffer)) >= 0) {  
            cos.write(buffer, 0, r);  
        }  
        cos.close();  
        out.close();  
        is.close();  
    }  
    /** 
     * <p>DES加密文件 
     * @param file 源文件 
     * @param destFile 加密后的文件 
     * @param realPath request.getSession().getServletContext().getRealPath("/");
     * @throws Exception 
     */  
    public static void encrypt(String file, String destFile, String realPath) throws Exception {  
            Cipher cipher = Cipher.getInstance("DES");  
            cipher.init(Cipher.ENCRYPT_MODE, getKey(realPath));  
            InputStream is = new FileInputStream(file);  
            OutputStream out = new FileOutputStream(destFile);  
            CipherInputStream cis = new CipherInputStream(is, cipher);  
            byte[] buffer = new byte[1024];  
            int r;  
            while ((r = cis.read(buffer)) > 0) {  
                out.write(buffer, 0, r);  
            }  
            cis.close();  
            is.close();  
            out.close();  
        }  
    private static  Key getKey(String realPath) {  
        Key kp = null;  
        try {  
        	String filePath = realPath + "/licences/DesKey.xml";
        	
            InputStream is = new FileInputStream(filePath);
            ObjectInputStream oos = new ObjectInputStream(is);  
            kp = (Key) oos.readObject();  
            oos.close();  
        } catch (Exception e) {  
        }  
        return kp;  
    }  

}  
