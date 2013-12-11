package utils;
import java.io.UnsupportedEncodingException;


public class Demo_UDPPwd {
//该Demo是更改密�?
	public static void main()throws UnsupportedEncodingException{
		//输入您的软件序列号和密码
		String sn="";
		String pwd="";

		Client client=new Client(sn,pwd);
		
		
		String result_charge = client.UDPPwd("173736");
		System.out.println(result_charge);
	}
}
