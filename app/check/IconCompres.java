package check;

import play.data.validation.Check;
import play.db.jpa.Blob;
import play.db.jpa.FileAttachment;

@SuppressWarnings("deprecation")
public class IconCompres extends Check{
	private static final String [] ACCEPTABLE_EXT = {"jpg","jpeg"};
	@Override
	public boolean isSatisfied(Object validatedObject, Object value) {
		setMessage("validation.icon");
		
		if(value != null && value instanceof Blob){
			try {
				// 未添加图标直接返回TRUE
				String fName = ((Blob)value).getFile().getName();
				if(fName == null || "".equals(fName))return true;
				
				// 图标Size不能大于20KB
				if(((FileAttachment)value).getStore() == null)return true;
				if(((FileAttachment)value).get() == null)return true;
				Long s = 0L;//FileUtil.getFileSizes(((FileAttachment)value).get().getPath());
				if(s >= 20000){
					return false;
				}
				
				// 文件类型Check 匹配到支持的后缀名，则通过
				for(String ext:ACCEPTABLE_EXT){
		    		if(fName.trim().toLowerCase().endsWith(ext)){
		    			return true;
		    		}
		    	}
				return false;
			} catch (Exception e) {
				setMessage("4000");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}