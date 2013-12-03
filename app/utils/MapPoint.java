package utils;

public class MapPoint {
	
	private Double latitude;
	private Double longitude;
	
	public MapPoint(Double latitude,Double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String toString(){
		return "(latitude,longitude)  = ("+this.latitude+","+this.longitude+")";
	}
	
}
