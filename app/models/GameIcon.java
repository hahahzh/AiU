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
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
	public Game game;

	public Blob picture1_g_icon;

	public Blob picture2_g_icon;

	public Blob picture3_g_icon;

	public Blob picture4_g_icon;

	public Blob picture5_g_icon;

	public Blob picture6_g_icon;

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