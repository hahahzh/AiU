package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import play.Logger;
import play.Play;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;
import javapns.exceptions.DuplicateDeviceException;
import javapns.exceptions.NullDeviceTokenException;
import javapns.exceptions.NullIdException;
import javapns.exceptions.UnknownDeviceException;

public class APNSUtil {
	public static final String host = "gateway.sandbox.push.apple.com"; // 照抄就可以了
	public static final int port = 2195;// 照抄就可以了
	private static final PushNotificationManager pushManager = PushNotificationManager.getInstance();// 照抄就可以了
	private static Set<String> tokens = new HashSet();
	
	public static void initializeConnection() throws Exception{
			pushManager.initializeConnection(host, port, Play.getFile("aps_developer_identity.p12").getAbsolutePath(), "abc123",
					SSLConnectionHelper.KEYSTORE_TYPE_PKCS12); // 以下都照抄吧
			tokens.clear();
	}
	
	public static void stopConnection(){
		try {
			pushManager.stopConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String token:tokens){
			try {
				pushManager.removeDevice(token);
			} catch (UnknownDeviceException e) {
				e.printStackTrace();
			} catch (NullIdException e) {
				e.printStackTrace();
			}
		}
		tokens.clear();
	}

	
	public static synchronized void send(String deviceToken, String alert,int bandge,String sound,Map<String,String> values) {
		try {
			PayLoad payLoad = new PayLoad();
			payLoad.addAlert(alert); // 手机端的提示消息
			payLoad.addBadge(1); // 图标小红圈的数值
			payLoad.addSound(sound); // 提示声音
			for(String key:values.keySet()){
				payLoad.addCustomDictionary(key, values.get(key)); // 自定义的json属性
			}

			if(!tokens.contains(deviceToken)){
				try{
						pushManager.addDevice(deviceToken, deviceToken); // iphone手机端的唯一标识
						tokens.add(deviceToken);
				}catch(Exception e){
					
				}
			}
			//String certificatePath = ""; // 这里是一个.p12格式的文件路径，需要去apple官网申请一个
			//String certificatePassword = ""; // 这个.p12文件的密码
			Device client = pushManager.getDevice(deviceToken);
			pushManager.sendNotification(client, payLoad);
		} catch (Exception e) {
			Logger.error(e, "[APNSUtil] apn send failed. token = "+deviceToken);
		}
	}
	
	public static void main(String [] argv) throws Exception{
		long start = System.currentTimeMillis();
		APNSUtil.initializeConnection();
		APNSUtil.send("58e0e1b82a3f2f242bc2abfc8b5c5e0245ade2618b19b4d94c06479ffc157e6b", "您好,我是来自ilbs的通知哦", 1, "default", new HashMap());
		System.out.println(System.currentTimeMillis() - start);
		APNSUtil.send("58e0e1b82a3f2f242bc2abfc8b5c5e0245ade2618b19b4d94c06479ffc157e6b", "您好,我是来自ilbs的通知哦", 1, "default", new HashMap());
		System.out.println(System.currentTimeMillis() - start);
		APNSUtil.stopConnection();
	}
}
