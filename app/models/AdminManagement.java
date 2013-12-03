package models;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.db.jpa.Model;

@Table(name = "appadmin")
@Entity
public class AdminManagement extends Model {

	@Required
	@MaxSize(15)
	@MinSize(6)
	@Match(value = "^\\w*$", message = "Not a valid username")
	public String name;

	@Required
	@MaxSize(15)
	@MinSize(5)
	@Password
	public String psd;
	
	public Byte role;
	
	public Integer admingroup;
	
	@OneToOne(optional = false, cascade = { CascadeType.ALL},fetch=FetchType.EAGER)
	public Game game;

	public String toString() {
		return name;
	}

}