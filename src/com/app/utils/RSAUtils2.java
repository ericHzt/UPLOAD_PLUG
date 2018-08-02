package com.app.utils;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;



import com.sun.org.apache.xml.internal.security.utils.Base64;

public class RSAUtils2 {

	public final static String ALGORITHM = "RSA";
	public final static String SIGNATURE_ALGORITHM = "MD5withRSA";
	 /**
     * RSA�?大加密明文大�?
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * RSA�?大解密密文大�?
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
	/**
	 * 获取公钥密钥�?
	 * @return
	 * @throws Exception
	 */
	public static KeyPair getKey() throws Exception{
		KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
		return generator.generateKeyPair();
	}
	
	private static Key getPublicKey(String key)throws Exception{
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Utils.decode(key));
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key k = keyFactory.generatePublic(keySpec);
		return k;
	}
	
	private static Key getPrivateKey(String key)throws Exception{
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(key));
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key k = keyFactory.generatePrivate(keySpec);
		return k;
	}
	
	/**
	 * 使用公钥进行加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data,String key)throws Exception{
		
		Key k = getPublicKey(key);
		
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		
		byte[] decode64Data = data.getBytes("UTF-8");
		
		int inputLen = decode64Data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加�?
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(decode64Data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(decode64Data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
		
		//byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
		
		return Base64Utils.encode(encryptedData);
	}
	
	/**
	 * 使用私钥进行加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String data, String key)throws Exception{
		
		Key k = getPrivateKey(key);
		
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		
		byte[] decode64Data = data.getBytes("UTF-8");
		int inputLen = decode64Data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加�?
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(decode64Data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(decode64Data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
		
		//byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
		
		return Base64Utils.encode(encryptedData);
	}
	
	/**
	 * 使用私钥进行解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data,String key)throws Exception{
		Key k = getPrivateKey(key);
		
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		
		//byte[] bytes = cipher.doFinal(Base64Utils.decode(data));
		
		byte[] encryptedData = Base64Utils.decode(data);
		int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解�?
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
		
		return new String(decryptedData, "UTF-8");
	}
	
	/**
	 * 使用公钥进行解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPublicKey(String data,String key)throws Exception{
		Key k = getPublicKey(key);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		
		//byte[] bytes = cipher.doFinal(Base64Utils.decode(data));
		byte[] encryptedData = Base64Utils.decode(data);
		int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解�?
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        
		return new String(decryptedData,"UTF-8");
	}
	
	/**
	 * 使用私钥进行签名
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String sign(String data,String key)throws Exception{
		PrivateKey k = (PrivateKey)getPrivateKey(key);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initSign(k);  
        signature.update(data.getBytes("UTF-8"));  
        return Base64.encode(signature.sign());
	}
	
	/**
	 * 使用公钥进行签名验证
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static boolean signVerify(String data,String key,String sign)throws Exception{
		PublicKey k = (PublicKey)getPublicKey(key);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initVerify(k);  
        signature.update(data.getBytes("UTF-8"));
        return signature.verify(Base64.decode(sign));  
	}

}
