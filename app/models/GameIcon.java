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
import utils.CompressPic;
import check.PicCompres;

@Table(name = "gameicon")
@Entity
public class GameIcon extends Model {

	@Required
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	public Game game;

	@Required
	public Blob picture1;

	public Blob picture2;

	public Blob picture3;

	public Blob picture4;

	public Blob picture5;

	public Blob picture6;

	@CheckWith(PicCompres.class)
	public Blob backgroundpicture;

	public Blob getBackgroundpicture() {
		return backgroundpicture;
	}

	public void setBackgroundpicture(Blob backgroundpicture) {
		if(backgroundpicture != null){
			CompressPic.compressPic(backgroundpicture.getFile().getPath(), backgroundpicture.getFile().getPath());
		}
		this.backgroundpicture = backgroundpicture;
	}

	public String toString() {
		return game.title;
	}
}