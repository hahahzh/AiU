package check;

import play.data.validation.Check;
import play.db.jpa.Blob;
import play.db.jpa.FileAttachment;

@SuppressWarnings("deprecation")
public class PicCompres extends Check{
	private static final String [] ACCEPTABLE_EXT = {"jpg","jpeg"};
	@Override
	public boolean isSatisfied(Object validatedObject, Object value) {
		setMessage("格式必须为jpg");
		
		if(value != null && value instanceof Blob){
			Blob tmp = (Blob)value;
				if(tmp.exists()){
				try {
					// 未添加图标直接返回TRUE
					
					
						String fName = tmp.type();
						// 文件类型Check 匹配到支持的后缀名，则通过
						for(String ext:ACCEPTABLE_EXT){
				    		if(fName.trim().toLowerCase().endsWith(ext)){
				    			return true;
				    		}
				    	}
					return false;
				} catch (Exception e) {
					setMessage("格式必须为jpg");
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
}