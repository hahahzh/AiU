package models;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import play.db.jpa.Model;

@Table(name = "cpkey")
@Entity
public class CPkey extends Model {

	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REMOVE)
	public Customer c;

	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REMOVE)
	public Pack p;
	
	public long updatetime;
	
}