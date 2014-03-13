package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import controllers.CRUD.Hidden;
import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

@Table(name = "clientversion")
@Entity
public class ClientVersion extends Model {

	@Required
	@URL
	public String url;
	
	@MaxSize(200)
	public String update_aiu;
	
	@Required
	@Index(name = "idx_version")
	public String version;
	
	@Required
	@Hidden
	public Date data = new Date();
	
	@Required
	public int mobiletype;
	
	public String toString() {
		return url;
	}
}