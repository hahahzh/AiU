package utils;
import java.io.UnsupportedEncodingException;

/**
 * 注销序列号
 * 
 *
 */
public class Demo_UnRegister {
	public static void main(String[] args) throws UnsupportedEncodingException{
		//输入软件序列号和密码
		String sn="";
		String pwd="";
		Client client=new Client(sn,pwd);
		
		//注销方法
		String result_UnRegisterResult=client.UnRegister();

		//System.out.print(result_UnRegisterResult);
		//该demo没有对空值进行判断
		if(result_UnRegisterResult.startsWith("-"))
		{
			System.out.print("注销失败,返回"+result_UnRegisterResult);
		}else
		{
			System.out.print("注销成功："+result_UnRegisterResult);
		}
	}
}
