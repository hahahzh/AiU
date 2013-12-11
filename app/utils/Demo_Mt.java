package utils;

import java.io.UnsupportedEncodingException;

import play.Play;

/**
 * 发送短信
 * 
 */
public class Demo_Mt {
	public static String sendSMS(String m, String content)
			throws UnsupportedEncodingException {
		// 输入软件序列号和密码
		String sn = Play.configuration.getProperty("aiu.sn");
		String pwd = Play.configuration.getProperty("aiu.pwd");
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
}