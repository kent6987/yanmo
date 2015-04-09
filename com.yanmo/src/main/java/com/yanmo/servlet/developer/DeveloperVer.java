package com.yanmo.servlet.developer;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author "DingYuan"
 */
public class DeveloperVer extends HttpServlet {

	private static final long serialVersionUID = 502847197818636744L;
	/**
	 * 随机字符串
	 */
	private static final String ECHOSTR2 = "echostr";
	/**
	 * 随机数
	 */
	private static final String NONCE2 = "nonce";
	/**
	 * 时间戳
	 */
	private static final String TIMESTAMP2 = "timestamp";
	/**
	 * 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
	 */
	private static final String SIGNATURE2 = "signature";

	private static final String TOKEN = "bbb";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String timestamp = req.getParameter(TIMESTAMP2);
		String nonce = req.getParameter(NONCE2);
		try {
			String echostr = req.getParameter(SIGNATURE2).equals(sha1(generateSign(Arrays.asList(nonce, timestamp, TOKEN)))) ? req
					.getParameter(ECHOSTR2)
					: "aaa";
			resp.getWriter().write(echostr);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private String sha1(String sourceStr) throws NoSuchAlgorithmException {
		if (sourceStr == null || sourceStr.length() == 0) {
			return "";
		}
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		return byte2hexString(md.digest(sourceStr.getBytes()));
	}
	
	public static void main(String[] args) {
		try {
			String sha1 = new DeveloperVer().sha1("123123aaabbb");
			System.out.println(sha1);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private static String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

	/**
	 * 字典排序参数序列
	 */
	private String generateSign(List<String> params) {
		Collections.sort(params);
		return splice(params);
	}

	/**
	 * 拼接字符串
	 */
	private String splice(List<String> params) {
		if (params == null || params.size() == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (String temp : params) {
			sb.append(temp);
		}
		return sb.toString();
	}
}
