package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.h2.constant.SysProperties;

import play.Play;

/**
 * 鏂囦欢鍚嶇О锛歴endSMS_demo.java
 * 
 * 鏂囦欢浣滅敤锛氱編鑱旇蒋閫�http鎺ュ彛浣跨敤瀹炰緥
 * 
 * 鍒涘缓鏃堕棿锛�012-05-18
 * 
 * 
杩斿洖鍊�										璇存槑
success:msgid								鎻愪氦鎴愬姛锛屽彂閫佺姸鎬佽瑙�.1
error:msgid									鎻愪氦澶辫触
error:Missing username						鐢ㄦ埛鍚嶄负绌�
error:Missing password						瀵嗙爜涓虹┖
error:Missing apikey						APIKEY涓虹┖
error:Missing recipient						鎵嬫満鍙风爜涓虹┖
error:Missing message content				鐭俊鍐呭涓虹┖
error:Account is blocked					甯愬彿琚鐢�
error:Unrecognized encoding					缂栫爜鏈兘璇嗗埆
error:APIKEY or password error				APIKEY 鎴栧瘑鐮侀敊璇�
error:Unauthorized IP address				鏈巿鏉�IP 鍦板潃
error:Account balance is insufficient		浣欓涓嶈冻
error:Black keywords is:鍏氫腑澶�			灞忚斀璇�
 */


public class SendSMS {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
	
		// 鍒涘缓StringBuffer瀵硅薄鐢ㄦ潵鎿嶄綔瀛楃涓�
		StringBuffer sb = new StringBuffer("http://m.5c.com.cn/api/send/?");
		// APIKEY
		sb.append("apikey=6cba7a9cdd47494d58101c111f608583");
		//鐢ㄦ埛鍚�
		sb.append("&username=suyou");
		// 鍚慡tringBuffer杩藉姞瀵嗙爜
		sb.append("&password=suyou123");
		// 鍚慡tringBuffer杩藉姞鎵嬫満鍙风爜
		sb.append("&mobile=15000993473");//,18501667323,15021091765,13564635042");
		// 鍚慡tringBuffer杩藉姞娑堟伅鍐呭杞琔RL鏍囧噯鐮�
		sb.append("&content=123456");
		// 鍒涘缓url瀵硅薄
		URL url = new URL(sb.toString());
		// 鎵撳紑url杩炴帴
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 璁剧疆url璇锋眰鏂瑰紡 鈥榞et鈥�鎴栬� 鈥榩ost鈥�
		connection.setRequestMethod("POST");
		// 鍙戦�
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		// 杩斿洖鍙戦�缁撴灉
		System.out.println(in.readLine()); 
	}
	
	public static String send(String m, String content) throws IOException{
		// 鍒涘缓StringBuffer瀵硅薄鐢ㄦ潵鎿嶄綔瀛楃涓�
		StringBuffer sb = new StringBuffer(Play.configuration.getProperty("aiu.smsmurl"));
		// APIKEY
		sb.append("apikey="+Play.configuration.getProperty("aiu.apikey"));
		//鐢ㄦ埛鍚�
		sb.append("&username="+Play.configuration.getProperty("aiu.username"));
		// 鍚慡tringBuffer杩藉姞瀵嗙爜
		sb.append("&password="+Play.configuration.getProperty("aiu.pwd"));
		// 鍚慡tringBuffer杩藉姞鎵嬫満鍙风爜
		sb.append("&mobile="+m);//,18501667323,15021091765,13564635042");
		// 鍚慡tringBuffer杩藉姞娑堟伅鍐呭杞琔RL鏍囧噯鐮�
		sb.append("&content="+new String(content.getBytes("UTF-8"), "GBK"));
		// 鍒涘缓url瀵硅薄
		URL url = new URL(sb.toString());
		// 鎵撳紑url杩炴帴
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 璁剧疆url璇锋眰鏂瑰紡 鈥榞et鈥�鎴栬� 鈥榩ost鈥�
		connection.setRequestMethod("POST");
		// 鍙戦�
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		// 杩斿洖鍙戦�缁撴灉
		return in.readLine();

	}

}
