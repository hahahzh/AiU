package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Table(name = "logs")
@Entity
public class Log extends Model {

	public String customer_name;
	
	public Date data = new Date();

	public String toString() {
		return customer_name;
	}
}