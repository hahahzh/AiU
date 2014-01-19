package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;



@Table(name="p_g_advertisement")
@Entity
public class PlusGame extends Model {
	@Required
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	public Game game;

	public String toString(){
		return game.title;
	}
}
