package models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name = "t_pkey")
@Entity
public class PackPKey extends Model {

	@Required
	@Unique
	public String pkey;
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.ALL)
	public Pack pack;
	
	public String toString(){
		return pkey;
	}
}