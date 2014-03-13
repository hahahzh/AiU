package models;


import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Table(name = "logs")
@Entity
public class Log extends Model {

	public String customer_name;
	
	public Long data;

	public String ip;
	
	public String mac;
	
	public String imei;
	
	public String type;
	
	public String toString() {
		return customer_name;
	}
}