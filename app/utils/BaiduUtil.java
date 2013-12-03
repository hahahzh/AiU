package utils;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import play.Play;

public class BaiduUtil {
	private static String url = Play.configuration.getProperty("convert_url",null);
	
	public static MapPoint convert(MapPoint source){
		MapPoint resultMapPoint = null;
		if(source == null)
			return null;
		String tempUrl = url.replace("$longitude$", ""+source.getLongitude()).replace("$latitude$", ""+source.getLatitude());
		//String tempUrl = "http://api.map.baidu.com/ag/coord/convert?from=2&to=4&x=$longitude$&y=$latitude$".replace("$longitude$", ""+source.getLongitude()).replace("$latitude$", ""+source.getLatitude());
		String result = HttpTool.getHttpInputStream(tempUrl);
		if(result != null){
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(result);
				int error = jsonObj.getInt("error");
				if(error == 0){
					double x = Double.parseDouble(new String(Coder.decryptBASE64(jsonObj.getString("x"))));
					double y = Double.parseDouble(new String(Coder.decryptBASE64(jsonObj.getString("y"))));
					resultMapPoint =  new MapPoint(y,x);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultMapPoint;
	}
	
    public static double degreePoint2degree(int flag, double value) {
        DecimalFormat df = new DecimalFormat("#.000000");
        // 取度
        int degreeValue = (int)(value / 100);
        // 取分
        double secondValue = value - (degreeValue * 100);
        // 换算成度的单位
        double result = degreeValue + secondValue / 60;
        if (flag == 0) {
            result = result * -1;
        }
        result = Double.parseDouble(df.format(result));
        return result;

    }
    public static double radiansPoint2radians(int flag, double value) {
        DecimalFormat df = new DecimalFormat("#.000000");
    
        int degreeValue = (int)value;
        
        double secondValue = (value - degreeValue)*60;
        
        double result = secondValue + degreeValue * 100;
 
        result = Double.parseDouble(df.format(result));
        return result;

    }
    
	public static void main(String[] args) {
	}
}