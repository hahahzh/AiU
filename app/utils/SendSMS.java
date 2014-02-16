package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

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
	private static final String TWO = "2";
	public static void main(String[] args) throws IOException, DocumentException {
		StringBuffer surl = new StringBuffer("http://106.ihuyi.com/webservice/sms.php?method=Submit&");
		// 账号
		surl.append("account=cf_tanlian");
		// 密码
		surl.append("&password=tanlian123");
		// 鍚慡tringBuffer杩藉姞鎵嬫満鍙风爜
		surl.append("&mobile=150000993473");//,18501667323,15021091765,13564635042");
		// 内容
		surl.append("&content=您的验证码是：1783456。请不要把验证码泄露给其他人。");
		// 鍒涘缓url瀵硅薄
		URL url = new URL(surl.toString());
		// 鎵撳紑url杩炴帴
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 璁剧疆url璇锋眰鏂瑰紡 鈥榞et鈥�鎴栬� 鈥榩ost鈥�
		connection.setRequestMethod("GET");
		// 鍙戦�
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
	}
	
	public static String send(String m, String content) throws IOException, DocumentException{
		StringBuffer surl = new StringBuffer(Play.configuration.getProperty("aiu.smsmurl"));
		//鐢ㄦ埛鍚�
		surl.append("account="+Play.configuration.getProperty("aiu.username"));
		// 鍚慡tringBuffer杩藉姞瀵嗙爜
		surl.append("&password="+Play.configuration.getProperty("aiu.pwd"));
		// 鍚慡tringBuffer杩藉姞鎵嬫満鍙风爜
		surl.append("&mobile="+m);//,18501667323,15021091765,13564635042");
		surl.append("&content="+content);
		// 鍒涘缓url瀵硅薄
		URL url = new URL(surl.toString());
		// 鎵撳紑url杩炴帴
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 璁剧疆url璇锋眰鏂瑰紡 鈥榞et鈥�鎴栬� 鈥榩ost鈥�
		connection.setRequestMethod("GET");
		// 鍙戦�
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		String line;
		StringBuilder result = new StringBuilder(64);
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        Document doc = DocumentHelper.parseText(result.toString());
        Element rootElt = doc.getRootElement();
        Element codeElt = rootElt.element("code");
        if(!TWO.equals(codeElt.getStringValue())){
        	return rootElt.element("msg").getStringValue();
        }else{
        	return codeElt.getStringValue();
        }
	}

}