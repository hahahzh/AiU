package utils;
import java.io.UnsupportedEncodingException;


/**
 * 发送个性短信,即给不同的手机号发不同的内容,短信内容和手机号用英文的逗号对应好
 * 
 *
 */
public class Demo_Gxmt {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		//输入软件序列号和密码
		String sn="";
		String pwd="";
		Client client=new Client(sn,pwd);		
		//发送个性短信		
		String result = client.gxmt("1532185855,1339528010", URLEncoder.encode("短信内容1", "GB2312")+","+URLEncoder.encode("短信内容2", "GB2312"), "", "", "");
		//输出返回标识
		System.out.print(result);
	}
}
