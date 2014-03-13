package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import controllers.CRUD.Hidden;

@Table(name = "pubchannel")
@Entity
public class PublicChannel extends Model {

	public Blob icon;
	
	@Required
	@MaxSize(20)
	@MinSize(2)
	public String pubchannelname;
	
	public String exp;
	
	@Hidden
	public Long data = new Date().getTime();

	public String toString() {
		return pubchannelname;
	}
}