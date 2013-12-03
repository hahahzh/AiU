package utils;

public class MD5Tool {
	
	private static MD5 md5;
	
	
	public static MD5 getInstance(){
		
		if(null == md5)
			md5 = new MD5();

		return md5;
	}
	
	//String to MD5
	public static String getMD5String(String str) {
		
		if(null==str || "".equals(str))
			return null;
		
		return MD5Tool.getInstance().getMD5ofStr(str);
	}
	
	public static void main(String[] args) {
		System.out.println(MD5Tool.getMD5String("1020111012140916l,@msvj4#"));
	}

}