package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Table(name = "gameicon")
@Entity
public class GameIcon extends Model {

	@Required
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	public Game game;

	@Required
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

	public String toString() {
		return game.title;
	}
}