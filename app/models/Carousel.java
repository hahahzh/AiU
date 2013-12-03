package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name = "adlist")
@Entity
public class Carousel extends Model {

	@Required
	@OneToOne(optional = false, cascade = { CascadeType.ALL},fetch=FetchType.EAGER)
	@Unique
	public Game ad_game;
	
	@Required
	public int mtype;

	public String toString() {
		return ad_game.toString();
	}
}