package com.app.utils;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES加密 解密算法  使用固定秘钥
 * key的长度最短为8
 * @author 
 *
 */
public class DESEncryptUtil {

    private final static String DES = "DES";
    private final static String ENCODE = "UTF-8";
    private final static String defaultKey = "ACTDefaultKey";

    /**
     * 使用 默认key 加密
     * @param data 待加密数�?
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), defaultKey);
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * 使用 默认key 解密
     * @param data 待解密数�?
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data) throws IOException, Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, defaultKey);
        return new String(bt, ENCODE);
    }

    /**
     * Description 根据键�?�进行加�?
     * @param data 待加密数�?
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), key);
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * 根据键�?�进行解�?
     * @param data 待解密数�?
     * @param key    密钥
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key);
        return new String(bt, ENCODE);
    }

    /**
     * Description 根据键�?�进行加�?
     * 
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, String key) throws Exception {
        /*// 生成�?个可信任的随机数�?
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建�?个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);*/

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, getKey(key));

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键�?�进行解�?
     * 
     * @param data
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, String key) throws Exception {
        /*// 生成�?个可信任的随机数�?
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建�?个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);*/

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, getKey(key));

        return cipher.doFinal(data);
    }
    
	//  根据参数生成KEY
	private static SecretKey getKey(String key) {
	    try {
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); 
	        DESKeySpec keySpec = new DESKeySpec(key.getBytes(ENCODE)); 
	        keyFactory.generateSecret(keySpec); 
	        return keyFactory.generateSecret(keySpec);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	 
	    return null;
	}
    
    public static void main(String[] args){
        String data = "12345";
        String key ="123420180612";
        try {
        	System.out.println("加密�?===>"+data);
        	data = encrypt(data, key);
        	//data = "TwMFCfGhAlE=";
            System.err.println("加密�?===>"+data);
            System.err.println("加密�?===>"+decrypt(data, key));
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}