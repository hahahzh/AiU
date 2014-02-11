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

@Table(name = "game_message")
@Entity
public class GameMessage extends Model {

	@MaxSize(30)
	public String msg;
	
	public long data;
	
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;
	
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public FirmNew news;
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Customer c;
	
	public String toString(){
		return msg;
	}
}