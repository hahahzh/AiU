package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import controllers.CRUD.Hidden;

@Table(name = "customer")
@Entity
public class Customer extends Model {

	@MaxSize(5)
	public String cid;

	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.ALL)
	public ClientVersion vid;

	public String mac;

	public int os;

	public String type;

	@Required
	@Phone
	@Unique
	@Index(name = "idx_m_number")
	public String m_number;

	@Required
	@MaxSize(15)
	@MinSize(6)
	@Match(value = "^\\w*$", message = "Not a valid username")
	public String nickname;

	@Required
	@MaxSize(15)
	@MinSize(5)
	@Password
	public String psd;

	public String imei;

	public Long exp;
	
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.ALL)
	public LevelType lv;

	@Hidden
	public Long data = new Date().getTime();

	public Byte gender;
	
	public Blob portrait;
	
	public String serialNumber;
	
	@OneToMany(fetch=FetchType.LAZY,cascade = { CascadeType.REMOVE})
	public List<Game> addgame;
	
	public String toString() {
		return nickname;
	}
}