package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import check.PicCompres;

@Table(name = "gameicon")
@Entity
public class GameIcon extends Model {

	@Required
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	public Game game;

	@Required
	//@CheckWith(PicCompres.class)
	public Blob picture1;

	//public String txt1;

	public Blob picture2;

	//public String txt2;

	public Blob picture3;

	//public String txt3;

	public Blob picture4;

	//public String txt4;

	public Blob picture5;

	//public String txt5;

	public Blob picture6;

	//public String txt6;
	
	public Blob backgroundpicture;

	public String toString() {
		return game.title;
	}
}