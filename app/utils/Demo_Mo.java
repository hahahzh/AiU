package utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 接收短信
 * @author acer
 *
 */
public class Demo_Mo {
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		//输入软件序列号和密码
		String sn="";
		String pwd="";
		Client client=new Client(sn,pwd);
		
		//接收短信
		String result_mo = client.mo();
		if (result_mo.startsWith("-")) {
			//接收失败的情况，输出失败信息
			System.out.print(result_mo+" 序列号或密码不对");
		}else if ("1" == result_mo) {
			System.out.print("无可接收信息");
		}else {
			//多条信息的情况，以回车换行分隔
			String[] result = result_mo.split("\r\n");
		for(int i=0;i<result.length;i++)
			{
				//内容做了url编码，在此解码，编码方式gb2312
				System.out.print(URLDecoder.decode(result[i], "gb2312") + "\r\n");
			}
		}
	}

}
