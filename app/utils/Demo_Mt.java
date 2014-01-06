package utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

import models.CheckDigit;
import play.Play;

/**
 * 发送短信
 * 
 */
public class Demo_Mt {
	public static String sendSMS(String m, String content)
			throws UnsupportedEncodingException {
		// 输入软件序列号和密码
		String sn = "SDK-TOM-010-00054";//Play.configuration.getProperty("aiu.sn");
		String pwd = "e986-2]f";//Play.configuration.getProperty("aiu.pwd");
		Client client = new Client(sn, pwd);
		// 我们的Demo最后是拼成xml了，所以要按照xml的语法来转义
		if (content.indexOf("&") >= 0) {
			content = content.replace("&", "&amp;");
		}

		if (content.indexOf("<") >= 0) {
			content = content.replace("<", "&lt;");
		}
		if (content.indexOf(">") >= 0) {
			content = content.replace(">", "&gt;");
		}

		// 短信发送
		return client.mt(m, content, "", "", "");
	}
	
	public static void main(String[] args){
		Random r = new Random();
		int n = Math.abs(r.nextInt())/10000;
//		CheckDigit cd = new CheckDigit();
//		cd.d = n;
//		cd.updatetime = new Date().getTime();
//		cd._save();
		
		try {
			String a = Demo_Mt.sendSMS("15000993473", "矮油互动娱乐欢迎您！验证码:"+n);
			System.out.println(a);
		} catch (UnsupportedEncodingException e) {
//			cd._delete();
			play.Logger.error(e.getMessage());
		}
	}
}