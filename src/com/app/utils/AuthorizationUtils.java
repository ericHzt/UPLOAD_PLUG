package com.app.utils;

import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * java获取mac和机器码，注册码的实现
 * 
 */
public class AuthorizationUtils {
	private static final int SPLITLENGTH = 4;
	public static final String SALT = "easyit2016";

	public static String getSplitString(String str) {
		return getSplitString(str, "-", SPLITLENGTH);
	}

	private static String getSplitString(String str, String split, int length) {
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < len; i++) {
			if (i % length == 0 && i > 0) {
				temp.append(split);
			}
			temp.append(str.charAt(i));
		}
		String[] attrs = temp.toString().split(split);
		StringBuilder finalMachineCode = new StringBuilder();
		for (String attr : attrs) {
			if (attr.length() == length) {
				finalMachineCode.append(attr).append(split);
			}
		}
		String result = finalMachineCode.toString().substring(0,
				finalMachineCode.toString().length() - 1);
		return result;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	// ‎00-24-7E-0A-22-93
	public static String getMac() {
		StringBuffer sb = new StringBuffer();
		try {
			Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				byte[] mac = el.nextElement().getHardwareAddress();
				if (mac == null || mac.length == 0)
					continue;
				String hexstr = bytesToHexString(mac);
				//return getSplitString(hexstr, "-", 2).toUpperCase();
				sb.append(",");
				sb.append(getSplitString(hexstr, "-", 2).toUpperCase());

			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if(sb.toString().length() > 1){
			return sb.toString().substring(1);
		}else{
			return sb.toString().substring(1);
		}
	}
}